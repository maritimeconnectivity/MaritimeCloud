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
package net.maritimecloud.mms.server.security.impl;

import com.typesafe.config.Config;
import net.maritimecloud.mms.server.security.AuthenticationException;
import net.maritimecloud.mms.server.security.AuthenticationHandler;
import net.maritimecloud.mms.server.security.AuthenticationToken;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class implements support for an Apache webserver-style configuration with
 * authentication against a htpasswd file.
 *
 * The class implements the {@code AuthenticationHandler} interface and attempts
 * to authenticate the client from an Apache htpasswd-style credentials file.
 * <p/>
 * The authentication token must be of type {@code UsernamePasswordToken}.
 *
 * <p>The security configuration file must specify the "htpasswd-file" attribute, pointing
 * to an Apache htpasswd-style credentials file</p>
 */
@SuppressWarnings("unused")
public class ApacheConfSecurityHandler implements AuthenticationHandler {

    private final static Pattern HTPASSWD_ENTRY = Pattern.compile("^([^:]+):(.+)");

    private Config conf;

    /** User + encrypted passwords loaded from Apache htpasswd-style file */
    private Map<String, String> userPasswords = new HashMap<>();

    /** Last modified timestamp of the htpasswd file */
    private long htpasswdFileLastModified = -1L;

    /** {@inheritDoc} */
    @Override
    public void init(Config conf) {
        this.conf = conf;
    }

    /** {@inheritDoc} */
    @Override
    public Config getConf() {
        return conf;
    }

    /*************************************************/
    /** Authentication Support                      **/
    /*************************************************/

    /** {@inheritDoc} */
    @Override
    public void authenticate(AuthenticationToken token) throws AuthenticationException {

        if (token == null || !(token instanceof UsernamePasswordToken)) {
            throw new AuthenticationException("Invalid authentication token: " + token);
        }
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;

        try {
            String htpasswdFile = getConf().getString("htpasswd-file");

            if (!authenticate(usernamePasswordToken, new File(htpasswdFile))) {
                throw new AuthenticationException("Invalid username-password");
            }
        } catch (IOException e) {
            throw new AuthenticationException("Error executing htpasswd authentication", e);
        }
    }

    /**
     * Check if the credentials are valid according to the Apache htpasswd-style credentials file
     *
     * @param token the credentials to check
     * @param htpasswdFile the htpasswd file
     * @return if the credentials are valid
     */
    protected boolean authenticate(UsernamePasswordToken token, File htpasswdFile) throws IOException {
        Objects.requireNonNull(token);
        Objects.requireNonNull(htpasswdFile);

        // Read in the htpasswd file
        checkReadHtpasswdFile(htpasswdFile);

        String storedPwd = userPasswords.get(token.getUsername());
        if (storedPwd != null) {
            final String passwd = new String(token.getPassword());

            // test Apache MD5 variant encrypted password
            if (storedPwd.startsWith("$apr1$")) {
                return storedPwd.equals(Md5Crypt.apr1Crypt(passwd, storedPwd));
            }

            // test unsalted SHA password
            else if (storedPwd.startsWith("{SHA}")) {
                String passwd64 = org.apache.commons.codec.binary.Base64.encodeBase64String(DigestUtils.sha1(passwd));
                return  storedPwd.substring("{SHA}".length()).equals(passwd64);
            }

            // test libc crypt() encoded password
            else if (storedPwd.equals(Crypt.crypt(passwd, storedPwd))) {
                return true;
            }

            // test clear text
            else if (storedPwd.equals(passwd)) {
                return true;
            }
        }

        // Not authenticated
        return false;
    }

    /**
     * Check if the htpasswd file has been updated since last time it was read.
     * Reads in all users + encrypted password.
     *
     * @param htpasswdFile the password file
     */
    protected synchronized void checkReadHtpasswdFile(File htpasswdFile) throws IOException {
        if (!htpasswdFile.exists()) {
            throw new IOException("File does not exist: " + htpasswdFile);
        }

        // Only read it, if the file has been updated ... and the first time called.
        if (htpasswdFile.lastModified() != htpasswdFileLastModified) {

            userPasswords.clear();
            try (Scanner scanner = new Scanner(new FileInputStream(htpasswdFile))) {
                while( scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if ( !line.isEmpty() &&  !line.startsWith("#") ) {
                        Matcher m = HTPASSWD_ENTRY.matcher(line);
                        if ( m.matches() ) {
                            userPasswords.put(m.group(1), m.group(2));
                        }
                    }
                }
            }

            // Record time stamp
            htpasswdFileLastModified = htpasswdFile.lastModified();
        }
    }
}
