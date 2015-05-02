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
package net.maritimecloud.mms.tests.server;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import net.maritimecloud.mms.server.MmsServer;

import org.junit.Test;

/**
 *
 * @author Kasper Nielsen
 */
public class StartStopServerTest {

    @Test
    public void noStart() throws InterruptedException {
        MmsServer s = MmsServer.create(12345);

        s.shutdown();
        assertTrue(s.awaitTerminated(10, TimeUnit.SECONDS));
    }

    @Test
    public void start() throws Exception {
        MmsServer s = MmsServer.create(12346);

        s.start().join();

        s.shutdown();
        assertTrue(s.awaitTerminated(10, TimeUnit.SECONDS));
    }

    @Test
    public void start2() throws Exception {
        MmsServer s = MmsServer.create(12347);

        s.start().join();

        s.shutdown();
        assertTrue(s.awaitTerminated(10, TimeUnit.SECONDS));
    }
}
