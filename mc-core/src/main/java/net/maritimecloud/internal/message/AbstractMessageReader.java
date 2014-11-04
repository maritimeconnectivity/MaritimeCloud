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
package net.maritimecloud.internal.message;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.maritimecloud.message.MessageReader;
import net.maritimecloud.message.ValueReader;
import net.maritimecloud.message.ValueSerializer;
import net.maritimecloud.util.Binary;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionTime;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class AbstractMessageReader implements MessageReader {

    protected abstract ValueReader find(int tag, String name) throws IOException;

    protected abstract ValueReader findOptional(int tag, String name) throws IOException;

    /** {@inheritDoc} */
    @Override
    public Binary readBinary(int tag, String name, Binary defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readBinary();
    }

    /** {@inheritDoc} */
    @Override
    public Boolean readBoolean(int tag, String name, Boolean defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readBoolean();
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal(int tag, String name) throws IOException {
        return find(tag, name).readDecimal();
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal readDecimal(int tag, String name, BigDecimal defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readDecimal();
    }

    /** {@inheritDoc} */
    @Override
    public double readDouble(int tag, String name) throws IOException {
        return find(tag, name).readDouble();
    }

    /** {@inheritDoc} */
    @Override
    public Double readDouble(int tag, String name, Double defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readDouble();
    }

    /** {@inheritDoc} */
    @Override
    public float readFloat(int tag, String name) throws IOException {
        return find(tag, name).readFloat();
    }

    /** {@inheritDoc} */
    @Override
    public Float readFloat(int tag, String name, Float defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readFloat();
    }

    /** {@inheritDoc} */
    @Override
    public int readInt(int tag, String name) throws IOException {
        return find(tag, name).readInt();
    }

    /** {@inheritDoc} */
    @Override
    public Integer readInt(int tag, String name, Integer defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readInt();
    }

    /** {@inheritDoc} */
    @Override
    public long readInt64(int tag, String name) throws IOException {
        return find(tag, name).readInt64();
    }

    /** {@inheritDoc} */
    @Override
    public Long readInt64(int tag, String name, Long defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readInt64();
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> List<T> readList(int tag, String name, ValueSerializer<T> parser) throws IOException {
        ValueReader r = findOptional(tag, name);
        // The list cast shoudn't be necessary but javac complains for some reason
        return r == null ? (List) Collections.emptyList() : r.readList(parser);
    }

    /** {@inheritDoc} */
    @Override
    public Position readPosition(int tag, String name) throws IOException {
        return find(tag, name).readPosition();
    }


    /** {@inheritDoc} */
    @Override
    public Position readPosition(int tag, String name, Position defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readPosition();

    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime(int tag, String name) throws IOException {
        return find(tag, name).readPositionTime();
    }

    /** {@inheritDoc} */
    @Override
    public PositionTime readPositionTime(int tag, String name, PositionTime defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readPositionTime();
    }

    /** {@inheritDoc} */
    @Override
    public <T> Set<T> readSet(int tag, String name, ValueSerializer<T> parser) throws IOException {
        return new HashSet<>(readList(tag, name, parser));
    }


    /** {@inheritDoc} */
    @Override
    public String readText(int tag, String name, String defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readText();
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp readTimestamp(int tag, String name) throws IOException {
        return find(tag, name).readTimestamp();
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp readTimestamp(int tag, String name, Timestamp defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readTimestamp();
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt(int tag, String name) throws IOException {
        return find(tag, name).readVarInt();
    }

    /** {@inheritDoc} */
    @Override
    public BigInteger readVarInt(int tag, String name, BigInteger defaultValue) throws IOException {
        ValueReader r = findOptional(tag, name);
        return r == null ? defaultValue : r.readVarInt();
    }
}
