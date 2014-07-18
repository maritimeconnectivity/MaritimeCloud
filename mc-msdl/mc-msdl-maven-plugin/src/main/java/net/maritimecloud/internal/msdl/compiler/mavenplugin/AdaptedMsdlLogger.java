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
package net.maritimecloud.internal.msdl.compiler.mavenplugin;

import static java.util.Objects.requireNonNull;
import net.maritimecloud.msdl.MsdlLogger;

import org.apache.maven.plugin.logging.Log;

/**
 *
 * @author Kasper Nielsen
 */
class AdaptedMsdlLogger extends MsdlLogger {
    private final Log logger;

    AdaptedMsdlLogger(Log logger) {
        this.logger = requireNonNull(logger);
    }

    /** {@inheritDoc} */
    @Override
    public void debug(CharSequence content) {
        logger.debug(content);
    }

    /** {@inheritDoc} */
    @Override
    public void debug(CharSequence content, Throwable error) {
        logger.debug(content, error);
    }

    /** {@inheritDoc} */
    @Override
    public void error(CharSequence content) {
        logger.error(content);
    }

    /** {@inheritDoc} */
    @Override
    public void error(CharSequence content, Throwable error) {
        logger.error(content, error);
    }

    /** {@inheritDoc} */
    @Override
    public void info(CharSequence content) {
        logger.info(content);
    }

    /** {@inheritDoc} */
    @Override
    public void info(CharSequence content, Throwable error) {
        logger.info(content, error);
    }

    /** {@inheritDoc} */
    @Override
    public void warn(CharSequence content) {
        logger.warn(content);
    }

    /** {@inheritDoc} */
    @Override
    public void warn(CharSequence content, Throwable error) {
        logger.warn(content, error);
    }
}
