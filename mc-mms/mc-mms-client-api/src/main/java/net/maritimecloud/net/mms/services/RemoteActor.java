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
package net.maritimecloud.net.mms.services;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.util.Timestamp;
import net.maritimecloud.util.geometry.Position;

/**
 *
 * @author Kasper Nielsen
 */
public interface RemoteActor {

    MaritimeId getId();

    Position getLatestPosition();

    Timestamp getLastSeen();

    String getName();

    String getDescription();

    String getOrganization();
}
