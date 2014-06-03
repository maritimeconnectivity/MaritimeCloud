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
package net.maritimecloud.net.service;

import java.util.List;

import net.maritimecloud.net.NetworkFuture;
import net.maritimecloud.net.service.spi.ServiceMessage;

/**
 *
 * @author Kasper Nielsen
 */
public interface ServiceLocator<T, E extends ServiceMessage<T>> {

    /**
     * Limits the result to services with the specified distance
     *
     * @param meters
     *            the distance in meters
     * @return a new service locator
     */
    ServiceLocator<T, E> withinDistanceOf(int meters);

    /**
     * Returns the nearest service end point to the clients current position.
     *
     * @return the nearest service end point to the clients current position
     */
    NetworkFuture<ServiceEndpoint<E, T>> nearest();

    /**
     * Returns a list of multiple service end points to the clients current position.
     *
     * @param limit
     *            the maximum number of service end points to return
     * @return a list of multiple service end points to the clients current position
     */
    NetworkFuture<List<ServiceEndpoint<E, T>>> nearest(int limit);
}
