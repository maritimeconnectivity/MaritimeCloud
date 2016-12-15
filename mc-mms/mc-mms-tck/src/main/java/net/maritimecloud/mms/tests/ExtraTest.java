/*
 * Copyright (c) 2008 Kasper Nielsen.
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
package net.maritimecloud.mms.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.net.mms.MmsClient;

/**
 *
 * @author Kasper Nielsen
 */
public class ExtraTest extends AbstractNetworkTest {

    public void test() throws Exception {
        MmsClient c = newClient(ID1);
        assertTrue(c.connection().awaitConnected(10, TimeUnit.SECONDS));
        assertEquals(1, clients(si).size());
    }
}
