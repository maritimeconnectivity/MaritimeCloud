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
package net.maritimecloud.net;

/**
 * The default environments that are hosted by the Maritime Cloud Foundation.
 *
 * @author Kasper Nielsen
 */
public abstract class Environment {

    /** The default domain. */
    private static final String DOMAIN = ".maritimecloud.net";

    /**
     * The default sandbox environment (encrypted connection). Even though the connection is encrypted other clients
     * connected to sandbox environment might not use encryption. So do not transmit any kind of confidential data in
     * the sandbox environment.
     */
    public static final Environment SANDBOX = new Environment() {

        /** {@inheritDoc} */
        @Override
        public String mmsServerURL() {
            return "wss://mms.sandbox03" + DOMAIN + ":443";
        }
    };

    /** The default sandbox environment (unencrypted connection). */
    public static final Environment SANDBOX_UNENCRYPTED = new Environment() {

        /** {@inheritDoc} */
        @Override
        public String mmsServerURL() {
            return "ws://mms.sandbox03" + DOMAIN + ":80";
        }
    };

    /** The default test environment (encrypted connection). */
    public static final Environment TEST = new Environment() {

        /** {@inheritDoc} */
        @Override
        public String mmsServerURL() {
            return "wss://mms.test03" + DOMAIN + ":443";
        }
    };

    // package private for now, we might allow others to construct an environment at some point.
    // Otherwise we should convert this class to an enum.
    Environment() {}

    /**
     * Returns whether or not the communication with the MMS server is encrypted.
     *
     * @return whether or not the communication with the MMS server is encrypted
     */
    public final boolean isEncrypted() {
        return mmsServerURL().startsWith("wss");
    }

    /**
     * Returns the url at which the Maritime Messaging Service is located.
     *
     * @return the url at which the Maritime Messaging Service is located
     */
    public abstract String mmsServerURL();
}
