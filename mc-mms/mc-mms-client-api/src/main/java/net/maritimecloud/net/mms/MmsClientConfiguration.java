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
package net.maritimecloud.net.mms;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionReaderSimulator;

/**
 *
 * @author Kasper Nielsen
 */
public class MmsClientConfiguration {

    static final String FACTORY = "net.maritimecloud.internal.mms.client.DefaultMmsClient";

    boolean autoConnect = true;

    private MaritimeId id;

    long keepAliveNanos = TimeUnit.SECONDS.toNanos(2);

    final List<MmsConnection.Listener> connectionListeners = new ArrayList<>();

    private String nodes = "localhost:43234";

    private PositionReader positionReader = new PositionReaderSimulator().forArea(Circle.create(0, 0, 50000));

    final Properties properties = new Properties();

    /**
     * Constructor
     * @param id the maritime id
     */
    MmsClientConfiguration(MaritimeId id) {
        this.id = id;
        String p = System.getProperty("maritimecloud.printmessages");
        if ("true".equalsIgnoreCase(p)) {
            addListener(new MmsConnection.Listener() {
                @Override
                public void textMessageReceived(String message) {
                    System.out.println("Received:" + message);
                }

                @Override
                public void textMessageSend(String message) {
                    System.out.println("Sending :" + message);
                }
            });
        }
    }

    /**
     * Adds a state listener that will be invoked whenever the state of the connection changes.
     *
     * @param listener
     *            the state listener
     * @throws NullPointerException
     *             if the specified listener is null
     * @return this configuration
     */
    public MmsClientConfiguration addListener(MmsConnection.Listener listener) {
        connectionListeners.add(requireNonNull(listener, "listener is null"));
        return this;
    }

    @SuppressWarnings("unchecked")
    public MmsClient build() {
        Class<?> c;
        try {
            c = Class.forName(FACTORY);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find factory class " + FACTORY + " make sure it is on the classpath");
        }
        Constructor<MmsClient> con;
        try {
            con = (Constructor<MmsClient>) c.getConstructor(MmsClientConfiguration.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find a valid constructor", e);
        }

        MmsClient client;
        try {
            client = con.newInstance(this);
        } catch (ReflectiveOperationException e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) e;
                if (ite.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) ite.getCause();
                } else if (ite.getCause() instanceof Error) {
                    throw (Error) ite.getCause();
                }
                throw new RuntimeException("Could not create a valid connection", ite.getCause());
            }
            throw new RuntimeException("Could not create a valid connection", e);
        }
        if (autoConnect) {
            client.connection().enable();
        }
        return client;
    }

    public MmsClient build(MaritimeId id) {
        this.id = id;
        return build();
    }

    public String getHost() {
        return nodes;
    }

    /**
     * @return the id
     */
    public MaritimeId getId() {
        return id;
    }


    /**
     * @param unit
     *            the timeunit
     * @return the keepAliveNanos
     */
    public long getKeepAlive(TimeUnit unit) {
        return unit.convert(keepAliveNanos, TimeUnit.NANOSECONDS);
    }

    /**
     * @return the connectionListeners
     */
    public List<MmsConnection.Listener> getListeners() {
        return connectionListeners;
    }

    /**
     * @return the positionSupplier
     */
    public PositionReader getPositionReader() {
        return positionReader;
    }

    /**
     * @return the autoConnect
     */
    public boolean isAutoConnect() {
        return autoConnect;
    }


    public Properties properties() {
        return properties;
    }

    /**
     * @param autoConnect
     *            the autoConnect to set
     */
    // setDisabledOnStart?
    public void setEnabledOnStartup(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public MmsClientConfiguration setHost(String host) {
        this.nodes = requireNonNull(host);
        return this;
    }

    public MmsClientConfiguration setId(MaritimeId id) {
        this.id = id;
        return this;
    }

    public MmsClientConfiguration setKeepAlive(long time, TimeUnit unit) {
        keepAliveNanos = unit.toNanos(time);
        return this;
    }

    public MmsClientConfiguration setPositionReader(PositionReader positionReader) {
        this.positionReader = requireNonNull(positionReader);
        return this;
    }

    public static MmsClientConfiguration create() {
        return new MmsClientConfiguration(null);
    }

    public static MmsClientConfiguration create(MaritimeId id) {
        return new MmsClientConfiguration(id);
    }

    public static MmsClientConfiguration create(String id) {
        return new MmsClientConfiguration(MaritimeId.create(id));
    }

    public static class Properties {
        private String description;

        private String name;

        private String organization;

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the organization
         */
        public String getOrganization() {
            return organization;
        }

        /**
         * @param description
         *            the description to set
         * @return this properties
         */
        public Properties setDescription(String description) {
            this.description = checkComma(description);
            return this;
        }

        /**
         * @param name
         *            the name to set
         * @return this properties
         */
        public Properties setName(String name) {
            this.name = checkComma(name);
            return this;
        }

        /**
         * @param organization
         *            the organization to set
         * @return this properties
         */
        public Properties setOrganization(String organization) {
            this.organization = checkComma(organization);
            return this;
        }

        private static String checkComma(String comma) {
            if (comma != null && comma.contains(",")) {
                throw new IllegalArgumentException(
                        "Sorry cannot use ',' at the moment it is used as an internal separator, was " + comma);
            }
            return comma;
        }
    }
}
