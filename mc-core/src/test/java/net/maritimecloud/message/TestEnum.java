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
package net.maritimecloud.message;

import java.io.IOException;

/**
 *
 * @author Kasper Nielsen
 */
public enum TestEnum implements MessageEnum {
    T1(1), T2(2), T3(3), T4(4);

    /** An enum parser that can create new instances of this class. */
    public static final MessageEnumSerializer<TestEnum> PARSER = new Parser();

    private final int value;

    private TestEnum(int value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public int getValue() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return toString();
    }

    public static TestEnum valueOf(int value) {
        switch (value) {
        case 1:
            return T1;
        case 2:
            return T2;
        case 3:
            return T3;
        case 4:
            return T4;
        default:
            return null;
        }
    }

    static class Parser extends MessageEnumSerializer<TestEnum> {

        /** {@inheritDoc} */
        @Override
        public TestEnum from(int value) {
            return TestEnum.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public TestEnum from(String name) {
            throw new UnsupportedOperationException("not implemented yet");
        }

        /** {@inheritDoc} */
        @Override
        public void write(int tag, String name, TestEnum t, MessageWriter writer) throws IOException {}
    }

}
