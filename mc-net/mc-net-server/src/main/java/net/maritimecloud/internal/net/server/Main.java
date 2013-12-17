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

package net.maritimecloud.internal.net.server;

import java.util.concurrent.TimeUnit;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParametersDelegate;

/**
 * Used to start a server from the command line.
 * 
 * @author Kasper Nielsen
 */
public class Main {

    @ParametersDelegate
    ServerConfiguration configuration = new ServerConfiguration();

    volatile InternalServer server;

    void kill() {
        InternalServer server = this.server;
        if (server != null) {
            server.shutdown();
            try {
                for (int i = 0; i < 30; i++) {
                    if (!server.awaitTerminated(1, TimeUnit.SECONDS)) {
                        System.out.println("Awaiting shutdown " + i + " / 30 seconds");
                    } else {
                        return;
                    }
                }

                throw new IllegalStateException("Could not shutdown server properly");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /** {@inheritDoc} */
    protected void run(String[] args) throws Exception {
        JCommander jc = new JCommander(this, args);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                kill();
            }
        });
        InternalServer server = new InternalServer(configuration);
        server.start();
        this.server = server; // only set it if it started succesfully

        System.out.println("Wuhuu Maritime Cloud Server started! Running on port " + configuration.getServerPort());
        System.out.println("Use CTRL+C to stop it");
    }

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }
}
