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
package net.maritimecloud.internal.mms.client.broadcast;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import net.maritimecloud.net.BroadcastMessage;
import net.maritimecloud.net.DispatchedMessage;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.WithBroadcast;
import net.maritimecloud.util.geometry.Area;

/**
 *
 * @author Kasper Nielsen
 */
class DefaultWithBroadcast implements WithBroadcast {

    Area area;

    Consumer<? super MessageHeader> consumer;

    ClientBroadcastManager manager;

    BroadcastMessage message;

    int radius = 50000;

    DefaultWithBroadcast(ClientBroadcastManager manager, BroadcastMessage message) {
        this.manager = requireNonNull(manager);
        this.message = requireNonNull(message);
    }

    /** {@inheritDoc} */
    @Override
    public WithBroadcast onRemoteReceive(Consumer<? super MessageHeader> consumer) {
        this.consumer = requireNonNull(consumer);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public DispatchedMessage send() {
        return manager.brodcast(message, area, radius, consumer);
    }

    /** {@inheritDoc} */
    @Override
    public WithBroadcast toArea(Area area) {
        this.area = area;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public WithBroadcast toArea(int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("radius must be non-negative, was " + radius);
        }
        area = null;
        this.radius = radius;
        return this;
    }
}
