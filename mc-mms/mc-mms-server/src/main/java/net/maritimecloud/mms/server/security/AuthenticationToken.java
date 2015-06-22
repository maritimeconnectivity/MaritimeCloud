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

import net.maritimecloud.mms.server.security.impl.UsernamePasswordToken;

import java.io.Serializable;

/**
 * <p>The {@code AuthenticationToken} is - up to a point - modelled on the
 * <a href="http://shiro.apache.org/static/latest/apidocs/org/apache/shiro/authc/AuthenticationToken.html">
 *     Apache Shiro AuthenticationToken</a> concept.
 * </p>
 * <p>An <code>AuthenticationToken</code> the principal and credentials submitted to authenticate a client.<p/>
 */
public interface AuthenticationToken extends Serializable {

    /**
     * The user principal, e.g. username, password or LDAP DN
     *
     * @return the user principal.
     */
    Object getPrincipal();

    /**
     * Returns the credentials associated with the principal
     *
     * @return the credential.
     */
    Object getCredentials();

}
