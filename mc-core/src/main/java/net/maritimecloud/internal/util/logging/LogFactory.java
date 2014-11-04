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
package net.maritimecloud.internal.util.logging;

import java.util.function.Function;

import org.slf4j.LoggerFactory;

/**
 *
 * @author Kasper Nielsen
 */
@SuppressWarnings("unchecked")
class LogFactory {
    static final Function<Class<?>, Logger> LOG_FACTORY;

    static {
        if (classExists("org.slf4j.LoggerXXX")) {
            Class<?> cl;
            try {
                cl = Class.forName(LogFactory.class.getName() + "$SLF4JFactory");
                LOG_FACTORY = (Function<Class<?>, Logger>) cl.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new Error(e);
            }
        } else {
            LOG_FACTORY = e -> new JDKLogger(java.util.logging.Logger.getLogger(e.getName()));
        }
    }


    static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    static class SLF4JFactory implements Function<Class<?>, Logger> {

        /** {@inheritDoc} */
        @Override
        public Logger apply(Class<?> t) {
            org.slf4j.Logger l = LoggerFactory.getLogger(t);
            return new SLF4JLogger(l);
        }

    }
}
