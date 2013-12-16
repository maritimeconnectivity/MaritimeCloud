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
package test.stubs;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.net.service.invocation.InvocationCallback;
import net.maritimecloud.net.service.spi.Service;
import net.maritimecloud.net.service.spi.ServiceInitiationPoint;
import net.maritimecloud.net.service.spi.ServiceMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class HelloService extends Service {

    public static InvocationCallback<HelloService.GetName, HelloService.Reply> create(final String reply) {
        return new InvocationCallback<HelloService.GetName, HelloService.Reply>() {
            public void process(GetName message, InvocationCallback.Context<Reply> context) {
                context.complete(new Reply(reply));
            }
        };
    }

    /** An initiation point */
    public static final ServiceInitiationPoint<GetName> GET_NAME = new ServiceInitiationPoint<>(GetName.class);

    public static class GetName extends ServiceMessage<Reply> {}

    public static class Reply extends ServiceMessage<Void> {

        /** The name of the ship. */
        private String name;

        public Reply() {}

        public Reply(String name) {
            this.name = requireNonNull(name);
        }

        /** @return the name of the service */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /** {@inheritDoc} */
        public String toString() {
            return name;
        }
    }
}
