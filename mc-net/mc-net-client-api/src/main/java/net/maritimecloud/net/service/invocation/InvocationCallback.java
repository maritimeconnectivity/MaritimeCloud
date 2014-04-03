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
package net.maritimecloud.net.service.invocation;

import net.maritimecloud.core.id.MaritimeId;

/**
 *
 * @author Kasper Nielsen
 */
public interface InvocationCallback<E, T> {

    void process(E message, Context<T> context);

    interface Context<T> {

        /**
         * Returns the caller id.
         *
         * @return the id of the caller
         */
        MaritimeId getCaller();

        /**
         * Invoked if the operation completed successfully.
         *
         * @param replyMessage
         *            the reply message to the caller, if T is Void null is the only valid parameter to this method
         */
        void complete(T replyMessage);

        /**
         * The caller did not have sufficient permissions to invoke the specified service.
         *
         * @param message
         *            a helper message that will be provided to the client
         */
        void failWithIllegalAccess(String message);

        void failWithIllegalInput(String message);

        void failWithInternalError(String message);
    }
}
