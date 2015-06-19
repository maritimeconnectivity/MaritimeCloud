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
import net.maritimecloud.mms.server.security.SslHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Default implementation of the SslSecurityConfHandler interface.
 *
 * <p>The implementation will look for the following attributes:</p>
 * <ul>
 *     <li>keystore: The path to a key-store that contains the SSL server certificate.</li>
 *     <li>keystore-password: The key-store password</li>
 *     <li>truststore: The path to a trust-store that contains the SSL client certificates or
 *                     the intermediate CA certificates used for issuing client certificates.</li>
 *     <li>truststore-password: The trust-store password</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class DefaultSslHandler implements SslHandler {

    private Config conf;

    /** {@inheritDoc} */
    @Override
    public SslContextFactory getSslContextFactory() {
        // SSL Context Factory for HTTPS
        SslContextFactory sslContextFactory = new SslContextFactory();
        if (conf.hasPath("keystore") && conf.hasPath("keystore-password")) {
            sslContextFactory.setKeyStorePath(conf.getString("keystore"));
            sslContextFactory.setKeyStorePassword(conf.getString("keystore-password"));
        }
        if (conf.hasPath("truststore") && conf.hasPath("truststore-password")) {
            sslContextFactory.setNeedClientAuth(true);
            sslContextFactory.setTrustStorePath(conf.getString("truststore"));
            sslContextFactory.setTrustStorePassword(conf.getString("truststore-password"));
        }
        return sslContextFactory;
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
}
