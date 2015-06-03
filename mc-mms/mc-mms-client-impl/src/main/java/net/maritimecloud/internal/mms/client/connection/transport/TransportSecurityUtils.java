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
package net.maritimecloud.internal.mms.client.connection.transport;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Objects;

/**
 * Utility methods for transport security handling.
 */
public class TransportSecurityUtils {

    /**
     * Loads the trust-store from the given path
     * @param trustStorePath the path to the trust-store
     * @param pwd the trust store password
     * @return the trust-store managers
     */
    public static TrustManager[] loadTrustStore(String trustStorePath, char[] pwd) throws Exception {
        Objects.requireNonNull(trustStorePath, "Trust-store path undefined");
        Objects.requireNonNull(pwd, "Trust-store password undefined");

        String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory instance = TrustManagerFactory.getInstance(defaultAlgorithm);
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (InputStream file = new FileInputStream(trustStorePath)) {
            trustStore.load(file, pwd);
        }

        instance.init(trustStore);
        return instance.getTrustManagers();
    }

    /**
     * Loads the key-store from the given path
     * @param keyStorePath the path to the key-store
     * @param pwd the key store password
     * @return the key-store managers
     */
    public static KeyManager[] loadKeyStore(String keyStorePath, char[] pwd) throws Exception {
        Objects.requireNonNull(keyStorePath, "Key-store path undefined");
        Objects.requireNonNull(pwd, "Key-store password undefined");

        String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory instance = KeyManagerFactory.getInstance(defaultAlgorithm);
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (InputStream file = new FileInputStream(keyStorePath)) {
            keyStore.load(file, pwd);
        }

        instance.init(keyStore, pwd);
        return instance.getKeyManagers();
    }


}
