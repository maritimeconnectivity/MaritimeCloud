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
package net.maritimecloud.internal.security;

/**
 * Bliver added i 0.4
 *
 * @author Kasper Nielsen
 */
public abstract class Credentials {

    // Hmm skal vi have flere af den her slags?
    // AccessKeyCreadentials, virker kun hvis man kun kommunikere direkte med MMS serveren.

    // 3 typer
    // Access keys
    // private + public key (Registeret hos MC cloud)
    // certificate + password

    // Access key credentials kan jo ikke bruges til at signe ting.
    // Okay, hvordan signer vi ting???
    // med certifikatet er det let nok
    // Skal man mon bruge et alias.

    public static final Credentials ANONYMOUS = null;

    static class X509CertificateCredentials extends Credentials {

    }

    // uses symmetric encryption
    static class AccessKeyCredentials extends Credentials {
        private final String accessKeyId;

        private final String secretAccessKey;

        AccessKeyCredentials(String accessKeyId, String secretAccessKey) {
            this.accessKeyId = accessKeyId;
            this.secretAccessKey = secretAccessKey;
        }
    }
}
