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
package ch04geocast;

import maritimecloud.examples.Hello;
import net.maritimecloud.net.mms.MmsBroadcastOptions;
import net.maritimecloud.net.mms.MmsClient;

/**
 *
 * @author Kasper Nielsen
 */
@SuppressWarnings("null")
public class Broadcasting {

    public void listen() {
        MmsClient client = null;
//tag::listen[]
client.broadcastSubscribe(Hello.class, (context, broadcast) -> {
    System.out.println(context.getSender() + " sends " + broadcast.getMsg());
});
//end::listen[]

//tag::send[]
Hello hello = new Hello();
hello.setMsg("HelloWorld");
client.broadcast(hello);
//end::send[]
    }
    
    public void sendClustom() {
        MmsClient client = null;
//tag::sendCustom[]
Hello hello = new Hello();
hello.setMsg("HelloWorld");
client.broadcast(hello, new MmsBroadcastOptions().toArea(30000));
//end::sendCustom[]
    }
}
