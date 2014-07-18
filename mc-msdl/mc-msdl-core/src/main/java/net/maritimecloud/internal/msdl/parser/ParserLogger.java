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

import net.maritimecloud.msdl.MsdlLogger;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author Kasper Nielsen
 */
class ParserLogger extends MsdlLogger {
    private MsdlLogger delegate;

    /** {@inheritDoc} */
    public void debug(CharSequence message) {
        delegate.debug(message);
    }

    /** {@inheritDoc} */
    public void debug(CharSequence message, Throwable error) {
        delegate.debug(message, error);
    }

    /** {@inheritDoc} */
    public void error(CharSequence message) {
        delegate.error(message);
    }

    /** {@inheritDoc} */
    public void error(CharSequence message, Throwable error) {
        delegate.error(message, error);
    }

    /** {@inheritDoc} */
    public void info(CharSequence message) {
        delegate.info(message);
    }

    /** {@inheritDoc} */
    public void info(CharSequence message, Throwable error) {
        delegate.info(message, error);
    }

    /** {@inheritDoc} */
    public void warn(CharSequence message) {
        delegate.warn(message);
    }

    /** {@inheritDoc} */
    public void warn(CharSequence message, Throwable error) {
        delegate.warn(message, error);
    }

    void error(ParsedFile file, ParserRuleContext context, String msg) {

    }
}
