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

import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.service.invocation.ServiceUnavailableException;

/**
 * 
 * @author Kasper Nielsen
 */
public interface ServiceRegistration {

    /**
     * Returns the current state of this registration
     * 
     * @return
     */
    State getState();

    /**
     * Cancels the service registration. All services will automatically be shutdown when the user explicit disconnects.
     * Remote clients that tries to access the service will receive a {@link ServiceUnavailableException}.
     */
    void cancel();

    boolean awaitRegistered(long timeout, TimeUnit unit) throws InterruptedException;

    /** The current state of the registration. */
    enum State {

        /**
         * The initial state of this registration. When created at the client but before the server has registered the
         * service registration.
         */
        INITIATED,

        /** The service has been registered with server. And remote clients may now invoke the service */
        REGISTERED,

        /**
         * The client no longer offers the service. Remote clients attempting to invoke the service will fail with a
         * {@link ServiceUnavailableException}.
         */
        CANCELLED;
    }
}
