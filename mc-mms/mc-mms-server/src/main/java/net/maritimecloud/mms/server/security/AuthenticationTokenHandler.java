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

import javax.servlet.http.HttpServletRequest;

/**
 * Interface to be implemented by authentication token handler classes
 */
public interface AuthenticationTokenHandler extends BaseSecurityHandler {

    String SECURITY_CONF_GROUP = "auth-token-conf";

    /**
     * Resolves an {@code AuthenticationToken} from the HTTP servlet request.
     * If none can be resolved, null is returned.
     * If the authentication token is invalid, an AuthenticationException is thrown
     *
     * @param request the websocket upgrade request
     * @return the authentication token, or null if none is resolved
     */
    AuthenticationToken resolveAuthenticationToken(HttpServletRequest request) throws AuthenticationException;
}
