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
package net.maritimecloud.internal.mms.messages.services;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.LocalEndpoint;

public final class Services extends LocalEndpoint {

    /** The name of the endpoint. */
    public static final String NAME = "Services";

    public Services(Invocator ei) {
        super(ei);
    }

    public EndpointInvocationFuture<List<String>> locate(String endpointName, Integer meters, Integer max) {
        Locate arguments = new Locate();
        arguments.setEndpointName(endpointName);
        arguments.setMeters(meters);
        arguments.setMax(max);
        return invokeRemote("Services.locate", arguments, Locate.SERIALIZER, ValueSerializer.TEXT.listOf());
    }

    public EndpointInvocationFuture<Void> registerEndpoint(String endpointName) {
        RegisterEndpoint arguments = new RegisterEndpoint();
        arguments.setEndpointName(endpointName);
        return invokeRemote("Services.registerEndpoint", arguments, RegisterEndpoint.SERIALIZER, null);
    }

    public EndpointInvocationFuture<Void> unregisterEndpoint(String endpointName) {
        UnregisterEndpoint arguments = new UnregisterEndpoint();
        arguments.setEndpointName(endpointName);
        return invokeRemote("Services.unregisterEndpoint", arguments, UnregisterEndpoint.SERIALIZER, null);
    }

    public EndpointInvocationFuture<Void> subscribe(List<String> name, net.maritimecloud.util.geometry.Area area) {
        Subscribe arguments = new Subscribe();
        arguments.addAllName(name);
        arguments.setArea(area);
        return invokeRemote("Services.subscribe", arguments, Subscribe.SERIALIZER, null);
    }

    static class Locate implements Message {

        /** The full name of this message. */
        public static final String NAME = "net.maritimecloud.internal.mms.messages.services.Locate";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<Locate> SERIALIZER = new LocateSerializer();

        /** Field definition. */
        private String endpointName;

        /** Field definition. */
        private Integer meters;

        /** Field definition. */
        private Integer max;

        /** Creates a new Locate. */
        public Locate() {}

        /**
         * Creates a new Locate by reading from a message reader.
         *
         * @param reader
         *            the message reader
         */
        Locate(MessageReader reader) throws IOException {
            this.endpointName = reader.readText(1, "endpointName", null);
            this.meters = reader.readInt(2, "meters", null);
            this.max = reader.readInt(3, "max", null);
        }

        void writeTo(MessageWriter w) throws IOException {
            w.writeText(1, "endpointName", endpointName);
            w.writeInt(2, "meters", meters);
            w.writeInt(3, "max", max);
        }

        public String getEndpointName() {
            return endpointName;
        }

        public boolean hasEndpointName() {
            return endpointName != null;
        }

        public Locate setEndpointName(String endpointName) {
            this.endpointName = endpointName;
            return this;
        }

        public Integer getMeters() {
            return meters;
        }

        public boolean hasMeters() {
            return meters != null;
        }

        public Locate setMeters(Integer meters) {
            this.meters = meters;
            return this;
        }

        public Integer getMax() {
            return max;
        }

        public boolean hasMax() {
            return max != null;
        }

