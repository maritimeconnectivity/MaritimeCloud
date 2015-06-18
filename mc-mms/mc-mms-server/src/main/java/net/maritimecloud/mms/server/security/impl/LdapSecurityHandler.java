/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.maritimecloud.mms.server.security.impl;

import com.typesafe.config.Config;
import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.AuthenticationHandler;
import net.maritimecloud.mms.server.security.AuthenticationToken;
import net.maritimecloud.mms.server.security.AuthorizationException;
import net.maritimecloud.mms.server.security.AuthorizationHandler;
import net.maritimecloud.mms.server.security.ClientVerificationException;
import net.maritimecloud.mms.server.security.ClientVerificationHandler;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Handles authentication and authorization against LDAP.
 *
 * <h2>Authentication</h2>
 * Implements the {@code AuthenticationHandler} interface and attempts
 * to authenticate the client using LDAP. Depending on the configuration, this is done by either binding
 * as the client, or by searching LDAP for the current client using a privileged LDAP user.
 *
 * <h2>Authorization</h2>
 * Implements of the {@code AuthorizationHandler} interface and attempts
 * to authorize the client using LDAP by resolving the group membership of the client.
 */
@SuppressWarnings("unused")
public class LdapSecurityHandler implements AuthenticationHandler, AuthorizationHandler, ClientVerificationHandler {

    protected Config conf;

    /** {@inheritDoc} */
    @Override
    public void init(Config conf) {
        this.conf = conf;
    }

    /** {@inheritDoc} */
    @Override
    public Config getConf() {
        return conf;
    }

    /*************************************************/
    /** Authentication Support                      **/
    /*************************************************/

    /** {@inheritDoc} */
    @Override
    public void authenticate(AuthenticationToken token) throws AuthenticationException {

        if (token == null) {
            throw new AuthenticationException("Invalid authentication token: null");
        }

        // Determine how to authenticate the user
        boolean bindAsUser = conf.hasPath("user-bind-dn");
        boolean searchForUser = conf.hasPath("user-search-bind-dn") &&
                conf.hasPath("user-search-credential") &&
                conf.hasPath("user-search-base-dn") &&
                conf.hasPath("user-search-filter");

        if (bindAsUser) {
            bindAsUser(token);

        } else if (searchForUser) {
            searchForUser(token);

        } else {
            throw new AuthenticationException("Invalid LDAP authentication configuration");
        }

    }

    /**
     * Authenticate by binding as the user given by the principal-credential of the authentication token
     * @param token the authentication token
     * @throws AuthenticationException if the authentication fails
     */
    private void bindAsUser(AuthenticationToken token) throws AuthenticationException {

        DirContext ctx = null;
        try {
            String ldapUrl = conf.getString("ldap-url");
            String bindDn = replaceTokens(conf.getString("user-bind-dn"), token.getPrincipal());
            Object credential = token.getCredentials();

            ctx = new InitialDirContext(createLdapEnv(ldapUrl, bindDn, credential));

        } catch (NamingException e) {
            throw new AuthenticationException("Failed authentication for principal " + token.getPrincipal());
        } finally {
            if (ctx != null) {
                try { ctx.close(); } catch (Exception ex) {}
            }
        }
    }

    /**
     * Search the user given by the authentication token principal
     * @param token the principal
     * @throws AuthenticationException if the authentication fails
     */
    private void searchForUser(AuthenticationToken token) throws AuthenticationException {
        DirContext ctx = null;
        try {
            String ldapUrl = conf.getString("ldap-url");
            String bindDn = conf.getString("user-search-bind-dn");
            String credential = conf.getString("user-search-credential");
            String baseDn = conf.getString("user-search-base-dn");
            String filter = replaceTokens(conf.getString("user-search-filter"), token.getPrincipal());

            ctx = new InitialDirContext(createLdapEnv(ldapUrl, bindDn, credential));

            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(new String[] { "*", "+" });
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> result = ctx.search(baseDn, filter, ctls);
            if (!result.hasMore()) {
                throw new AuthenticationException("Failed authentication for principal " + token.getPrincipal());
            }

        } catch (NamingException e) {
            throw new AuthenticationException("Failed authentication for principal " + token.getPrincipal());
        } finally {
            if (ctx != null) {
                try { ctx.close(); } catch (Exception ex) {}
            }
        }
    }

    /*************************************************/
    /** Authorization Support                       **/
    /*************************************************/

    /** {@inheritDoc} */
    @Override
    public void checkRoles(Object principal, boolean matchAll, String... roleIdentifiers) throws AuthorizationException {

        // Sanity checks
        if (roleIdentifiers == null || roleIdentifiers.length == 0) {
            return;
        }

        if (principal == null) {
            throw new AuthorizationException("Authorization error: User unauthenticated");
        }

        try {
            // TODO: Currently, the implementation supports searching LDAP groups for members.
            // It should also support LDAP schemas where each user entry has group attributes.
            boolean searchGroups = conf.hasPath("group-search-bind-dn") &&
                    conf.hasPath("group-search-credential") &&
                    conf.hasPath("group-search-base-dn") &&
                    conf.hasPath("group-search-filter");
            if (!searchGroups) {
                throw new AuthorizationException("Invalid LDAP group search configuration");
            }

            Set<String> roles = fetchUserGroups(principal);
            if (!matchRoles(roles, matchAll, false, roleIdentifiers)) {
                throw new AuthorizationException("Authorization error");
            }

        } catch (AuthorizationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new AuthorizationException("Error checking user roles", e);
        }
    }

