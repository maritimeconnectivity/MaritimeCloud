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

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Implementation of the {@code AuthenticationTokenHandler} interface that attempts
 * to extract the user-password authentication token the basic authentication header
 */
@SuppressWarnings("unused")
public class BasicAuthAuthenticationTokenHandler implements AuthenticationTokenHandler {

    private Config conf;

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

    /** {@inheritDoc} */
    @Override
    public AuthenticationToken resolveAuthenticationToken(ServletUpgradeRequest upgradeRequest) {
        return resolveAuthenticationToken(upgradeRequest.getHeader("Authorization"));
    }

    /**
     * Resolves an {@code AuthenticationToken} from the websocket upgrade request authorization header.
     * If none can be resolved, null is returned.
     *
     * @param authHeader the authorization header
     * @return the authentication token, or null if none is resolved
     */
    public static AuthenticationToken resolveAuthenticationToken(String authHeader) {

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Extract the user part from the header
            authHeader = authHeader.substring("Basic ".length());
            authHeader = new String(Base64.getDecoder().decode(authHeader), Charset.forName("UTF-8"));
            String name = authHeader.substring(0, authHeader.indexOf(":"));
            String password = authHeader.substring(name.length() + 1);
            return new UsernamePasswordToken(name, password.toCharArray());
        }

        // No principal resolved
        return null;
    }
}
