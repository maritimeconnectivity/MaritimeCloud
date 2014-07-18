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
package net.maritimecloud.internal.msdl.parser;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author Kasper Nielsen
 */
public class Checks {

    public static boolean checkFirstLower(ParsedFile file, ParserRuleContext c, String prefix, String name) {
        if (name == null || name.length() == 0 || !isAsciiAlphaLower(name.charAt(0))) {
            file.error(c, prefix + ", must start with an lowercase ascii character (a-z), was '" + name + "'");
            return false;
        }
        return true;
    }


    public static boolean checkFirstUpper(ParsedFile file, ParserRuleContext c, String prefix, String name) {
        if (name == null || name.length() == 0 || !isAsciiAlphaUpper(name.charAt(0))) {
            file.error(c, prefix + ", must start with an uppercase ascii character (A-Z), was '" + name + "'");
            return false;
        }
        return true;
    }

    public static boolean checkAsciiNumber(ParsedFile file, ParserRuleContext c, String prefix, String name) {
        if (!name.matches("[A-Z][a-zA-Z0-9_]*")) {
            file.error(c, prefix
                    + ", must consists only of ascii characters (a-zA-Z), numbers (1-9) and underscore (_) , was '"
                    + name + "'");
            return false;
        }
        return true;
    }

    public static boolean checkAsciiUpperLowercase(ParsedFile file, ParserRuleContext c, String prefix, String name) {
        if (!name.matches("[A-Z][A-Z0-9_]*")) {
            file.error(
                    c,
                    prefix
                            + ", must consists only of uppercase ascii characters (A-Z), numbers (1-9) and underscore (_) , was '"
                            + name + "'");
            return false;
        }
        return true;
    }

    private static boolean isAsciiAlphaLower(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    private static boolean isAsciiAlphaUpper(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }
}
