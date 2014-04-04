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
package net.maritimecloud.net.service.rpc;

import net.maritimecloud.core.message.Message;

/**
 * 
 * @author Kasper Nielsen
 */
public interface RpcClient {

    <T extends Message<T>, R extends Message<R>> RpcFuture<R> invoke(RpcEndpoint<T, R> endpoint, T message);

    <T extends Message<T>, R extends Message<R>> RpcFuture<R> invoke(RpcEndpoint<T, R> endpoint, T message,
            RpcOptions options);

    // VoidMessage?
    <T extends Message<T>, R extends Message<R>> RpcSubscription rpcListen(RpcEndpoint<T, R> endpoint,
            RpcListener<T, R> listener);

    <T extends Message<T>, R extends Message<R>> RpcSubscription rpcListen(RpcEndpoint<T, R> endpoint,
            RpcListener<T, R> listener, RpcListener.Options options);
}
