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
package net.maritimecloud.net.service.registration;

import net.maritimecloud.net.service.ServiceException;

/**
 * An error occurred while unregistering the service.
 * 
 * @author Kasper Nielsen
 */
public class ServiceUnregisterException extends ServiceException {

    /** The service that was unregistered was never registered. */
    public static final int SERVICE_NEVER_REGISTERED = 1;

    /** The error code. */
    private final int errorCode;

    // private final String service name???
    /**
     * Creates a new ServiceUnregisterException.
     * 
     * @param errorCode
     *            the error code
     */
    public ServiceUnregisterException(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code.
     * 
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }
}
