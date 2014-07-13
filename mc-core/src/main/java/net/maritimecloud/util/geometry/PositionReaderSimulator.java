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
package net.maritimecloud.util.geometry;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import net.maritimecloud.util.units.SpeedUnit;

/**
 * A simple builder for creating position readers that simulate simple sailing patterns.
 *
 * @author Kasper Nielsen
 */
public final class PositionReaderSimulator {

    final Random random;

    DoubleSupplier speedSource;

    LongSupplier timeSource = new LongSupplier() {
        public long getAsLong() {
            return System.currentTimeMillis();
        }
    };

    /** Creates a new PositionReaderSimulator. With a non-deterministic random source */
    public PositionReaderSimulator() {
        this(new Random());
        setSpeedVariable(1, 40, SpeedUnit.KNOTS);
    }

    /**
     * Creates a new PositionReaderSimulator.
     *
     * @param random
     *            the random source of data
     */
    public PositionReaderSimulator(Random random) {
        this.random = requireNonNull(random);
        setSpeedVariable(1, 40, SpeedUnit.KNOTS);
    }

    /**
     * Creates a new simulated position reader. The simulated vessel will start at random position within the specified
     * area. And travel to another random position within the area with a random speed. When it arrives at the position
     * it will choose another random position within the area to travel to and so on.
     *
     * @param supplier
     *            a supplier of positions
     * @return the simulated position reader
     * @throws NullPointerException
     *             if the specified area is null
     */
    PositionReader forA(Supplier<Position> supplier) {
        return new AbtractSimulatedReader(this, supplier);
    }

    /**
     * Creates a new simulated position reader. The simulated vessel will start at random position within the specified
     * area. And then travel to another random position within the area. When it arrives at the position it will choose
     * another random position within the area to travel to and so on.
     *
     * @param area
     *            the area to travel within
     * @return a new position reader
     * @throws NullPointerException
     *             if the specified area is null
     */
    public PositionReader forArea(final Area area) {
        final Random r = random;
        return forA(new Supplier<Position>() {
            public Position get() {
                return r == null ? area.getRandomPosition() : area.getRandomPosition(r);
            }
        });
    }

    /**
     * Creates a new simulated reader with the specified route. When the vessel reaches the last of the specified
     * positions. It will sail the same route back. Continuing indefinitely.
     * <p>
     * If the first position and the last position is equivalent the positions will be delivered as if the vessel is
     * sailing in a circle. When the ship reaches the final position it will sail to position number 2 instead of
     * sailing the same route back.
     *
     * @param positions
     *            each position for route
     * @return a new simulated reader
     */
    public PositionReader forRoute(Position... positions) {
        final LinkedList<Position> l = new LinkedList<>(Arrays.asList(positions));
        new ConcurrentLinkedQueue<>(l);// im lazy, this checks for null positions
        if (l.size() < 2) {
            throw new IllegalArgumentException("Must specified at least 2 positions");
        }

        // if the first position is equal to the last position we are sailing in circles, remove the last one then
        if (l.getFirst().equals(l.getLast())) {
            l.removeLast();
        } else if (l.size() > 2) {
            // dont do it for A->B->A->B....
            // but A->B->C->D should become A->B->C->D->C->B
            LinkedList<Position> l2 = new LinkedList<>(l);
            l2.removeFirst();
            l2.removeLast();
            l.addAll(l2);
        }
        final Position[] p = l.toArray(new Position[l.size()]);
        return forA(new Supplier<Position>() {
            int counter = p.length - 1;

            public Position get() {
                return p[counter = (counter + 1) % p.length];
            }
        });
    }

