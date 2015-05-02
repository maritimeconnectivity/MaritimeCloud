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
package net.maritimecloud.net;

/**
 *
 * @author Kasper Nielsen
 */
// Or InternetConnectionProfile
class InternetConnectionType {
    // man kan vel specificere en policy
    // Naa man er low

    // Satellite
    public InternetConnectionType LOW = null;

    // 3G
    public InternetConnectionType MEDIUM = null;

    //
    public InternetConnectionType HIGH = null;

    boolean updateAlmanac() {
        return false;
    }
    // Bandwidth
    // free/not-free
    // Kan f.eks. saette en policy op at man opdatere at man kun opdatere Almanac, naa ConnectionType>low.

    // High Bandwidth detected, updating XYZ
    // Man kan vel configurere det i nogle filer.
    // prefered networks
    // med mindre man bare laver
    // XYZ, connect when up for 2 minutes

    // Man kan ogsaa skrive sin egen dynamiske switcher
}
