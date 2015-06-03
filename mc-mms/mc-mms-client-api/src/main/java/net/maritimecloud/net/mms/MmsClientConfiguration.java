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

import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.Environment;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionReaderSimulator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * MMS Client configuration class.
 * @author Kasper Nielsen
 */
public class MmsClientConfiguration {

    /** The default implementation of the MmsClient. */
    private static final String IMPLEMENTATION = "net.maritimecloud.internal.mms.client.DefaultMmsClient";

    /** Whether or not we will automatically connect to the MMS server when the client has been created. */
    private boolean autoConnect = true;

    private MaritimeId id;

    long keepAliveNanos = TimeUnit.SECONDS.toNanos(2);

    final List<MmsConnection.Listener> connectionListeners = new ArrayList<>();

    private String host;

    private PositionReader positionReader = new PositionReaderSimulator().forArea(Circle.create(0, 0, 50000));

    final Properties properties = new Properties();

    private boolean useBinary;

    private String keystore;

    private String keystorePassword;

    private String truststore;

    private String truststorePassword;

    private Map<String, String> headers = new HashMap<>();

    /**
     * Constructor
     *
     * @param id
     *            the maritime id
     */
    MmsClientConfiguration(MaritimeId id) {
        this.id = id;
        String p = System.getProperty("maritimecloud.printmessages");
        if ("true".equalsIgnoreCase(p)) {
            System.err.println("This setting is deprecated for 0.3 and will be removed in the next release (0.4)");
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
        setHost(Environment.SANDBOX_UNENCRYPTED);
    }

    /**
     * Factory method for creating a new MMS client configuration
     * @return a new MMS client configuration
     */
    public static MmsClientConfiguration create() {
        return new MmsClientConfiguration(null);
    }

    /**
     * Factory method for creating a new MMS client configuration
     * @param id the maritime id of the client
     * @return a new MMS client configuration
     */
    public static MmsClientConfiguration create(MaritimeId id) {
        return new MmsClientConfiguration(id);
    }

    /**
     * Factory method for creating a new MMS client configuration
     * @param id the maritime id of the client
     * @return a new MMS client configuration
     */
    public static MmsClientConfiguration create(String id) {
        return new MmsClientConfiguration(MaritimeId.create(id));
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

    /**
     * Build an MmsClient from the configuration
     *
     * @return the MmsClient defined by the configuration
     */
    @SuppressWarnings("unchecked")
    public MmsClient build() {
        Class<?> c;
        try {
            c = Class.forName(IMPLEMENTATION);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find factory class " + IMPLEMENTATION
                    + " make sure it is on the classpath");
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
                throw new RuntimeException("Could not create a client", ite.getCause());
            }
            throw new RuntimeException("Could not create a client", e);
        }
        if (autoConnect) {
            client.connection().enable();
        }
        return client;
    }

    /**
     * Builds an MmsClient for the given maritime id
     * @param id the maritime id
     * @return an MmsClient for the maritime id
     */
    public MmsClient build(MaritimeId id) {
        this.id = id;
        return build();
    }

    // ****************************************
    // ********** Getters and Setters *********
    // ****************************************

    public String getHost() {
        return host;
    }

    public MmsClientConfiguration setHost(String host) {
        this.host = requireNonNull(host);
        return this;
    }

    public MmsClientConfiguration setHost(Environment environment) {
        this.host = requireNonNull(environment.mmsServerURL());
        return this;
    }

    public MaritimeId getId() {
        return id;
    }

    public MmsClientConfiguration setId(MaritimeId id) {
        this.id = id;
        return this;
    }

    public boolean useBinary() {
        return useBinary;
    }

    public MmsClientConfiguration setUseBinary(boolean useBinary) {
        this.useBinary = useBinary;
        return this;
    }

    public long getKeepAlive(TimeUnit unit) {
        return unit.convert(keepAliveNanos, TimeUnit.NANOSECONDS);
    }

    public MmsClientConfiguration setKeepAlive(long time, TimeUnit unit) {
        keepAliveNanos = unit.toNanos(time);
        return this;
    }

    public List<MmsConnection.Listener> getListeners() {
        return connectionListeners;
    }

    public PositionReader getPositionReader() {
        return positionReader;
    }

    public MmsClientConfiguration setPositionReader(PositionReader positionReader) {
        this.positionReader = requireNonNull(positionReader);
        return this;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setEnabledOnStartup(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public Properties properties() {
        return properties;
    }

    public String getKeystore() {
        return keystore;
    }

    public MmsClientConfiguration setKeystore(String keystore) {
        this.keystore = keystore;
        return this;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public MmsClientConfiguration setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
        return this;
    }

    public String getTruststore() {
        return truststore;
    }

    public MmsClientConfiguration setTruststore(String truststore) {
        this.truststore = truststore;
        return this;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public MmsClientConfiguration setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public MmsClientConfiguration setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    // ****************************************
    // ********** Helper Classes **************
    // ****************************************

    /**
     * Class that defines the client properties such as name and organization
     */
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
