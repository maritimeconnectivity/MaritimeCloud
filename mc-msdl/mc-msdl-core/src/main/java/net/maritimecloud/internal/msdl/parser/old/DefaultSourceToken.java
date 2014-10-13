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
package net.maritimecloud.internal.msdl.parser.old;

import java.nio.file.Path;

import net.maritimecloud.msdl.model.SourceToken;

/**
 *
 * @author Kasper Nielsen
 */
// moved from net.maritimecloud.internal.msdl.parser
public class DefaultSourceToken implements SourceToken {

    private final int endColumn;

    private final int endLine;

    private final Path path;

    private final int startColumn;

    private final int startLine;

    /**
     * @param path
     * @param startColumn
     * @param startLine
     * @param endColumn
     * @param endLine
     */
    public DefaultSourceToken(Path path, int startColumn, int startLine, int endColumn, int endLine) {
        this.path = path;
        this.startColumn = startColumn;
        this.startLine = startLine;
        this.endColumn = endColumn;
        this.endLine = endLine;
    }

    /** {@inheritDoc} */
    @Override
    public int getEndColumn() {
        return endColumn;
    }

    /** {@inheritDoc} */
    @Override
    public int getEndLine() {
        return endLine;
    }

    /** {@inheritDoc} */
    @Override
    public Path getPath() {
        return path;
    }

    /** {@inheritDoc} */
    @Override
    public int getStartColumn() {
        return startColumn;
    }

    /** {@inheritDoc} */
    @Override
    public int getStartLine() {
        return startLine;
    }


    public String toString() {
        return getPath() + ":[" + startLine + ":" + startColumn + "]";
    }
}
