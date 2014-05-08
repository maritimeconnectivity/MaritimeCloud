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
package net.maritimecloud.net.service.spi;


/**
 * 
 * @author Kasper Nielsen
 */
// Maaske skal den ikke vaere serializeable???
// Serveren skal ikke bruge den. kun nogle informationer derfra
public abstract class Service {

    private final String name;

    protected Service() {
        // try {
        // this.name = (String) getClass().getField("NAME").get(null);
        // } catch (ReflectiveOperationException e) {
        // throw new ServiceConfigurationError("oops", e);
        // }
        this.name = getClass().getCanonicalName();

    }

    /**
     * @return the description
     */
    public String getDescription() {
        return "No Description";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
