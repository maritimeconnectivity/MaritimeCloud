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
package net.maritimecloud.internal.mms.transport;

/**
 * Interface that should be implemented by the main configuration classes to provide security-related settings.
 */
public interface SecurityConfiguration {

    /**
     * Returns the key-store path
     * @return the key-store
     */
    default String getKeystore() {
        return null;
    }

    /**
     * Returns the key-store password
     * @return the key-store password
     */
    default String getKeystorePassword() {
        return null;
    }

    /**
     * Returns whether or not to configure a key-store
     * @return whether or not to configure a key-store
     */
    default boolean useKeystore() {
        return getKeystore() != null && getKeystorePassword() != null;
    }

    /**
     * Returns the trust-store path
     * @return the trust-store
     */
    default String getTruststore() {
        return null;
    }

    /**
     * Returns the trust-store password
     * @return the trust-store password
     */
    default String getTruststorePassword() {
        return null;
    }

    /**
     * Returns whether or not to configure a trust-store
     * @return whether or not to configure a trust-store
     */
    default boolean useTruststore() {
        return getTruststore() != null && getTruststorePassword() != null;
    }

}
