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
package net.maritimecloud.internal.msdl.parser.antlr;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlLexer;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser;
import net.maritimecloud.internal.msdl.parser.antlr.generated.MsdlParser.CompilationUnitContext;
import net.maritimecloud.msdl.MsdlLogger;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 *
 * @author Kasper Nielsen
 */
public class AntlrFile {
    CompilationUnitContext context;

    final ANTLRInputStream input;

    final MsdlLexer lexer;

    final MsdlLogger logger;

    final MsdlParser parser;

    final Path path;

    final CommonTokenStream tokenStream;

    public AntlrFile(Path path, MsdlLogger logger) throws IOException {
        this.path = path;
        this.input = new ANTLRInputStream(Files.newInputStream(path));
        this.lexer = new MsdlLexer(input);
        this.tokenStream = new CommonTokenStream(lexer);
        this.parser = new MsdlParser(tokenStream);
        this.logger = requireNonNull(logger);

        // Better errors
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(new VerboseListener());
        parser.addErrorListener(new VerboseListener());
    }

    public AntlrFile(URL url, MsdlLogger logger) throws IOException {
        this.path = null;
        this.input = new ANTLRInputStream(url.openStream());
        this.lexer = new MsdlLexer(input);
        this.tokenStream = new CommonTokenStream(lexer);
        this.parser = new MsdlParser(tokenStream);
        this.logger = requireNonNull(logger);

        // Better errors
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(new VerboseListener());
        parser.addErrorListener(new VerboseListener());
    }

    public CompilationUnitContext getCompilationUnit() {
        CompilationUnitContext context = this.context;
        return context == null ? this.context = parser.compilationUnit() : context;
    }

    /**
     * @return the lexer
     */
    public MsdlLexer getLexer() {
        return lexer;
    }

    /**
     * @return the parser
     */
    public MsdlParser getParser() {
        return parser;
    }

    /**
     * @return the path
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return the tokenStream
     */
    public CommonTokenStream getTokenStream() {
        return tokenStream;
    }

    class VerboseListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                String msg, RecognitionException e) {
            // [ERROR]
            // /Users/kasperni/dma-workspace/MaritimeCloud/mc-msdl/mc-msdl-core/src/main/java/net/maritimecloud/msdl/MsdlPlugin.java:[33,29]
            // ';' expected
            // [ERROR]
            // /Users/kasperni/dma-workspace/MaritimeCloud/mc-msdl/mc-msdl-core/src/main/java/net/maritimecloud/msdl/MsdlPlugin.java:[33,30]
            // invalid method declaration; return type required

            logger.error(path + ":[" + line + ":" + charPositionInLine + "] " + msg);
            // System.err.println("line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": "
            // + sentenceStr);
            // --throw new IllegalArgumentException(msg + " @ character " + charPositionInLine);
            if (recognizer instanceof Parser) {
                List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
                Collections.reverse(stack);
                // System.err.println("rule stack: " + stack);
                // System.err.println("line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": ");
            }
        }
    }
}
