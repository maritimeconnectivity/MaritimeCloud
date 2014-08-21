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
package net.maritimecloud.internal.id160;

import java.security.MessageDigest;

import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class Test {

    public static void main(String[] args) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = "This is some text";

        md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        System.out.println(digest.length);
        System.out.println(HexStringUtil.from(digest));


        Binary bin = Binary.copyFromUtf8("This is ");

        bin = bin.concat(Binary.copyFromUtf8("some text"));

        System.out.println(HexStringUtil.from(bin.sha256().toByteArray()));

    }
}
