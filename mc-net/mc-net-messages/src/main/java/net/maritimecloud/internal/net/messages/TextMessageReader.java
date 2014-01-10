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
package net.maritimecloud.internal.net.messages;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.ArrayList;

import net.maritimecloud.util.geometry.Area;
import net.maritimecloud.util.geometry.BoundingBox;
import net.maritimecloud.util.geometry.Circle;
import net.maritimecloud.util.geometry.CoordinateSystem;
import net.maritimecloud.util.geometry.Position;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * 
 * @author Kasper Nielsen
 */
public class TextMessageReader {

    private final JsonParser jp;

    public TextMessageReader(String message) throws IOException {
        requireNonNull(message);
        JsonFactory jsonF = new JsonFactory();
        jp = jsonF.createJsonParser(message);
        if (jp.nextToken() != JsonToken.START_ARRAY) {
            throw new IOException("Expected the start of a JSON array, but was '" + jp.getText() + "'");
        }
    }

    public boolean takeBoolean() throws IOException {
        JsonToken t = jp.nextToken();
        if (t != JsonToken.VALUE_FALSE && t != JsonToken.VALUE_TRUE) {
            throw new IOException("Expected a boolean, but was '" + jp.getText() + "'");
        }
        return jp.getBooleanValue();
    }

    public int takeInt() throws IOException {
        if (jp.nextToken() != JsonToken.VALUE_NUMBER_INT) {
            throw new IOException("Expected an integer, but was '" + jp.getText() + "'");
        }
        return jp.getIntValue();
    }

    public long takeLong() throws IOException {
        if (jp.nextToken() != JsonToken.VALUE_NUMBER_INT) {
            throw new IOException("Expected an long, but was '" + jp.getText() + "'");
        }
        return jp.getLongValue();
    }

    public double takeDouble() throws IOException {
        if (jp.nextToken() != JsonToken.VALUE_NUMBER_FLOAT) {
            throw new IOException("Expected an double, but was '" + jp.getText() + "'");
        }
        return jp.getDoubleValue();
    }

    public Position takePosition() throws IOException {
        double lat = takeDouble();
        double lon = takeDouble();
        return Position.create(lat, lon);
    }

    public Area takeArea() throws IOException {
        int type = takeInt();
        if (type == 0) {
            Position center = takePosition();
            double radius = takeDouble();
            return new Circle(center, radius, CoordinateSystem.CARTESIAN);
        } else if (type == 1) {
            Position topLeft = takePosition();
            Position buttomRight = takePosition();
            return BoundingBox.create(topLeft, buttomRight, CoordinateSystem.CARTESIAN);
        } else {
            throw new UnsupportedOperationException("Only type 0 (circles) and type 1 (bounding boxes) supported, was "
                    + type);
        }
    }

    public String takeString() throws IOException {
        if (jp.nextToken() != JsonToken.VALUE_STRING) {
            throw new IOException("Expected an String, but was '" + jp.getText() + "'");
        }
        return jp.getText();
    }

    public String[] takeStringArray() throws IOException {
        if (jp.nextToken() != JsonToken.START_ARRAY) {
            throw new IOException("Expected an String, but was '" + jp.getText() + "'");
        }
        ArrayList<String> result = new ArrayList<>();
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            result.add(jp.getText());
        }
        return result.toArray(new String[result.size()]);
    }
}
