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
package net.maritimecloud.internal.net.messages;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.maritimecloud.internal.message.Hashing;
import net.maritimecloud.internal.message.MessageHelper;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;

public class BroadcastTarget implements Message {

    /** The full name of this message. */
    public static final String NAME = "net.maritimecloud.internal.net.messages.BroadcastTarget";

    /** A message serializer that can read and write instances of this class. */
    public static final MessageSerializer<BroadcastTarget> SERIALIZER = new Serializer();

    /** Field definition. */
    private net.maritimecloud.util.geometry.Area area;

    /** Field definition. */
    private Integer radius;

    /** Field definition. */
    private final List<String> maritimeIds;

    /** Creates a new BroadcastTarget. */
    public BroadcastTarget() {
        maritimeIds = new java.util.ArrayList<>();
    }

    /**
     * Creates a new BroadcastTarget by reading from a message reader.
     *
     * @param reader
     *            the message reader
     */
    BroadcastTarget(MessageReader reader) throws IOException {
        this.area = reader.readMessage(1, "area", net.maritimecloud.util.geometry.Area.SERIALIZER);
        this.radius = reader.readInt(2, "radius", null);
        this.maritimeIds = MessageHelper.readList(3, "maritimeIds", reader, ValueSerializer.TEXT);
    }

    /**
     * Creates a new BroadcastTarget by copying an existing.
     *
     * @param instance
     *            the instance to copy all fields from
     */
    BroadcastTarget(BroadcastTarget instance) {
        this.area = MessageHelper.immutable(instance.area);
        this.radius = instance.radius;
        this.maritimeIds = MessageHelper.immutableCopy(instance.maritimeIds);
    }

    void writeTo(MessageWriter w) throws IOException {
        w.writeMessage(1, "area", area, net.maritimecloud.util.geometry.Area.SERIALIZER);
        w.writeInt(2, "radius", radius);
        w.writeList(3, "maritimeIds", maritimeIds, ValueSerializer.TEXT);
    }

    /** Returns the area to deliver the broadcast in. (Optional) */
    public net.maritimecloud.util.geometry.Area getArea() {
        return area;
    }

    public boolean hasArea() {
        return area != null;
    }

    public BroadcastTarget setArea(net.maritimecloud.util.geometry.Area area) {
        this.area = area;
        return this;
    }

    /** Returns the radius to deliver the broadcast in. (Optional) */
    public Integer getRadius() {
        return radius;
    }

    public boolean hasRadius() {
        return radius != null;
    }

    public BroadcastTarget setRadius(Integer radius) {
        this.radius = radius;
        return this;
    }

    public List<String> getMaritimeIds() {
        return java.util.Collections.unmodifiableList(maritimeIds);
    }

    public boolean hasMaritimeIds() {
        return maritimeIds != null;
    }

    public BroadcastTarget addMaritimeIds(String maritimeIds) {
        java.util.Objects.requireNonNull(maritimeIds, "maritimeIds is null");
        this.maritimeIds.add(maritimeIds);
        return this;
    }

    public BroadcastTarget addAllMaritimeIds(Collection<? extends String> maritimeIds) {
        for (String e : maritimeIds) {
            addMaritimeIds(e);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public BroadcastTarget immutable() {
        return new Immutable(this);
    }

    /** Returns a JSON representation of this message */
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, SERIALIZER);
    }

    /**
     * Creates a message of this type from a JSON throwing a runtime exception if the format of the message does not match
     */
    public static BroadcastTarget fromJSON(CharSequence c) {
        return MessageSerializer.readFromJSON(SERIALIZER, c);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = 31 + Hashing.hashcode(this.area);
        result = 31 * result + Hashing.hashcode(this.radius);
        return 31 * result + Hashing.hashcode(this.maritimeIds);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof BroadcastTarget) {
            BroadcastTarget o = (BroadcastTarget) other;
            return Objects.equals(area, o.area) &&
                   Objects.equals(radius, o.radius) &&
                   Objects.equals(maritimeIds, o.maritimeIds);
        }
        return false;
    }

    /** A serializer for reading and writing instances of BroadcastTarget. */
    static class Serializer extends MessageSerializer<BroadcastTarget> {

        /** {@inheritDoc} */
        @Override
        public BroadcastTarget read(MessageReader reader) throws IOException {
            return new BroadcastTarget(reader);
        }

        /** {@inheritDoc} */
        @Override
        public void write(BroadcastTarget message, MessageWriter writer) throws IOException {
            message.writeTo(writer);
        }
    }

    /** An immutable version of BroadcastTarget. */
    static class Immutable extends BroadcastTarget {

        /**
         * Creates a new Immutable instance.
         *
         * @param instance
         *            the instance to make an immutable copy of
         */
        Immutable(BroadcastTarget instance) {
            super(instance);
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastTarget immutable() {
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastTarget setArea(net.maritimecloud.util.geometry.Area area) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastTarget setRadius(Integer radius) {
            throw new UnsupportedOperationException("Instance is immutable");
        }

        /** {@inheritDoc} */
        @Override
        public BroadcastTarget addMaritimeIds(String maritimeIds) {
            throw new UnsupportedOperationException("Instance is immutable");
        }
    }
}
