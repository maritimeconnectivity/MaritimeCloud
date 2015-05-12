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
package ch03services;
//tag::code[]
import java.util.concurrent.TimeUnit;

import maritimecloud.examples.MathEndpoint;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.EndpointInvocationFuture;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;

public class ServiceConsumer {
  public static void main(String[] args) throws Exception {
    MmsClient c = MmsClientConfiguration.create("mmsi:2223333").build();

    MathEndpoint e = c.endpointCreate(MaritimeId.create("mmsi:1112222"), MathEndpoint.class);
        
    EndpointInvocationFuture<Integer> sum = e.sum(123, 456);
    System.out.println(" 123 + 456 = " + sum.join());

    c.shutdown();
    c.awaitTermination(1, TimeUnit.SECONDS);
  }
}
//end::code[]