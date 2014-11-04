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
package net.maritimecloud.internal.mms.client.endpoint;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import net.maritimecloud.internal.message.text.json.JsonMessageReader;
import net.maritimecloud.internal.net.endpoint.EndpointMirror;
import net.maritimecloud.internal.net.messages.MethodInvokeFailure;
import net.maritimecloud.internal.net.messages.MethodInvokeResult;
import net.maritimecloud.internal.net.util.DefaultEndpointInvocationFuture;
import net.maritimecloud.message.ValueSerializer;

/**
 *
 * @author Kasper Nielsen
 */
class RemoteInvocation {

    final DefaultEndpointInvocationFuture<Object> fr;

    final EndpointMirror mirror;

    final ValueSerializer<?> resultSerializer;

    @SuppressWarnings("unchecked")
    RemoteInvocation(DefaultEndpointInvocationFuture<?> fr, EndpointMirror mirror, ValueSerializer<?> vr) {
        this.fr = (DefaultEndpointInvocationFuture<Object>) requireNonNull(fr);
        this.mirror = requireNonNull(mirror);
        this.resultSerializer = vr;
    }

    void complete(MethodInvokeResult ack) {
        if (resultSerializer == null) {
            fr.complete(null);
        } else {
            if (ack.getFailure() != null) {
                MethodInvokeFailure f = ack.getFailure();
                fr.completeExceptionally(new RuntimeException(f.getExceptionType()));
            } else {
                String msg = ack.getResult().toStringUtf8();
                Object o;
                try {
                    o = JsonMessageReader.readFromString(msg, resultSerializer);
                    fr.complete(o);
                } catch (IOException e) {
                    fr.completeExceptionally(e);
                }
            }
        }
    }
}
