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
package net.maritimecloud.mms.server.rest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Path;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 *
 * @author Kasper Nielsen
 */
@Path("/tail")
public class TailLogger {

    final Cache<String, List<?>> cache;

    public TailLogger() {
        CacheBuilder<Object, Object> b = CacheBuilder.newBuilder();
        b.expireAfterAccess(1, TimeUnit.HOURS);
        cache = b.build();
    }

    @Path("/incoming")
    public void incoming() {

    }


    @Path("/outgoing")
    public void outcoming() {

    }
}
