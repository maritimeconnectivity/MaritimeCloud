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
package net.maritimecloud.internal.message.text.json;

import org.junit.Ignore;

/**
 *
 * @author Kasper Nielsen
 */
@Ignore
public class JSONMessageWriterTest {

    // StringWriter sw;
    //
    // MessageWriter newW() {
    // sw = new StringWriter();
    // return new JSONNewMessageWriter(new PrintWriter(sw));
    // }
    //
    // @Test
    // public void simpleTypes() throws IOException {
    // newW().writeBoolean(1000, "foo", true);
    // assertEquals("\"foo\": true", sw.toString());
    //
    // newW().writeBoolean(1000, "foo", false);
    // assertEquals("\"foo\": false", sw.toString());
    //
    // newW().writeDouble(1, "aa", 232.23);
    // assertEquals("\"aa\": 232.23", sw.toString());
    //
    // newW().writeFloat(1, "ba", -231.23f);
    // assertEquals("\"ba\": -231.23", sw.toString());
    //
    // newW().writeInt(1, "ba", Integer.MAX_VALUE);
    // assertEquals("\"ba\": 2147483647", sw.toString());
    //
    // newW().writeInt64(1, "ba", Long.MIN_VALUE);
    // assertEquals("\"ba\": -9223372036854775808", sw.toString());
    //
    // newW().writeText(1000, "foo", "boo");
    // assertEquals("\"foo\": \"boo\"", sw.toString());
    //
    // // enum
    // }
    //
    // @Test
    // public void messageType() throws IOException {
    // newW().writeMessage(1, "t", TestSerializers.intOf(1));
    // assertEquals(linesOf("\"t\": {", "  \"ivalue\": 1", "}"), sw.toString());
    // }
    //
    // @Test
    // public void binaryTypes() throws IOException {
    // newW().writeBinary(1, "bin", "foobar".getBytes());
    // assertEquals("\"bin\": \"Zm9vYmFy\"", sw.toString());
    // }
    //
    // @Test
    // public void collectionTypes() throws IOException {
    // newW().writeList(1, "list", Arrays.asList(TestSerializers.intOf(1)));
    // assertEquals(linesOf("\"list\": [", "  {", "    \"ivalue\": 1", "  }", "]"), sw.toString());
    //
    // newW().writeList(1, "list", Arrays.asList(TestSerializers.intOf(1), TestSerializers.intOf(4)));
    // assertEquals(
    // linesOf("\"list\": [", "  {", "    \"ivalue\": 1", "  },", "  {", "    \"ivalue\": 4", "  }", "]"),
    // sw.toString());
    //
    // newW().writeSet(1, "set", new HashSet<>(Arrays.asList(TestSerializers.intOf(1))));
    // assertEquals(linesOf("\"set\": [", "  {", "    \"ivalue\": 1", "  }", "]"), sw.toString());
    //
    // // map
    // }
    //
    // static String linesOf(String... lines) {
    // StringWriter sw = new StringWriter();
    // PrintWriter pw = new PrintWriter(sw);
    // for (int i = 0; i < lines.length; i++) {
    // if (i == lines.length - 1) {
    // pw.print(lines[i]);
    // } else {
    // pw.println(lines[i]);
    // }
    // }
    // return sw.toString();
    // }
    //
    // @Test
    // public void simpleStrings() throws IOException {
    // StringWriter sw = new StringWriter();
    // // try (MessageWriter w = new JsonMessageWriter(new PrintWriter(sw))) {
    // // w.writeText(1000, "foo", "boo");
    // // }
    // assertEquals("\"foo\": \"boo\"", sw.toString());
    // JsonParser p = Json.createParser(new StringReader("{" + sw.toString() + "}"));
    // assertSame(Event.START_OBJECT, p.next());
    // assertSame(Event.KEY_NAME, p.next());
    // assertEquals("foo", p.getString());
    // assertSame(Event.VALUE_STRING, p.next());
    // assertEquals("boo", p.getString());
    // assertSame(Event.END_OBJECT, p.next());
    // }
}
