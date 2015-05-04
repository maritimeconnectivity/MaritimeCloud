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
package net.maritimecloud.core.id;

import static java.util.Objects.requireNonNull;


/**
 *
 * @author Kasper Nielsen
 */
class TestId extends MaritimeId {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private final String str;

    public TestId(String str) {
        super("test");
        this.str = requireNonNull(str);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return str.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof TestId && equals((TestId) obj);
    }

    public boolean equals(TestId other) {
        return str.equals(other.str);
    }

    public String toString() {
        return "test:" + str;
    }

    /** {@inheritDoc} */
    @Override
    public String getId() {
        return str;
    }

    // mmsi:102323233

    // enid://

    // area://box/
}
