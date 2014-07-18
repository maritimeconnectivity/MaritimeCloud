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

import static java.util.Objects.requireNonNull;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author Kasper Nielsen
 */
class SourceTagHolder {

    final int startLine;

    final int startCharPosition;

    final int stopLine;

    final int stopCharPosition;

    final ParsedFile file;

    SourceTagHolder(ParsedFile file, ParserRuleContext c) {
        this.file = requireNonNull(file);
        this.startLine = c.getStart().getLine();
        this.startCharPosition = c.getStart().getCharPositionInLine();
        this.stopLine = c.getStop().getLine();
        this.stopCharPosition = c.getStop().getCharPositionInLine();
    }

    public String getPrefix() {
        return file.antlrFile.getPath() + ":[" + startLine + ":" + stopLine + "] ";
    }
}
