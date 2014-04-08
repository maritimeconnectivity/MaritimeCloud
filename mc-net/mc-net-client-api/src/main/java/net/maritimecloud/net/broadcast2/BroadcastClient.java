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
package net.maritimecloud.net.broadcast2;

import net.maritimecloud.core.net.BroadcastMessage;


/**
 *
 * @author Kasper Nielsen
 */
public interface BroadcastClient {

    <T extends BroadcastMessage<T>> BroadcastFuture sendBroadcast(T message);

    <T extends BroadcastMessage<T>> BroadcastFuture sendBroadcast(T message, BroadcastSendOptions options);

    <T extends BroadcastMessage<T>> BroadcastSubscription listenForBroadcast(Class<T> messageType,
            BroadcastListener<T> listener);

    <T extends BroadcastMessage<T>> BroadcastSubscription listenForBroadcast(Class<T> messageType,
            BroadcastListener<T> listener, BroadcastListenOptions options);
}
