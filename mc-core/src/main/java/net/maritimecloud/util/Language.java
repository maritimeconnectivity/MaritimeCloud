/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
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
