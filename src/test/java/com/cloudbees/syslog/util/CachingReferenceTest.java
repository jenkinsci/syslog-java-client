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
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class CachingReferenceTest {
    /**
     * Test that the locks are properly released.
     */
    @Test
    public void test_return_value() {

        CachingReference<String> cachingReference = new CachingReference<String>(5, TimeUnit.SECONDS) {
            @Nullable
            @Override
            protected String newObject() {
                return "value";
            }
        };

        String actual = cachingReference.get();
        Assert.assertThat(actual, Matchers.equalTo("value"));
    }

    /**
     * Test that the locks are properly released.
     */
    @Test(expected = MyRuntimeException.class)
    public void test_throw_exception_in_get_object() {

        CachingReference<String> cachingReference = new CachingReference<String>(5, TimeUnit.SECONDS) {
            @Nullable
            @Override
            protected String newObject() {
                throw new MyRuntimeException();
            }
        };

        cachingReference.get();
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