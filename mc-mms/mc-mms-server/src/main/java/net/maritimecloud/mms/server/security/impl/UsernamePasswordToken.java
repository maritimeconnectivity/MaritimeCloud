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

import net.maritimecloud.mms.server.security.AuthenticationToken;

/**
 * <p>A simple username/password authentication token to support the most widely-used authentication mechanism.</p>
 */
public class UsernamePasswordToken implements AuthenticationToken {

    /** The username */
    private String username;

    /** The password, in char[] format */
    private char[] password;

    /**
     * Constructor
     */
    public UsernamePasswordToken() {
    }

    /**
     * Constructor
     * @param username the user name
     * @param password the password
     */
    public UsernamePasswordToken(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    /** {@inheritDoc} */
    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    /** {@inheritDoc} */
    @Override
    public Object getCredentials() {
        return getPassword();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
}
