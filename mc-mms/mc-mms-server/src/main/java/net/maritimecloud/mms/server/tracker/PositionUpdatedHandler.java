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
package net.maritimecloud.mms.server.tracker;

import net.maritimecloud.util.geometry.PositionTime;

/**
 * A position update handler is used for notifying the user about updated positions within the area of interest.
 * 
 * @author Kasper Nielsen
 */
public abstract class PositionUpdatedHandler<T> {

    /**
     * Invoked whenever an object enters the area of interest.
     * 
     * @param t
     *            the tracked object
     * @param positiontime
     *            the position and time when entering
     */
    protected void entering(T t, PositionTime positiontime, EnterReason reasonForEntering) {}

    /**
     * Invoked whenever a tracked object position is updated.
     * 
     * @param t
     *            the tracked object
     * @param previous
     *            the previous position (is null if entering the area of interest)
     * @param current
     *            the current position (is null if existing the area of interest)
     */
    protected void updated(T t, PositionTime previous, PositionTime current) {}

    /**
     * Invoked whenever an object leaves the area of interest.
     * 
     * @param t
     *            the tracked object
     */
    protected void exiting(T t, LeaveReason reasonForEntering) {}
    
    public enum EnterReason {
        ENTERED_AREA,
        CONNECTED;
    }
    
    public enum LeaveReason {
        LEFT_AREA,
        DISCONNECTED,
        TIMEOUT;
    }
}
