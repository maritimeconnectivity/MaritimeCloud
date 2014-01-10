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
package net.maritimecloud.internal.net.server.rest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * @author Kasper Nielsen
 */
public class JSONObjectTest {
    public static final String LS = "\n";

    @Test
    public void singles() {
        chec(new JSONObject());
        chec(JSONObject.single("foo", "bar"), "  \"foo\":\"bar\"");
        chec(JSONObject.single("foo", 123), "  \"foo\":123");
        chec(JSONObject.single("foo", null), "  \"foo\":null");
    }

    @Test
    public void singles2() {
        JSONObject o = new JSONObject();
        o.addElement("foo", "boo");
        o.addElement("foo", 1234);
        o.addElement("fvv", null);
        chec(o, "  \"foo\":\"boo\",",//
                "  \"foo\":1234,", //
                "  \"fvv\":null");
    }

    @Test
    public void children() {
        JSONObject o = new JSONObject();
        o.addElement("foo", "boo");
        o.addElement("foo", 1234);
        o.addElement("fvv", null);

        JSONObject r = new JSONObject();
        r.addElement("fff", o);
        r.addElement("fff", o);

        chec(r, "  \"fff\":{",//
                "    \"foo\":\"boo\",",//
                "    \"foo\":1234,",//
                "    \"fvv\":null",//
                "  },",//
                "  \"fff\":{",//
                "    \"foo\":\"boo\",",//
                "    \"foo\":1234,",//
                "    \"fvv\":null",//
                "  }");
    }

    @Test
    public void list() {
        JSONObject o = new JSONObject();
        o.addList("foo", 1234, "noo", null);

        chec(o, "  \"foo\":[",//
                "    1234,",//
                "    \"noo\",",//
                "    null",//
                "  ]");
    }

    @Test
    public void list2() {
        JSONObject o = new JSONObject();
        o.addList("foo", Arrays.asList(1234, "noo", null));

        chec(o, "  \"foo\":[",//
                "    [",//
                "      1234,",//
                "      \"noo\",",//
                "      null",//
                "    ]",//
                "  ]");
    }

    @Test
    public void objectInList() {
        JSONObject o = new JSONObject();
        o.addElement("foo", "boo");
        o.addElement("foo", 1234);
        o.addElement("fvv", null);

        JSONObject c = new JSONObject();
        c.addList("foo", o, o);
        // System.out.println(c);
        o = new JSONObject();
        // System.out.println(o.addList("fff"));
    }

    private void chec(JSONObject o, String... lines) {
        String actual = o.toString();
        String expected = "{" + LS;
        for (String s : lines) {
            // System.out.println(s);
            expected += s + LS;
        }
        expected += "}" + LS;
        assertEquals(expected, actual);
    }
}
