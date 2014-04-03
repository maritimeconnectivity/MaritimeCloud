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
package net.maritimecloud.internal.net.messages.s2c.service;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.internal.net.messages.MessageType;
import net.maritimecloud.internal.net.messages.TextMessageReader;
import net.maritimecloud.internal.net.messages.TextMessageWriter;
import net.maritimecloud.internal.net.messages.s2c.ServerResponseMessage;

/**
 *
 * @author Kasper Nielsen
 */
public class FindServiceResult extends ServerResponseMessage {

    final String[] maritimeIds;

    public FindServiceResult(TextMessageReader pr) throws IOException {
        super(MessageType.FIND_SERVICE_ACK, pr);
        this.maritimeIds = requireNonNull(pr.takeStringArray());
    }

    public FindServiceResult(long id, String[] ids) {
        super(MessageType.FIND_SERVICE_ACK, id);
        this.maritimeIds = requireNonNull(ids);

    }

    public String[] getMax() {
        return maritimeIds;
    }

    public final Class<String[]> getType() {
        return String[].class;
    }

    /** {@inheritDoc} */
    @Override
    protected void write1(TextMessageWriter w) {
        w.writeStringArray(maritimeIds);
    }
}
