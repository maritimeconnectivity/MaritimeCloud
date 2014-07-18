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
package net.maritimecloud.msdl.plugins.javagen;


/**
 *
 * @author Kasper Nielsen
 */
public class ClassDefinitions {

    public static final String PACKAGE_ENDPOINT = "net.maritimecloud.mms.endpoint";

    public static final String ENDPOINT_LOCAL = "EndpointLocal";

    public static final String ENDPOINT_LOCAL_CLASS = PACKAGE_ENDPOINT + "." + ENDPOINT_LOCAL;

    public static final String ENDPOINT_INVOCATOR = "EndpointInvocator";

    public static final String ENDPOINT_INVOCATOR_CLASS = PACKAGE_ENDPOINT + "." + ENDPOINT_INVOCATOR;

    public static final String ENDPOINT_INVOCATION_FUTURE = "EndpointInvocationFuture";

    public static final String ENDPOINT_INVOCATION_FUTURE_CLASS = PACKAGE_ENDPOINT + "." + ENDPOINT_INVOCATION_FUTURE;
}
