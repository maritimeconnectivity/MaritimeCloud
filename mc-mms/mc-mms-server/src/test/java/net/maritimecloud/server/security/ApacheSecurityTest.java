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
package net.maritimecloud.server.security;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.AuthorizationException;
import net.maritimecloud.mms.server.security.impl.ApacheConfSecurityHandler;
import net.maritimecloud.mms.server.security.impl.UsernamePasswordToken;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Test of the apache security functionality, i.e. htpasswd passwords and AuthGroupFile-style group files
 * <p/>
 * Hint:
 * Generate a htpasswd file using: htpasswd -c htpasswd-users mmsuser
 */
public class ApacheSecurityTest {

    public static final Path getResourcePath(String file) throws URISyntaxException {
        if (!file.startsWith("/")) {
            file = "/" + file;
        }

        return Paths.get(ApacheSecurityTest.class.getResource(file).toURI());
    }

    @Test
    public void testHtpasswdAuthentication() throws Exception {

        Config conf = ConfigFactory.parseString("htpasswd-file = " + getResourcePath("htpasswd-users"));
        ApacheConfSecurityHandler securityHandler = new ApacheConfSecurityHandler();
        securityHandler.init(conf);

        UsernamePasswordToken authToken = new UsernamePasswordToken();
        authToken.setUsername("mmsuser");
        authToken.setPassword("changeit".toCharArray());

        securityHandler.authenticate(authToken);

        authToken.setPassword("dontchangeit".toCharArray());
        try {
            securityHandler.authenticate(authToken);
            assertTrue("False positive authentication", false);
        } catch (Exception e) {
            assertTrue(e instanceof AuthenticationException);
        }
    }

    @Test
    public void testAuthGroupFileAuthorization() throws Exception {

        Config conf = ConfigFactory.parseString("auth-group-file = " + getResourcePath("groups"));
        ApacheConfSecurityHandler securityHandler = new ApacheConfSecurityHandler();
        securityHandler.init(conf);

        securityHandler.checkRoles("mmsuser", true, "mms users");

        try {
            securityHandler.checkRoles("mmsuser", true, "mms loosers");
            assertTrue("False positive authorization", false);
        } catch (Exception e) {
            assertTrue(e instanceof AuthorizationException);
        }
    }

}
