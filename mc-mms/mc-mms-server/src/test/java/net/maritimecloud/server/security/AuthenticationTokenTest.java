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

import io.jsonwebtoken.ExpiredJwtException;
import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.impl.BasicAuthAuthenticationTokenHandler;
import net.maritimecloud.mms.server.security.impl.ClientCertAuthenticationTokenHandler.SubjectDnAuthenticationToken;
import net.maritimecloud.mms.server.security.impl.JwtAuthenticationTokenHandler;
import net.maritimecloud.mms.server.security.impl.JwtAuthenticationTokenHandler.JwtAuthenticationToken;
import net.maritimecloud.mms.server.security.impl.UsernamePasswordToken;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for the various authentication token handlers
 */
public class AuthenticationTokenTest {

    @Test
    public void testBasicAuthAuthenticationTokenHandler() throws Exception {

        // Create header at http://www.motobit.com/util/base64-decoder-encoder.asp
        String authHeader = "Basic bW1zdXNlcjpjaGFuZ2VpdA==";
        UsernamePasswordToken token = (UsernamePasswordToken) BasicAuthAuthenticationTokenHandler.resolveAuthenticationToken(authHeader);
        assertNotNull(token);
        assertEquals(token.getPrincipal(), "mmsuser");
    }


    @Test
    public void testCertificateSubjectDnTokens() {
        String subjectDn = "CN=mmsuser, O=Scandlines, C=DK";

        SubjectDnAuthenticationToken token = new SubjectDnAuthenticationToken(subjectDn, null);
        assertEquals(token.getPrincipal(), subjectDn);

        token = new SubjectDnAuthenticationToken(subjectDn, "cn");
        assertEquals(token.getPrincipal(), "mmsuser");
    }


    @Test
    public void testJwtTokenHandler() {
        // JWT Tokens generated at http://jwtbuilder.jamiekurtz.com
        String jwtSecret = "KimJongUn";
        String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJETUEiLCJpYXQiOjE0MzQ2MzU5NzQsImV4cCI6NDYyMTg0NTU3NCwiYXV"
                + "kIjoibWFyaXRpbWVjbG91ZC5uZXQiLCJzdWIiOiJtbXN1c2VyQG1hcml0aW1lY2xvdWQubmV0In0.UpFPi3VZCxhcTYNNS1g_Zby_ZG2fPLiy1DYIszSKxso";
        String expiredJwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJETUEiLCJpYXQiOjExMTkxMDMxNzQsImV4cCI6MTI3Njc4MzE3"
                + "NCwiYXVkIjoibWFyaXRpbWVjbG91ZC5uZXQiLCJzdWIiOiJtbXN1c2VyQG1hcml0aW1lY2xvdWQubmV0In0.zwsIZIoGpQWs3R5aJe9iBdN3zQws87efDqm4Iw0DOCA";

        try {
            JwtAuthenticationToken token = (JwtAuthenticationToken)JwtAuthenticationTokenHandler
                    .resolveAuthenticationToken(jwtSecret, "Bearer " + jwtToken);
            assertEquals(token.getPrincipal(), "mmsuser@maritimecloud.net");
        } catch (AuthenticationException e) {
            assertTrue("Failed validating JWT token", false);
        }

        try {
            JwtAuthenticationTokenHandler
                    .resolveAuthenticationToken(jwtSecret, "Bearer " + expiredJwtToken);
            assertTrue("Validated expired JWT token", false);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof ExpiredJwtException);
        }
    }
}
