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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

/**
 *
 * @author Kasper Nielsen
 */
public class SecurityTools {


    public static Signature newSignature(PrivateKey key) {
        Signature dsa = SecurityTools.newSignature();
        try {
            dsa.initSign(key);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("The specified private key is invalid", e);
        }
        return dsa;
    }

    public static Signature newSignature() {
        try {
            return Signature.getInstance("SHA512withECDSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA512withECDSA should be installed on all platform", e);
        }
    }


}
