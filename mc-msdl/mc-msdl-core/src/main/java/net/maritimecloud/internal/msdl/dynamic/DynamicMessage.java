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
package net.maritimecloud.internal.msdl.dynamic;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageEnum;
import net.maritimecloud.message.MessageEnumSerializer;
import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueReader;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.message.ValueWriter;
import net.maritimecloud.msdl.model.BaseMessage;
import net.maritimecloud.msdl.model.EnumDeclaration;
import net.maritimecloud.msdl.model.FieldOrParameter;
import net.maritimecloud.msdl.model.ListOrSetType;
import net.maritimecloud.msdl.model.MessageDeclaration;
import net.maritimecloud.msdl.model.Type;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

import com.google.common.base.Objects;

/**
 *
 * @author Kasper Nielsen
 */
public class DynamicMessage implements Message {

    final LinkedHashMap<String, FieldOrParameter> fields = new LinkedHashMap<>();

    final String fullName;

    final HashMap<FieldOrParameter, Object> values = new HashMap<>();

    public DynamicMessage(String fullName, Collection<FieldOrParameter> col) {
        this.fullName = fullName;
        for (FieldOrParameter p : col) {
            fields.put(p.getName(), p);
        }
    }

    public DynamicMessage(BaseMessage md) {
        this(md.getFullName(), md.getFields());
    }

    /** {@inheritDoc} */
    @Override
    public Message immutable() {
        return this;
    }

    public String name() {
        return fullName; // used by MessageHelper
    }

    void readFrom(MessageReader reader) throws IOException {
        for (FieldOrParameter p : fields.values()) {
            if (reader.isNext(p.getTag(), p.getName())) {
                values.put(p, readField(p, reader));
            }
        }
    }

    public MessageSerializer<DynamicMessage> serializer() {
        return Serializer.WRITEABLE; // used by MessageHelper
    }

    public void setField(String name, Object value) {
        FieldOrParameter f = fields.get(name);
        if (f == null) {
            throw new IllegalArgumentException("Unknown field '" + name + "'");
        }
        switch (f.getType().getBaseType()) {
        case BINARY:

        default:
            break;

        }
    }

    /** {@inheritDoc} */
    @Override
    public String toJSON() {
        return MessageSerializer.writeToJSON(this, serializer());
    }

    public void writeTo(ValueWriter writer) throws IOException {
        writer.writeMessage(this, Serializer.WRITEABLE);
    }

    static Object readField(FieldOrParameter p, MessageReader reader) throws IOException {
        switch (p.getType().getBaseType()) {
        case BINARY:
            return reader.readBinary(p.getTag(), p.getName(), (Binary) p.getDefaultValue());
        case BOOLEAN:
            return reader.readBoolean(p.getTag(), p.getName(), (Boolean) p.getDefaultValue());
        case DECIMAL:
            return reader.readDecimal(p.getTag(), p.getName(), (BigDecimal) p.getDefaultValue());
        case DOUBLE:
            return reader.readDouble(p.getTag(), p.getName(), (Double) p.getDefaultValue());
        case ENUM:
            MessageEnumSerializer<DynamicEnum> s = new DynamicEnum.Serializer((EnumDeclaration) p.getType());
            return reader.readEnum(p.getTag(), p.getName(), s);
        case FLOAT:
            return reader.readFloat(p.getTag(), p.getName(), (Float) p.getDefaultValue());
        case INT:
            return reader.readInt(p.getTag(), p.getName(), (Integer) p.getDefaultValue());
        case INT64:
            return reader.readDouble(p.getTag(), p.getName(), (Double) p.getDefaultValue());
        case LIST:
            return reader.readList(p.getTag(), p.getName(),
                    valueSerializer(((ListOrSetType) p.getType()).getElementType()));
        case MAP:
            throw new UnsupportedOperationException();
        case MESSAGE:
            return reader.readMessage(p.getTag(), p.getName(), new Serializer((MessageDeclaration) p.getType()));
        case POSITION:
            return reader.readPosition(p.getTag(), p.getName(), (Position) p.getDefaultValue());
        case POSITION_TIME:
            return reader.readPositionTime(p.getTag(), p.getName(), (PositionTime) p.getDefaultValue());
        case SET:
            return reader.readSet(p.getTag(), p.getName(),
                    valueSerializer(((ListOrSetType) p.getType()).getElementType()));
        case TEXT:
            return reader.readText(p.getTag(), p.getName(), (String) p.getDefaultValue());
        case TIMESTAMP:
            return reader.readTimestamp(p.getTag(), p.getName(), (Timestamp) p.getDefaultValue());
        case VARINT:
            return reader.readVarInt(p.getTag(), p.getName(), (BigInteger) p.getDefaultValue());
        }
        throw new Error();
    }

    public static DynamicMessage readFrom(String fullName, Collection<FieldOrParameter> col, MessageReader reader)
            throws IOException {
        DynamicMessage m = new DynamicMessage(fullName, col);
        m.readFrom(reader);
        return m;
    }


    public static DynamicMessage readFrom(MessageDeclaration definition, MessageReader reader) throws IOException {
        DynamicMessage m = new DynamicMessage(definition);
        m.readFrom(reader);
        return m;
    }

