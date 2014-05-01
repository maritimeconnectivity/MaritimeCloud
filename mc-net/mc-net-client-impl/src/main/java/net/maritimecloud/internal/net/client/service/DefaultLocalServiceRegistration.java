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
package net.maritimecloud.internal.net.client.service;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.messages.ServiceInvoke;
import net.maritimecloud.net.service.invocation.InvocationCallback;
import net.maritimecloud.net.service.registration.ServiceRegistration;
import net.maritimecloud.net.service.spi.ServiceInitiationPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Kasper Nielsen
 */
public class DefaultLocalServiceRegistration implements ServiceRegistration {

    final InvocationCallback<Object, Object> c;

    final CountDownLatch replied = new CountDownLatch(1);

    final ServiceInitiationPoint<?> sip;

    final ConnectionMessageBus bus;

    @SuppressWarnings("unchecked")
    DefaultLocalServiceRegistration(ConnectionMessageBus bus, ServiceInitiationPoint<?> sip, InvocationCallback<?, ?> c) {
        this.sip = requireNonNull(sip);
        this.c = requireNonNull((InvocationCallback<Object, Object>) c);
        this.bus = requireNonNull(bus);
    }

    void invoke(ServiceInvoke message) {
        Object o = null;
        try {

            Class<?> mt = Class.forName(message.getServiceType());
            ObjectMapper om = new ObjectMapper();
            o = om.readValue(message.getMsg(), mt);
        } catch (Exception e) {
            // LOG error
            // Send invalid message
            e.printStackTrace();
        }

        // OLD code
        // DefaultLocalServiceInvocationContext<Object> context = new DefaultLocalServiceInvocationContext<>(
        // message.getSourceId());
        // c.process(o, context);
        //
        // if (context.done) {
        // if (context.errorCode == 0) {
        // bus.sendConnectionMessage(message.createReply(context.message));
        // } else {
        // // send fail message
        // }
        // } else {
        // // Log error
        // // send failed internal error message
        // }

        DefaultLocalServiceInvocationContext2<Object> context2 = new DefaultLocalServiceInvocationContext2<>(message,
                bus, MaritimeId.create(message.getSource()));
        c.process(o, context2);
    }

    /** {@inheritDoc} */
    @Override
    public boolean awaitRegistered(long timeout, TimeUnit unit) throws InterruptedException {
        return replied.await(timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    public void cancel() {
        throw new UnsupportedOperationException();
    }

    void completed() {

    }

    /** {@inheritDoc} */
    @Override
    public State getState() {
        throw new UnsupportedOperationException();
    }
}
