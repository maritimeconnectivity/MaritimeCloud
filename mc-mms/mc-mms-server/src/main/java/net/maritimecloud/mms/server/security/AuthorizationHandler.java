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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Interface to be implemented by authorization security handler classes
 */
public interface AuthorizationHandler extends BaseSecurityHandler {

    String SECURITY_CONF_GROUP = "authorization-conf";

    /**
     * Check if the client has the given authorization
     * @param principal the principal or null if unauthenticated
     * @param matchAll whether to match any or all of the roles
     * @param roleIdentifiers the roles to check
     */
    void checkRoles(Object principal, boolean matchAll, String... roleIdentifiers) throws AuthorizationException;

    /**
     * Returns if the list of roles to check are either fully or partly part of the list of user roles
     * @param userRoles the list of user roles
     * @param matchAll whether the roles to check should be fully or partly part of the list of user roles
     * @param caseSensitive whether the role check is case sensitive or not
     * @param checkRoles the roles to check
     * @return the the list of roles to check are either fully or partly part of the list of user roles
     */
    default boolean matchRoles(Set<String> userRoles, boolean matchAll, boolean caseSensitive, String... checkRoles) {

        // Sanity checks
        if (checkRoles == null || checkRoles.length == 0) {
            return true;
        } else if (userRoles == null || userRoles.size() == 0) {
            return false;
        }

        if (!caseSensitive) {
            userRoles = userRoles.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        }
        int cnt = (int) Arrays.stream(checkRoles)
                .map(r -> caseSensitive ? r : r.toLowerCase())
                .filter(userRoles::contains)
                .count();
        return (matchAll && cnt == checkRoles.length) || (!matchAll && cnt > 0);
    }

}
