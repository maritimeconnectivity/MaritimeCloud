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
package net.maritimecloud.mms.server.security;

import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Interface to be implemented by SSL security handler classes
 */
public interface SslHandler extends BaseSecurityHandler {

    String SECURITY_CONF_GROUP = "ssl-conf";

    /**
     * Returns a new SSL context factory based on the SSL configuration
     * @return a new SSL context factory based on the SSL configuration
     */
    SslContextFactory getSslContextFactory();

}