    public static ValueSerializer<?> valueSerializer(Type type) {
        if (type == null) {
            return null; // void method
        }
        switch (type.getBaseType()) {
        case BINARY:
            return ValueSerializer.BINARY;
        case BOOLEAN:
            return ValueSerializer.BOOLEAN;
        case DECIMAL:
            return ValueSerializer.DECIMAL;
        case DOUBLE:
            return ValueSerializer.DOUBLE;
        case ENUM:
            throw new UnsupportedOperationException();
        case FLOAT:
            return ValueSerializer.FLOAT;
        case INT:
            return ValueSerializer.INT;
        case INT64:
            return ValueSerializer.INT64;
        case LIST:
            ListOrSetType listType = (ListOrSetType) type;
            return valueSerializer(listType.getElementType()).listOf();
        case MAP:
            throw new UnsupportedOperationException();
        case MESSAGE:
            return new ValueSerializer<DynamicMessage>() {
                @Override
                public DynamicMessage read(ValueReader reader) throws IOException {
                    BaseMessage bm = (BaseMessage) type;
                    return reader.readMessage(new Serializer(bm));
                }

                @Override
                public void write(DynamicMessage t, ValueWriter writer) throws IOException {
                    writer.writeMessage(t, DynamicMessage.Serializer.WRITEABLE);
                }

                @Override
                public void write(int tag, String name, DynamicMessage t, MessageWriter writer) throws IOException {
                    writer.writeMessage(tag, name, t, DynamicMessage.Serializer.WRITEABLE);
                }
            };
        case POSITION:
            return ValueSerializer.POSITION;
        case POSITION_TIME:
            return ValueSerializer.POSITION_TIME;
        case SET:
            ListOrSetType setType = (ListOrSetType) type;
            return valueSerializer(setType.getElementType()).listOf();
        case TEXT:
            return ValueSerializer.TEXT;
        case TIMESTAMP:
            return ValueSerializer.TIMESTAMP;
        case VARINT:
            return ValueSerializer.VARINT;
        }
        throw new Error();
    }

    static class Serializer extends MessageSerializer<DynamicMessage> {

        final static Serializer WRITEABLE = new Serializer();

        final String fullName;

        final Collection<FieldOrParameter> col;

        private Serializer() {
            this.fullName = null;
            this.col = null;
        }

        Serializer(String fullName, Collection<FieldOrParameter> col) {
            this.fullName = fullName;
            this.col = col;
        }

        /**
         * @param m
         */
        Serializer(BaseMessage m) {
            this(m.getFullName(), m.getFields());
        }

        /** {@inheritDoc} */
        @Override
        public DynamicMessage read(MessageReader reader) throws IOException {
            DynamicMessage dm = new DynamicMessage(fullName, col);
            dm.readFrom(reader);
            return dm;
        }

        /** {@inheritDoc} */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void write(DynamicMessage message, MessageWriter mw) throws IOException {
            for (Map.Entry<FieldOrParameter, Object> e : message.values.entrySet()) {
                FieldOrParameter f = e.getKey();
                // Don't write default values
                if (!Objects.equal(f.getDefaultValue(), e.getValue())) {
                    switch (f.getType().getBaseType()) {
                    case BINARY:
                        mw.writeBinary(f.getTag(), f.getName(), (Binary) e.getValue());
                        break;
                    case BOOLEAN:
                        mw.writeBoolean(f.getTag(), f.getName(), (Boolean) e.getValue());
                        break;
                    case DECIMAL:
                        mw.writeDecimal(f.getTag(), f.getName(), (BigDecimal) e.getValue());
                        break;
                    case DOUBLE:
                        mw.writeDouble(f.getTag(), f.getName(), (Double) e.getValue());
                        break;
                    case ENUM:
                        mw.writeEnum(f.getTag(), f.getName(), (MessageEnum) e.getValue());
                        break;
                    case FLOAT:
                        mw.writeFloat(f.getTag(), f.getName(), (Float) e.getValue());
                        break;
                    case INT:
                        mw.writeInt(f.getTag(), f.getName(), (Integer) e.getValue());
                        break;
                    case INT64:
                        mw.writeInt64(f.getTag(), f.getName(), (Long) e.getValue());
                        break;
                    case LIST:
                        ListOrSetType type = (ListOrSetType) f.getType();
                        ValueSerializer<?> s = valueSerializer(type.getElementType());
                        mw.writeList(f.getTag(), f.getName(), (List) e.getValue(), s);
                        break;
                    case MAP:
                        throw new UnsupportedOperationException();
                    case MESSAGE:
                        mw.writeMessage(f.getTag(), f.getName(), (DynamicMessage) e.getValue(), WRITEABLE);
                        break;
                    case POSITION:
                        mw.writePosition(f.getTag(), f.getName(), (Position) e.getValue());
                        break;
                    case POSITION_TIME:
                        mw.writePositionTime(f.getTag(), f.getName(), (PositionTime) e.getValue());
                        break;
                    case SET:
                        type = (ListOrSetType) f.getType();
                        s = valueSerializer(type.getElementType());
                        mw.writeSet(f.getTag(), f.getName(), (Set) e.getValue(), s);
                        break;
                    case TEXT:
                        mw.writeText(f.getTag(), f.getName(), (String) e.getValue());
                        break;
                    case TIMESTAMP:
                        mw.writeTimestamp(f.getTag(), f.getName(), (Timestamp) e.getValue());
                        break;
                    case VARINT:
                        mw.writeVarInt(f.getTag(), f.getName(), (BigInteger) e.getValue());
                        break;
                    }
                }
            }
        }
    }
}
