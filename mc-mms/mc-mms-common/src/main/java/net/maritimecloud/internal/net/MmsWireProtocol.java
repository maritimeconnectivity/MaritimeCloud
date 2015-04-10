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
package net.maritimecloud.internal.net;

/**
 * A dummy implementation to be replaced at a later stage.
 *
 * For now, the class merely exposes the USE_BINARY flag
 * that defines whether to use text (JSON) or binary (Protobuf)
 * over the wire.
 */
public class MmsWireProtocol {
    public static boolean USE_BINARY = false;
}
