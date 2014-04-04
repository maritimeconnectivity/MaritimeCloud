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
package net.maritimecloud.core.message;

import java.io.IOException;
import java.util.List;

import net.maritimecloud.core.message.MessageSerializable;
import net.maritimecloud.core.message.MessageWriter;

/**
 *
 * @author Kasper Nielsen
 */
public class TestSerializers {


    static List<IntWrapper> intListOf(int size) {
        return null;
    }

    static IntWrapper intOf(int i) {
        return new IntWrapper(i);
    }

    public static class IntWrapper implements MessageSerializable {
        final int i;

        IntWrapper(int i) {
            this.i = i;
        }

        /** {@inheritDoc} */
        @Override
        public void writeTo(MessageWriter w) throws IOException {
            w.writeInt32(1, "ivalue", i);
        }
    }
}
