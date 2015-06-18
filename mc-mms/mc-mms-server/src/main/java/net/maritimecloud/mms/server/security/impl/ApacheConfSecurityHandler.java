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
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class implements support for an Apache webserver-style configuration with
 * authentication against a htpasswd file and authorization by supporting auth group files.
 *
 * <h2>Authentication</h2>
 * The class implements the {@code AuthenticationHandler} interface and attempts
 * to authenticate the client from an Apache htpasswd-style credentials file.
 * <p/>
 * The authentication token must be of type {@code UsernamePasswordToken}.
 *
 * <p>The security configuration file must specify the "htpasswd-file" attribute, pointing
 * to an Apache htpasswd-style credentials file</p>
 *
 * <h2>Authorization</h2>
 * The class implements the {@code AuthorizationHandler} interface and will
 * assign the roles to the client specified by an Apache AuthGroupFile-style group file.
 *
 * <p>The security configuration file must specify the "auth-group-file" attribute, pointing
 * to an Apache htpasswd-style credentials file</p>
 */
@SuppressWarnings("unused")
public class ApacheConfSecurityHandler implements AuthenticationHandler, AuthorizationHandler {

    private final static Pattern HTPASSWD_ENTRY = Pattern.compile("^([^:]+):(.+)");
    private final static Pattern AUTH_GROUP_ENTRY = Pattern.compile("^([^:]+):(.+)");

    private Config conf;

    /** User + encrypted passwords loaded from Apache htpasswd-style file */
    private Map<String, String> userPasswords = new HashMap<>();

    /** Last modified timestamp of the htpasswd file */
    private long htpasswdFileLastModified = -1L;

    /** User + groups loaded from Apache AuthGroupFile-style file */
    private Map<String, Set<String>> userGroups = new HashMap<>();

    /** Last modified timestamp of the auth group file */
    private long authGroupFileLastModified = -1L;

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

        if (token == null || !(token instanceof UsernamePasswordToken)) {
            throw new AuthenticationException("Invalid authentication token: " + token);
        }
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;

        try {
            String htpasswdFile = getConf().getString("htpasswd-file");

            if (!authenticate(usernamePasswordToken, new File(htpasswdFile))) {
                throw new AuthenticationException("Invalid username-password");
            }
        } catch (IOException e) {
            throw new AuthenticationException("Error executing htpasswd authentication", e);
        }
    }

    /**
     * Check if the credentials are valid according to the Apache htpasswd-style credentials file
     *
     * @param token the credentials to check
     * @param htpasswdFile the htpasswd file
     * @return if the credentials are valid
     */
    protected boolean authenticate(UsernamePasswordToken token, File htpasswdFile) throws IOException {
        Objects.requireNonNull(token);
        Objects.requireNonNull(htpasswdFile);

        // Read in the htpasswd file
        checkReadHtpasswdFile(htpasswdFile);

        String storedPwd = userPasswords.get(token.getUsername());
        if (storedPwd != null) {
            final String passwd = new String(token.getPassword());

            // test Apache MD5 variant encrypted password
            if (storedPwd.startsWith("$apr1$")) {
                return storedPwd.equals(Md5Crypt.apr1Crypt(passwd, storedPwd));
            }

            // test unsalted SHA password
            else if (storedPwd.startsWith("{SHA}")) {
                String passwd64 = org.apache.commons.codec.binary.Base64.encodeBase64String(DigestUtils.sha1(passwd));
                return  storedPwd.substring("{SHA}".length()).equals(passwd64);
            }

            // test libc crypt() encoded password
            else if (storedPwd.equals(Crypt.crypt(passwd, storedPwd))) {
                return true;
            }

            // test clear text
            else if (storedPwd.equals(passwd)) {
                return true;
            }
        }

        // Not authenticated
        return false;
    }

    /**
     * Check if the htpasswd file has been updated since last time it was read.
     * Reads in all users + encrypted password.
     *
     * @param htpasswdFile the password file
     */
    protected synchronized void checkReadHtpasswdFile(File htpasswdFile) throws IOException {
        if (!htpasswdFile.exists()) {
            throw new IOException("File does not exist: " + htpasswdFile);
        }

        // Only read it, if the file has been updated ... and the first time called.
        if (htpasswdFile.lastModified() != htpasswdFileLastModified) {

            userPasswords.clear();
            try (Scanner scanner = new Scanner(new FileInputStream(htpasswdFile))) {
                while( scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if ( !line.isEmpty() &&  !line.startsWith("#") ) {
                        Matcher m = HTPASSWD_ENTRY.matcher(line);
                        if ( m.matches() ) {
                            userPasswords.put(m.group(1), m.group(2));
                        }
                    }
                }
            }

            // Record time stamp
            htpasswdFileLastModified = htpasswdFile.lastModified();
        }
    }

    /*************************************************/
    /** Authorization Support                      **/
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
            String authGroupFile = getConf().getString("auth-group-file");

            if (!checkRoles(principal, new File(authGroupFile), matchAll, roleIdentifiers)) {
                throw new AuthorizationException("Authorization error");
            }
        } catch (IOException e) {
            throw new AuthorizationException("Error checking user roles", e);
        }
    }

    /**
     * Checks if the users roles, as defined by the auth-group-file, matches the required roles
     * @param principal the user principal
     * @param file the auth-group-file
     * @param matchAll whether to match all or any
     * @param roleIdentifiers the roles to match
     * @return if the users roles matches the required roles
     */
    private boolean checkRoles(Object principal, File file, boolean matchAll, String... roleIdentifiers) throws IOException {
        // Make sure we have loaded the auth-group-file
        checkReadAuthGroupFile(file);

        Set<String> roles = userGroups.get(principal.toString());
        return matchRoles(roles, matchAll, false, roleIdentifiers);
    }


    /**
     * Check if the group file has been updated since last time it was read.
     * Reads in all users + groups.
     *
     * @param authGroupFile the group file
     */
    protected synchronized void checkReadAuthGroupFile(File authGroupFile) throws IOException {
        if (!authGroupFile.exists()) {
            throw new IOException("File does not exist: " + authGroupFile);
        }

        // Only read it, if the file has been updated ... and the first time called.
        if (authGroupFile.lastModified() != authGroupFileLastModified) {

            userGroups.clear();
            try (Scanner scanner = new Scanner(new FileInputStream(authGroupFile))) {
                while( scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if ( !line.isEmpty() &&  !line.startsWith("#") ) {
                        Matcher m = AUTH_GROUP_ENTRY.matcher(line);
                        if ( m.matches() ) {
                            String group = m.group(1);
                            Arrays.asList(m.group(2).trim().split("\\s+")).forEach(user -> {
                                Set<String> groups = userGroups.get(user);
                                if (groups == null) {
                                    groups = new HashSet<>();
                                    userGroups.put(user, groups);
                                }
                                groups.add(group);
                            });
                        }
                    }
                }
            }

            // Record time stamp
            authGroupFileLastModified = authGroupFile.lastModified();
        }
    }

}
