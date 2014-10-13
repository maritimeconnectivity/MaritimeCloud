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
package net.maritimecloud.util.geometry;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Kasper Nielsen
 */
public class AbstractAreaTest {

    // Positions are order as telephone display (P5) being the center (0,0), P1 top left, P9 bottom right

    static final Position P1 = Position.create(10, -10);

    static final Position P2 = Position.create(10, 0);

    static final Position P3 = Position.create(10, 10);

    static final Position P4 = Position.create(0, -10);

    static final Position P5 = Position.create(0, 0);

    static final Position P6 = Position.create(0, 10);

    static final Position P7 = Position.create(-10, -10);

    static final Position P8 = Position.create(-10, 0);

    static final Position P9 = Position.create(-10, 10);

    static Collection<Position> PALL = Arrays.asList(P1, P2, P3, P4, P5, P6, P7, P8, P9);
}
