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

import java.nio.file.Path;

import net.maritimecloud.msdl.model.SourceToken;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author Kasper Nielsen
 */
class SourceTagHolder implements SourceToken {

    final ParsedMsdlFile file;

    final int startCharPosition;

    final int startLine;

    final int stopCharPosition;

    final int stopLine;

    SourceTagHolder(ParsedMsdlFile file, ParserRuleContext c) {
        this.file = requireNonNull(file);
        this.startLine = c.getStart().getLine();
        this.startCharPosition = c.getStart().getCharPositionInLine();
        this.stopLine = c.getStop().getLine();
        this.stopCharPosition = c.getStop().getCharPositionInLine();
    }

    /** {@inheritDoc} */
    @Override
    public int getEndColumn() {
        return stopCharPosition;
    }

    /** {@inheritDoc} */
    @Override
    public int getEndLine() {
        return stopLine;
    }

    /** {@inheritDoc} */
    @Override
    public Path getPath() {
        return file.antlrFile.getPath();
    }

    public String toString() {
        return getPath() + ":[" + startLine + ":" + startCharPosition + "]";
    }

    /** {@inheritDoc} */
    @Override
    public int getStartColumn() {
        return startCharPosition;
    }

    /** {@inheritDoc} */
    @Override
    public int getStartLine() {
        return startLine;
    }
}
