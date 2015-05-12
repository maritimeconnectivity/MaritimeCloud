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
import maritimecloud.examples.AbstractMathEndpoint;
import net.maritimecloud.net.MessageHeader;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;

public class ServiceProvider {
  public static void main(String[] args) throws Exception {
    MmsClient c = MmsClientConfiguration.create("mmsi:1112222").build();

    c.endpointRegister(new AbstractMathEndpoint() {
      protected Integer sum(MessageHeader h, Integer left, Integer right) {
        System.out.println("Trying to sum " + left + " " + right + " for " + h.getSender());
        return left + right;
      }
    });

    Thread.sleep(1000000);
  }
}
//end::code[]
