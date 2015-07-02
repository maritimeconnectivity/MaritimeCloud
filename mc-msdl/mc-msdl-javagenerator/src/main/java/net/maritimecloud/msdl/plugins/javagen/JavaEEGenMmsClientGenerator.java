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
package net.maritimecloud.msdl.plugins.javagen;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a singleton MmsClientService class.
 * <p/>
 * The class is loaded from the "MmsClientService.java" template of the resources root.
 */
class JavaEEGenMmsClientGenerator {

    public final static String MMS_CLIENT_PACKAGE = "net.maritimecloud.mms.jee";
    public final static String MMS_CLIENT_CLASS = "MmsClientService.java";

    /** Generates the MmsClientService classes */
    public static Path writeSource(String license, Path outputPath) throws IOException {

        StringBuilder sb = new StringBuilder();

        // Prepend license
        if (license != null && license.trim().length() > 0) {
            sb.append(license).append("\n");
        }

        // Add the class definition
        sb.append(loadResource("/" + MMS_CLIENT_CLASS));

        // And write to the destination file
        return writeSource(sb, outputPath, MMS_CLIENT_PACKAGE, MMS_CLIENT_CLASS);
    }

    /** Loads the content of the given file from the class path */
    private static String loadResource(String file) {
        StringBuilder out = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(JavaEEGenMmsClientGenerator.class.getResourceAsStream(file)))) {
            String line;
            while ((line = in.readLine()) != null) {
                out.append(line).append("\n");
            }
        } catch (Exception e) {
            // This should never happen
            Logger.getLogger("msdl").log(Level.SEVERE, "Error reading html ", e);
        }
        return out.toString();
    }

    /**
     * Writes the class to the file under the given root.
     *
     * Code mostly lifted from the {@code CodegenClass.writeSource} method
     *
     * @param s the source file contents
     * @param root the root destination folder
     * @param packageName the java package name
     * @param javaClassName the java class name
     * @return the path of the destination file
     */
    private static Path writeSource(StringBuilder s, Path root, String packageName, String javaClassName) throws IOException {
        if (!Files.exists(root)) {
            throw new IllegalArgumentException("The specified root path does not exist: " + root);
        } else if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("The specified root path is not a directory: " + root);
        }

        // Create the directory for the class
        Path dir = root;
        for (String ss : packageName.split("\\.")) {
            dir = dir.resolve(ss);
        }
        Files.createDirectories(dir);

        // Create the file
        Path file = dir.resolve(javaClassName);

        final ByteArrayOutputStream srcOS = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(srcOS, StandardCharsets.US_ASCII), true);
        pw.append(s).flush();
        byte[] bytes = srcOS.toByteArray();

        if (Files.exists(file)) {
            byte[] previous = Files.readAllBytes(file);
            if (Arrays.equals(bytes, previous)) {
                return null;
            }
        }
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(file))) {
            os.write(bytes);
        }
        return file;
    }
}
