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
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.internal.net.client.connection.ConnectionMessageBus;
import net.maritimecloud.internal.net.messages.c2c.service.InvokeService;
import net.maritimecloud.net.service.invocation.InvocationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of InvocationCallback.Context.
 * 
 * @author Kasper Nielsen
 */
class DefaultLocalServiceInvocationContext2<T> implements InvocationCallback.Context<T> {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultLocalServiceInvocationContext2.class);

    private final MaritimeId id;

    T message;

    int errorCode;

    String errorMessage;

    boolean done;

    ConnectionMessageBus bus;

    final InvokeService is;

    DefaultLocalServiceInvocationContext2(InvokeService is, ConnectionMessageBus bus, MaritimeId id) {
        this.id = requireNonNull(id);
        this.is = is;
        this.bus = requireNonNull(bus);
    }

    /** {@inheritDoc} */
    @Override
    public void complete(T message) {
        checkNotDone();
        this.message = message;
        bus.sendConnectionMessage(is.createReply(message));
    }

    /** {@inheritDoc} */
    @Override
    public void failWithIllegalAccess(String errorMessage) {
        failWith(1, errorMessage);
    }

    /** {@inheritDoc} */
    @Override
    public void failWithIllegalInput(String errorMessage) {
        failWith(2, errorMessage);
    }

    /** {@inheritDoc} */
    @Override
    public void failWithInternalError(String errorMessage) {
        failWith(3, errorMessage);
    }

    private void failWith(int errorCode, String errorMessage) {
        checkNotDone();
        errorCode = 1;
        this.errorMessage = errorMessage == null ? "Unknown Error" : errorMessage;
    }

    private void checkNotDone() {
        if (done) {
            LOG.error("This context has already been used", new Exception());
        }
        done = true;
    }

    /** {@inheritDoc} */
    @Override
    public MaritimeId getCaller() {
        return id;
    }
}
