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
package net.maritimecloud.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class to represent an ISO 639 2-letter language
 */
// TODO make Message
public class Language {

    private final String code;

    private static final Map<String, String> CODE_NAME_MAP = new HashMap<>();

    static {
        for (String code : java.util.Locale.getISOLanguages()) {
            Locale locale = new Locale(code);
            CODE_NAME_MAP.put(code, locale.getDisplayLanguage(locale));
        }
    }

    Language(String code) {
        if (!CODE_NAME_MAP.containsKey(code)) {
            throw new IllegalArgumentException("Unknown language code: " + code);
        }
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Map<String, String> getCodeMap() {
        return Collections.unmodifiableMap(CODE_NAME_MAP);
    }

}
