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

package net.maritimecloud.mms.server;

import java.util.concurrent.TimeUnit;

import org.cakeframework.container.Container.State;
import org.cakeframework.util.DurationFormatter;

import com.beust.jcommander.JCommander;

/**
 * Used to start a server from the command line.
 *
 * @author Kasper Nielsen
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MmsServerConfiguration configuration = new MmsServerConfiguration();
        new JCommander(configuration, args);

        MmsServer server = configuration.build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            try {
                for (int i = 0; i < 30; i++) {
                    if (!server.awaitState(State.TERMINATED, 1, TimeUnit.SECONDS)) {
                        System.out.println("Awaiting shutdown " + i + " / 30 seconds");
                    } else {
                        return;
                    }
                }
                System.err.println("Could not shutdown server properly");
            } catch (InterruptedException ignore) {}
        }));

        server.start().join();

        System.out.println("Wuhuu Maritime Messing Service started succesfully started in "
                + DurationFormatter.DEFAULT.format(server.start().getStartupTime()));
        if (configuration.getServerPort() >= 0 && !configuration.requireTLS) {
            System.out.println("MMS  : Running on port " + configuration.getServerPort() + " (unsecure)");
        } else {
            System.out.println("MMS  : Unencrypted MMS disabled");
        }

        if (configuration.getSecureport() >= 0) {
            System.out.println("MMS  : Running on port " + configuration.getSecureport() + " (secure)");
        }
        if (configuration.getWebserverPort() >= 0) {
            System.out.println("REST : Running on port " + configuration.getWebserverPort() + " (unsecure)");
        }
        System.out.println("Use CTRL+C to stop it");
    }
}
