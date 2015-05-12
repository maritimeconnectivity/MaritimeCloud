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
package ch02connecting;

import net.maritimecloud.net.Environment;
import net.maritimecloud.net.mms.MmsClientConfiguration;

public class MMSSelectEnvironment {

public static void setStaticPosition() {
    MmsClientConfiguration conf = MmsClientConfiguration.create();
    //tag::setEnvironment[]
    // If you want to use the sandbox environment use one of these
    conf.setHost(Environment.SANDBOX);
    conf.setHost(Environment.SANDBOX_UNENCRYPTED);

    // If you want to use the test environment use this
    conf.setHost(Environment.TEST);
    //end::setEnvironment[]
}
    
}
