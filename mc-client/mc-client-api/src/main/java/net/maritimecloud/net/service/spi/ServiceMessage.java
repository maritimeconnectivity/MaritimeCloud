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

import java.util.ServiceConfigurationError;

/**
 *
 * @author Kasper Nielsen
 */
public abstract class ServiceMessage<T> {

    public String messageName() {
        String name = getClass().getSimpleName();
        return name;
    }

    public String serviceName() {
        try {
            return (String) serviceType().getField("NAME").get(null);
        } catch (ReflectiveOperationException e) {
            throw new ServiceConfigurationError("oops", e);
        }
    }

    /**
     * @return the serviceType
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Service> serviceType() {
        return (Class<? extends Service>) getClass().getDeclaringClass();
    }
}
