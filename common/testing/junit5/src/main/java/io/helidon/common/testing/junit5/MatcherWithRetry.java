/*
 * Copyright (c) 2022, 2024 Oracle and/or its affiliates.
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
package io.helidon.common.testing.junit5;

import java.util.function.Supplier;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Hamcrest matcher capable of configured retries before failing the assertion, plus more generic retry processing.
 */
public class MatcherWithRetry {

    private static final int RETRY_COUNT = Integer.getInteger("io.helidon.test.retryCount", 10);
    private static final int RETRY_DELAY_MS = Integer.getInteger("io.helidon.test.retryDelayMs", 500);

    private MatcherWithRetry() {
    }

    /**
     * Checks the matcher, possibly multiple times after configured delays, invoking the supplier of the matched value each time,
     * until either the matcher passes or the maximum retry expires.
     * @param reason explanation of the assertion
     * @param actualSupplier {@code Supplier} that furnishes the value to submit to the matcher
     * @param matcher Hamcrest matcher which evaluates the supplied value
     * @return the supplied value
     * @param <T> type of the supplied value
     */
    public static <T> T assertThatWithRetry(String reason, Supplier<T> actualSupplier, Matcher<? super T> matcher) {
        return assertThatWithRetry(reason, actualSupplier, matcher, RETRY_COUNT, RETRY_DELAY_MS);
    }

    /**
     * Checks the matcher, possibly multiple times after configured delays, invoking the supplier of the matched value each time,
     * until either the matcher passes or the maximum retry expires.
     * @param reason explanation of the assertion
     * @param actualSupplier {@code Supplier} that furnishes the value to submit to the matcher
     * @param matcher Hamcrest matcher which evaluates the supplied value
     * @param retryCount number of times to retry the supplier while it does not give an answer satisfying the matcher
     * @param retryDelayMs delay in milliseconds between retries
     * @return the supplied value
     * @param <T> type of the supplied value
     */
    public static <T> T assertThatWithRetry(String reason,
                                            Supplier<T> actualSupplier,
                                            Matcher<? super T> matcher,
                                            int retryCount,
                                            int retryDelayMs) {

        T actual = null;
        for (int i = 0; i < retryCount; i++) {
            actual = actualSupplier.get();
            if (matcher.matches(actual)) {
                return actual;
            }
            try {
                Thread.sleep(retryDelayMs);
            } catch (InterruptedException e) {
                fail("Error sleeping during assertThatWithRetry", e);
            }
        }

        Description description = new StringDescription();
        description.appendText(reason)
                .appendText("\nExpected: ")
                .appendDescriptionOf(matcher)
                .appendText("\n     but: ");
        matcher.describeMismatch(actual, description);

        throw new AssertionError(description.toString());
    }

    /**
     * Checks the matcher, possibly multiple times after configured delays, invoking the supplier of the matched value each time,
     * until either the matcher passes or the maximum retry expires.
     * @param actualSupplier {@code Supplier} that furnishes the value to submit to the matcher
     * @param matcher Hamcrest matcher which evaluates the supplied value
     * @return the supplied value
     * @param <T> type of the supplied value
     */
    public static <T> T assertThatWithRetry(Supplier<T> actualSupplier, Matcher<? super T> matcher) {
        return assertThatWithRetry("", actualSupplier, matcher);
    }
}
