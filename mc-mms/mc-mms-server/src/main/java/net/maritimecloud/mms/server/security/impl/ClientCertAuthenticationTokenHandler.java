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
import net.maritimecloud.mms.server.security.AuthenticationToken;
import net.maritimecloud.mms.server.security.AuthenticationTokenHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;

import javax.naming.ldap.LdapName;
import java.security.cert.X509Certificate;

/**
 * Implementation of the {@code AuthenticationTokenHandler} interface that attempts
 * to extract the subject-dn principal of the X.509 client certificate
 * <p/>
 * If the "subject-dn-header" configuration option is set, then it is assumed that a proxy SSL-server
 * (e.g. nginx) has already validated the client certificate and stamped the subject-DN in the given request header.
 * <p/>
 * If the "principal-rdn-attr" configuration option is set, then the given RDN attribute (e.g. "CN") will be extracted
 * from the certificate and used as the principal.
 */
@SuppressWarnings("unused")
public class ClientCertAuthenticationTokenHandler implements AuthenticationTokenHandler {

    private Config conf;

    /** {@inheritDoc} */
    @Override
    public AuthenticationToken resolveAuthenticationToken(ServletUpgradeRequest upgradeRequest) {

        String rdnAttr = conf.hasPath("principal-rdn-attr") ? conf.getString("principal-rdn-attr") : null;

        if (conf.hasPath("subject-dn-header")) {

            // Check if the client certificate has already been validated by an SSL proxy (e.g. nginx)
            // and the subject-dn stamped into a request header
            String subjectDnHeader = upgradeRequest.getHeader(conf.getString("subject-dn-header"));
            if (subjectDnHeader != null && subjectDnHeader.trim().length() > 0) {
                return new SubjectDnAuthenticationToken(subjectDnHeader, rdnAttr);
            }

        } else {

            // Returns the subject-dn principal of the X.509 client certificate
            X509Certificate[] certs = upgradeRequest.getCertificates();
            if (certs != null && certs.length > 0) {
                return new X509CertificateAuthenticationToken(certs[0], rdnAttr);
            }
        }

        // No principal resolved
        return null;
    }

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

    /**
     * Base class for the certificate authentication tokens
     */
    public abstract static class BaseCertificateAuthenticationToken implements AuthenticationToken {

        String rdnAttr;

        /** No-arg constructor */
        public BaseCertificateAuthenticationToken() {
        }

        /**
         * Constructor
         * @param rdnAttr the RDN attribute name
         */
        public BaseCertificateAuthenticationToken(String rdnAttr) {
            this.rdnAttr = rdnAttr;
        }

        /**
         * Extracts the configured RDN attribute from the given DN.
         * If {@code rndAttr} is undefined or the attribute is not
         * part of the DN, then the DN is returned.
         *
         * @param dn the principal DN
         * @return the extracted RDN attribute value
         */
        protected String getPrincipal(String dn) {
            if (rdnAttr == null) {
                return dn;
            }
            try {
                return new LdapName(dn).getRdns().stream()
                        .filter(rdn -> rdn.getType().equalsIgnoreCase(rdnAttr))
                        .map(rdn -> rdn.getValue().toString())
                        .findFirst()
                        .orElse(dn);
            } catch (Exception e) {
                // Fall back to returning the DN
                return dn;
            }
        }

        public String getRdnAttr() {
            return rdnAttr;
        }

        public void setRdnAttr(String rdnAttr) {
            this.rdnAttr = rdnAttr;
        }
    }

    /**
     * An X.509 certificate authentication token
     */
    public static class X509CertificateAuthenticationToken extends BaseCertificateAuthenticationToken {

        private X509Certificate cert;

        /** No-arg constructor */
        public X509CertificateAuthenticationToken() {
        }

        /**
         * Constructor
         * @param cert the certificate
         */
        public X509CertificateAuthenticationToken(X509Certificate cert, String rdnAttr) {
            super(rdnAttr);
            this.cert = cert;
        }

        /** {@inheritDoc} */
        @Override
        public Object getPrincipal() {
            return getPrincipal(cert.getSubjectDN().getName());
        }

        /** {@inheritDoc} */
        @Override
        public Object getCredentials() {
            return null;
        }

        public X509Certificate getCert() {
            return cert;
        }

        public void setCert(X509Certificate cert) {
            this.cert = cert;
        }
    }

    /**
     * A certificate subject DN authentication token.
     * <p/>
     * Used when a front-end SSL-proxy has handled the client certificate validation and
     * stamped the subject DN into a request header.
     */
    public static class SubjectDnAuthenticationToken extends BaseCertificateAuthenticationToken {

        private String subjectDn;

        /** No-arg constructor */
        public SubjectDnAuthenticationToken() {
        }

        /**
         * Constructor
         * @param subjectDn the certificate subject DN
         */
        public SubjectDnAuthenticationToken(String subjectDn, String rdnAttr) {
            super(rdnAttr);
            this.subjectDn = subjectDn;
        }

        /** {@inheritDoc} */
        @Override
        public Object getPrincipal() {
            return getPrincipal(subjectDn);
        }

        /** {@inheritDoc} */
        @Override
        public Object getCredentials() {
            return null;
        }

        public String getSubjectDn() {
            return subjectDn;
        }

        public void setSubjectDn(String subjectDn) {
            this.subjectDn = subjectDn;
        }
    }
}
