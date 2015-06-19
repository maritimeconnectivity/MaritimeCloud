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

/**
 * Interface to be implemented by client verification handler classes.
 */
public interface ClientVerificationHandler extends BaseSecurityHandler {

    String SECURITY_CONF_GROUP = "client-verification-conf";

    /**
     * Checks that the client ID, i.e. the client Maritime ID, is valid for the given principal
     * @param principal the principal or null if unauthenticated
     * @param id the client ID
     * @throws ClientVerificationException if the verification check fails
     */
    void verifyClient(Object principal, String id) throws ClientVerificationException;
}
