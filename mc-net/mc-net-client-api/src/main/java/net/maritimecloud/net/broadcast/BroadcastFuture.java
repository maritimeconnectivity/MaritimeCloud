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
package net.maritimecloud.net.broadcast;

import net.maritimecloud.net.ConnectionClosedException;
import net.maritimecloud.net.ConnectionFuture;
import net.maritimecloud.net.MaritimeCloudClient;
import net.maritimecloud.util.function.Consumer;

/**
 * A broad future is created every time a broadcast is sent via {@link MaritimeCloudClient#broadcast(BroadcastMessage)}
 * or {@link MaritimeCloudClient#broadcast(BroadcastMessage, BroadcastOptions)}.
 * 
 * @author Kasper Nielsen
 */
public interface BroadcastFuture {

    /**
     * A future that can be used to determine when the messages has been received by the server. The future will return
     * with {@link ConnectionClosedException} if the broadcast is not received by the server.
     * 
     * @return a future that can be used to determine when the messages has been received by the server
     */
    ConnectionFuture<Void> receivedOnServer();

    /**
     * If {@link BroadcastOptions#isReceiverAckEnabled()} is enabled. The specified consumer will be invoked every time
     * a remote actor has received the broadcast message.
     * 
     * @param consumer
     *            the consumer of each acknowledgment
     * @throws UnsupportedOperationException
     *             if the messages has not been sent with receiver acknowledgment enabled
     */
    void onAck(Consumer<? super BroadcastMessage.Ack> consumer);

    /**
     * Invoked whenever the broadcast has finished being delivered to remote parties. Either because the broadcast has
     * been delivered to all available parties. Or because a timeout occurred.
     * <p>
     * {@link #onAck(Consumer)} will not be invoked anymore after this method has been invoked.
     */
    void onFinish();
}