    /**
     * Search out the LDAP groups for the given user
     * @param principal the principal
     * @return the user groups
     */
    private Set<String> fetchUserGroups(Object principal) throws Exception {
        DirContext ctx = null;
        try {
            String ldapUrl = conf.getString("ldap-url");
            String bindDn = conf.getString("group-search-bind-dn");
            String credential = conf.getString("group-search-credential");
            String baseDn = conf.getString("group-search-base-dn");
            String filter = replaceTokens(conf.getString("group-search-filter"), principal.toString());
            String groupRdnAttr = conf.hasPath("group-rdn-attr") ? conf.getString("group-rdn-attr") : "cn";
            Set<String> userGroups = new HashSet<>();

            ctx = new InitialDirContext(createLdapEnv(ldapUrl, bindDn, credential));

            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(new String[] { groupRdnAttr });
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> result = ctx.search(baseDn, filter, ctls);
            while (result.hasMore()) {
                SearchResult entry = result.next();
                Attributes as = entry.getAttributes();

                NamingEnumeration<String> ids = as.getIDs();
                if (ids.hasMore()) {
                    String id = ids.next();
                    Attribute attr = as.get(id);
                    if (attr.size() > 0) {
                        userGroups.add(attr.get(0).toString());
                    }
                }
            }

            return userGroups;
        } finally {
            if (ctx != null) {
                try { ctx.close(); } catch (Exception ex) {}
            }
        }
    }

    /*************************************************/
    /** Client Verification methods                 **/
    /*************************************************/

    @Override
    public void verifyClient(Object principal, String id) throws ClientVerificationException {

        // Sanity checks
        if (principal == null) {
            throw new ClientVerificationException("Client verification error: User unauthenticated");
        } else if (id == null) {
            return;
        }

        try {
            // TODO: Support vessel group membership, not just user membership.
            boolean searchVessels = conf.hasPath("vessel-search-bind-dn") &&
                    conf.hasPath("vessel-search-credential") &&
                    conf.hasPath("vessel-search-base-dn") &&
                    conf.hasPath("vessel-search-filter");
            if (!searchVessels) {
                throw new ClientVerificationException("Invalid LDAP vessel search configuration");
            }

            // Strip "mmsi" prefix
            if (id.startsWith("mmsi:")) {
                id = id.substring("mmsi:".length());
            }
            Set<String> vesselIds = fetchVesselIds(principal);
            if (!vesselIds.contains(id)) {
                throw new ClientVerificationException("User " + principal + " does not have access to maritime id " + id);
            }

        } catch (Exception e) {
            throw new ClientVerificationException("Error checking client id", e);
        }
    }

    /**
     * Search out the LDAP vessels for the given user
     * @param principal the principal
     * @return the vessels
     */
    private Set<String> fetchVesselIds(Object principal) throws Exception {
        DirContext ctx = null;
        try {
            String ldapUrl = conf.getString("ldap-url");
            String bindDn = conf.getString("vessel-search-bind-dn");
            String credential = conf.getString("vessel-search-credential");
            String baseDn = conf.getString("vessel-search-base-dn");
            String filter = replaceTokens(conf.getString("vessel-search-filter"), principal.toString());
            String vesselIdAttr = conf.hasPath("client-id-attr") ? conf.getString("client-id-attr") : "mmsi";
            Set<String> vesselIds = new HashSet<>();

            ctx = new InitialDirContext(createLdapEnv(ldapUrl, bindDn, credential));

            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(new String[] { vesselIdAttr });
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> result = ctx.search(baseDn, filter, ctls);
            while (result.hasMore()) {
                SearchResult entry = result.next();
                Attributes as = entry.getAttributes();

                NamingEnumeration<String> ids = as.getIDs();
                if (ids.hasMore()) {
                    String id = ids.next();
                    Attribute attr = as.get(id);
                    if (attr.size() > 0) {
                        vesselIds.add(attr.get(0).toString());
                    }
                }
            }

            return vesselIds;
        } finally {
            if (ctx != null) {
                try { ctx.close(); } catch (Exception ex) {}
            }
        }
    }

    /*************************************************/
    /** Utility methods                             **/
    /*************************************************/

    /**
     * Returns the LDAP Naming Context environment
     * @param url the LDAP url
     * @param bindDn the bind DN
     * @param credential the bind DN password
     * @return the LDAP Naming Context environment
     */
    protected Hashtable<Object, Object> createLdapEnv(String url, String bindDn, Object credential) {

        Hashtable<Object, Object> env = new Hashtable<>();
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        env.put(Context.SECURITY_PRINCIPAL, bindDn);
        env.put(Context.SECURITY_CREDENTIALS, credential);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        return env;
    }

    /**
     * Replaces tokens {0}, {1}, etc, with the parameters passed along
     * @param str the string to replace tokens in
     * @param params the parameters
     * @return the result
     */
    protected String replaceTokens(String str, Object... params) {
        try {
            return MessageFormat.format(str, params);
        } catch (IllegalArgumentException e) {
            return str;
        }
    }

}
