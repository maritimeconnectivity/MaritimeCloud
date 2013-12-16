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
package test.util;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.net.service.spi.Service;
import net.maritimecloud.net.service.spi.ServiceInitiationPoint;
import net.maritimecloud.net.service.spi.ServiceMessage;

/**
 * 
 * @author Kasper Nielsen
 */
public class TesstService2 extends Service {

    /** An initiation point */
    public static final ServiceInitiationPoint<TestInit> TEST_INIT = new ServiceInitiationPoint<>(TestInit.class);

    public static class TestInit extends ServiceMessage<TestReply> {

        private long id;

        private String source;

        private String target;

        final long timestamp = System.nanoTime();

        public TestInit() {}

        public TestInit(long id, String source, String target) {
            this.id = id;
            this.source = requireNonNull(source);
            this.target = requireNonNull(target);
        }

        public long getId() {
            return id;
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }

        /**
         * @return the timestamp
         */
        protected long getTimestamp() {
            return timestamp;
        }

        public TestReply reply() {
            return new TestReply(this);
        }

        /**
         * @param id
         *            the id to set
         */
        public void setId(long id) {
            this.id = id;
        }

        /**
         * @param source
         *            the source to set
         */
        public void setSource(String source) {
            this.source = source;
        }

        /**
         * @param target
         *            the target to set
         */
        public void setTarget(String target) {
            this.target = target;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "TestInit [id=" + id + ", source=" + source + ", target=" + target + "]";
        }
    }

    public static class TestReply extends ServiceMessage<Void> {

        private TestInit testInit;

        final transient long timestamp = System.nanoTime();

        public TestReply() {}

        TestReply(TestInit testInit) {
            this.testInit = requireNonNull(testInit);
        }

        /**
         * @return the testInit
         */
        public TestInit getTestInit() {
            return testInit;
        }

        /**
         * @return the timestamp
         */
        protected long getTimestamp() {
            return timestamp;
        }

        /**
         * @param testInit
         *            the testInit to set
         */
        public void setTestInit(TestInit testInit) {
            this.testInit = testInit;
        }

        public String toString() {
            return "reply-" + testInit.toString();
        }
    }
}