        public Locate setMax(Integer max) {
            this.max = max;
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Locate immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of Locate. */
    static class LocateSerializer extends MessageSerializer<Locate> {

        /** {@inheritDoc} */
        @Override
        public Locate read(MessageReader reader) throws IOException {
            return new Locate(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Locate message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    static class RegisterEndpoint implements Message {

        /** The full name of this message. */
        public static final String NAME = "net.maritimecloud.internal.mms.messages.services.RegisterEndpoint";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<RegisterEndpoint> SERIALIZER = new RegisterEndpointSerializer();

        /** Field definition. */
        private String endpointName;

        /** Creates a new RegisterEndpoint. */
        public RegisterEndpoint() {}

        /**
         * Creates a new RegisterEndpoint by reading from a message reader.
         *
         * @param reader
         *            the message reader
         */
        RegisterEndpoint(MessageReader reader) throws IOException {
            this.endpointName = reader.readText(1, "endpointName", null);
        }

        void writeTo(MessageWriter w) throws IOException {
            w.writeText(1, "endpointName", endpointName);
        }

        public String getEndpointName() {
            return endpointName;
        }

        public boolean hasEndpointName() {
            return endpointName != null;
        }

        public RegisterEndpoint setEndpointName(String endpointName) {
            this.endpointName = endpointName;
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of RegisterEndpoint. */
    static class RegisterEndpointSerializer extends MessageSerializer<RegisterEndpoint> {

        /** {@inheritDoc} */
        @Override
        public RegisterEndpoint read(MessageReader reader) throws IOException {
            return new RegisterEndpoint(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(RegisterEndpoint message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    static class UnregisterEndpoint implements Message {

        /** The full name of this message. */
        public static final String NAME = "net.maritimecloud.internal.mms.messages.services.UnregisterEndpoint";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<UnregisterEndpoint> SERIALIZER = new UnregisterEndpointSerializer();

        /** Field definition. */
        private String endpointName;

        /** Creates a new UnregisterEndpoint. */
        public UnregisterEndpoint() {}

        /**
         * Creates a new UnregisterEndpoint by reading from a message reader.
         *
         * @param reader
         *            the message reader
         */
        UnregisterEndpoint(MessageReader reader) throws IOException {
            this.endpointName = reader.readText(1, "endpointName", null);
        }

        void writeTo(MessageWriter w) throws IOException {
            w.writeText(1, "endpointName", endpointName);
        }

        public String getEndpointName() {
            return endpointName;
        }

        public boolean hasEndpointName() {
            return endpointName != null;
        }

        public UnregisterEndpoint setEndpointName(String endpointName) {
            this.endpointName = endpointName;
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of UnregisterEndpoint. */
    static class UnregisterEndpointSerializer extends MessageSerializer<UnregisterEndpoint> {

        /** {@inheritDoc} */
        @Override
        public UnregisterEndpoint read(MessageReader reader) throws IOException {
            return new UnregisterEndpoint(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(UnregisterEndpoint message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    static class Subscribe implements Message {

        /** The full name of this message. */
        public static final String NAME = "net.maritimecloud.internal.mms.messages.services.Subscribe";

        /** A message serializer that can read and write instances of this class. */
        public static final MessageSerializer<Subscribe> SERIALIZER = new SubscribeSerializer();

        /** Field definition. */
        private final List<String> name;

        /** Field definition. */
        private net.maritimecloud.util.geometry.Area area;

        /** Creates a new Subscribe. */
        public Subscribe() {
            name = new java.util.ArrayList<>();
        }

        /**
         * Creates a new Subscribe by reading from a message reader.
         *
         * @param reader
         *            the message reader
         */
        Subscribe(MessageReader reader) throws IOException {
            this.name = MessageHelper.readList(1, "name", reader, ValueSerializer.TEXT);
            this.area = reader.readMessage(2, "area", net.maritimecloud.util.geometry.Area.SERIALIZER);
        }

        void writeTo(MessageWriter w) throws IOException {
            w.writeList(1, "name", name, ValueSerializer.TEXT);
            w.writeMessage(2, "area", area, net.maritimecloud.util.geometry.Area.SERIALIZER);
        }

        /** Returns the full name(s) of the broadcast types to listen for. */
        public List<String> getName() {
            return java.util.Collections.unmodifiableList(name);
        }

        public boolean hasName() {
            return name != null;
        }

        public Subscribe addName(String name) {
            java.util.Objects.requireNonNull(name, "name is null");
            this.name.add(name);
            return this;
        }

        public Subscribe addAllName(Collection<? extends String> name) {
            for (String e : name) {
                addName(e);
            }
            return this;
        }

        /** Returns the area to listen in, should only be used by stationary actors (Optional). */
        public net.maritimecloud.util.geometry.Area getArea() {
            return area;
        }

        public boolean hasArea() {
            return area != null;
        }

        public Subscribe setArea(net.maritimecloud.util.geometry.Area area) {
            this.area = area;
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Subscribe immutable() {
            throw new UnsupportedOperationException("method not supported");
        }

        /** Returns a JSON representation of this message */
        public String toJSON() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    /** A serializer for reading and writing instances of Subscribe. */
    static class SubscribeSerializer extends MessageSerializer<Subscribe> {

        /** {@inheritDoc} */
        @Override
        public Subscribe read(MessageReader reader) throws IOException {
            return new Subscribe(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(Subscribe message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }
}
