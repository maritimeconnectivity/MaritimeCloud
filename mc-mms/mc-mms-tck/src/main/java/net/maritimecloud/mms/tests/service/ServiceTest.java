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
package net.maritimecloud.mms.tests.service;

import net.maritimecloud.mms.tests.AbstractNetworkTest;

/**
 *
 * @author Kasper Nielsen
 */
public class ServiceTest extends AbstractNetworkTest {
    //
    // @Test
    // public void oneClient() throws Exception {
    // MaritimeCloudClient c1 = newClient(ID1);
    // c1.serviceRegister(HelloService.GET_NAME, HelloService.create("foo123")).awaitRegistered(4, TimeUnit.SECONDS);
    //
    // MaritimeCloudClient c2 = newClient(ID6);
    // ServiceEndpoint<GetName, Reply> end = c2.serviceLocate(HelloService.GET_NAME).nearest()
    // .get(6, TimeUnit.SECONDS);
    // assertEquals(ID1, end.getId());
    // NetworkFuture<Reply> f = end.invoke(new HelloService.GetName());
    // assertEquals("foo123", f.get(4, TimeUnit.SECONDS).getName());
    // }
    //
    // @Test
    // public void manyClients() throws Exception {
    // MaritimeCloudClient c1 = newClient(ID1);
    // c1.serviceRegister(HelloService.GET_NAME, HelloService.create("foo123")).awaitRegistered(4, TimeUnit.SECONDS);
    //
    // for (MaritimeCloudClient c : newClients(20)) {
    // ServiceEndpoint<GetName, Reply> end = c.serviceLocate(HelloService.GET_NAME).nearest()
    // .get(6, TimeUnit.SECONDS);
    // assertEquals(ID1, end.getId());
    // NetworkFuture<Reply> f = end.invoke(new HelloService.GetName());
    // assertEquals("foo123", f.get(4, TimeUnit.SECONDS).getName());
    // }
    // }
    //
    // @Test
    // public void oneClientHandle() throws Exception {
    // MaritimeCloudClient c1 = newClient(ID1);
    // c1.serviceRegister(HelloService.GET_NAME, HelloService.create("foo123")).awaitRegistered(4, TimeUnit.SECONDS);
    //
    // MaritimeCloudClient c2 = newClient(ID6);
    //
    //
    // ServiceEndpoint<GetName, Reply> end = c2.serviceLocate(HelloService.GET_NAME).nearest()
    // .get(6, TimeUnit.SECONDS);
    //
    // assertEquals(ID1, end.getId());
    // NetworkFuture<Reply> f = end.invoke(new HelloService.GetName());
    // final CountDownLatch cdl = new CountDownLatch(1);
    //
    // f.handle(new BiConsumer<HelloService.Reply, Throwable>() {
    // public void accept(Reply l, Throwable r) {
    // assertNull(r);
    // assertEquals("foo123", l.getName());
    // cdl.countDown();
    // }
    // });
    // assertTrue(cdl.await(1, TimeUnit.SECONDS));
    // }
}
