/*
 * Copyright 2010-2013, CloudBees Inc.
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
package com.cloudbees.syslog.util;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CachingReferenceTest {

    /**
     * Test that the locks are properly released.
     */
    @Test
    void test_return_value() {
        CachingReference<String> cachingReference = new CachingReference<>(5, TimeUnit.SECONDS) {
            @Nullable
            @Override
            protected String newObject() {
                return "value";
            }
        };

        String actual = cachingReference.get();
        assertThat(actual, equalTo("value"));
    }

    /**
     * Test that the locks are properly released.
     */
    @Test
    void test_throw_exception_in_get_object() {
        CachingReference<String> cachingReference = new CachingReference<>(5, TimeUnit.SECONDS) {
            @Nullable
            @Override
            protected String newObject() {
                throw new MyRuntimeException();
            }
        };
        assertThrows(MyRuntimeException.class, cachingReference::get);
    }

    private static class MyRuntimeException extends RuntimeException {
        public MyRuntimeException() {
            super();
        }

        public MyRuntimeException(Throwable cause) {
            super(cause);
        }

        public MyRuntimeException(String message) {
            super(message);
        }

        public MyRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }

        protected MyRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}