    /**
     * Sets a fixed speed for the vessel.
     * <p>
     * If no speed is set the simulated position reader will use a variable speed between 1 and 40 knots.
     *
     * @param speed
     *            the speed of the vessel
     * @param speedUnit
     *            the unit of speed
     * @return this builder
     * @throws NullPointerException
     *             if the speed unit is null
     * @throws IllegalArgumentException
     *             if the specified speed is non positive
     * @see #setSpeedVariable(double, double, SpeedUnit)
     */
    public PositionReaderSimulator setSpeedFixed(double speed, SpeedUnit speedUnit) {
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be positive (>0)");
        }
        final double metersPerSecond = speedUnit.toMetersPerSecond(speed);
        speedSource = new DoubleSupplier() {
            public double getAsDouble() {
                return metersPerSecond;
            }
        };
        return this;
    }

    /**
     * Sets a variable speed for the vessel. Every time the vessel reaches a target position. It will change the speed
     * to a random number between minimum speed and maximum speed.
     * <p>
     * If no speed is set the simulated position reader will use a variable speed between 1 and 40 knots.
     *
     * @param minSpeed
     *            the minimum speed of the vessel
     * @param maxSpeed
     *            the maximum speed of the vessel
     * @param speedUnit
     *            the unit of speed
     * @return this builder
     * @throws NullPointerException
     *             if the speed unit is null
     * @throws IllegalArgumentException
     *             if the specified minimum speed is non positive or the max speed is than or equal to minimum speed
     * @see #setSpeedFixed(double, SpeedUnit)
     */
    public PositionReaderSimulator setSpeedVariable(double minSpeed, double maxSpeed, SpeedUnit speedUnit) {
        if (minSpeed <= 0) {
            throw new IllegalArgumentException("Minimum Speed must be positive (>0), was " + minSpeed);
        } else if (maxSpeed <= minSpeed) {
            throw new IllegalArgumentException("Maximum Speed must greater than minimum speed, minSpeed= " + minSpeed
                    + ", maxSpeed=" + maxSpeed);
        }
        final double metersPerSecondMin = speedUnit.toMetersPerSecond(minSpeed);
        final double metersPerSecondMax = speedUnit.toMetersPerSecond(maxSpeed);
        speedSource = new DoubleSupplier() {
            public double getAsDouble() {
                return Area.nextDouble(random, metersPerSecondMin, metersPerSecondMax);
            }
        };
        return this;
    }

    /**
     * Sets the time source that is used to determine how long duration has passed between succint invocations of
     * {@link PositionReader#getCurrentPosition()}. If no time source is set, {@link System#currentTimeMillis()} is
     * used.
     *
     * @param timeSource
     *            the time source
     * @return this builder
     * @throws NullPointerException
     *             if the specified time source is null
     */
    public PositionReaderSimulator setTimeSource(LongSupplier timeSource) {
        this.timeSource = requireNonNull(timeSource);
        return this;
    }

    /**
     * Sets a deterministic time source that increment the time with the specified amount of milliseconds every time.
     *
     * @param milliesIncrement
     *            the number of milliseconds that the ship will sail every time
     *            {@link PositionReader#getCurrentPosition()} is invoked
     * @return this builder
     */
    public PositionReaderSimulator setTimeSourceFixedSlice(final long milliesIncrement) {
        if (milliesIncrement <= 0) {
            throw new IllegalArgumentException();
        }
        return setTimeSource(new LongSupplier() {
            final AtomicLong al = new AtomicLong();

            public long getAsLong() {
                return al.incrementAndGet() * milliesIncrement;
            }
        });
    }

    static class AbtractSimulatedReader extends PositionReader {

        PositionTime currentPosition;

        /** The current speed of the vessel in meters per second. */
        double currentSpeed;

        /** A supplier that can calculate the next speed */
        DoubleSupplier distanceSupplier;

        final Supplier<Position> positionSupplier;

        /** The current target of the vessel. */
        Position target;

        /** The time source. */
        final LongSupplier timeSource;

        AbtractSimulatedReader(PositionReaderSimulator prs, Supplier<Position> positionSupplier) {
            this.timeSource = requireNonNull(prs.timeSource);
            this.positionSupplier = requireNonNull(positionSupplier);
            this.currentPosition = positionSupplier.get().withTime(timeSource.getAsLong());
            this.target = positionSupplier.get();
            this.distanceSupplier = prs.speedSource;
            this.currentSpeed = distanceSupplier.getAsDouble();
        }

        /** {@inheritDoc} */
        @Override
        public final PositionTime getCurrentPosition() {
            long now = timeSource.getAsLong();
            // no time elapsed, return last position
            if (now <= currentPosition.getTime()) {
                return currentPosition;
            }

            double distanceSailed = currentSpeed * (now - currentPosition.getTime()) / 1000;
            for (;;) {
                double distanceToTarget = currentPosition.rhumbLineDistanceTo(target);
                if (distanceSailed <= distanceToTarget) {
                    return currentPosition = CoordinateSystem.CARTESIAN.pointOnBearing(currentPosition, distanceSailed,
                            currentPosition.rhumbLineBearingTo(target)).withTime(now);
                } else {// okay we need to travel long than to target. Find the next point to travel to
                    distanceSailed -= distanceToTarget;
                    currentPosition = target.withTime(0);// not going to use the time parameter
                    target = positionSupplier.get();
                    this.currentSpeed = distanceSupplier.getAsDouble();
                }
            }
        }
    }
}
