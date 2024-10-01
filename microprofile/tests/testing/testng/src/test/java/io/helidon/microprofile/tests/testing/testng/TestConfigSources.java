/*
 * Copyright (c) 2022, 2023 Oracle and/or its affiliates.
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

package io.helidon.microprofile.tests.testing.testng;

import io.helidon.microprofile.testing.testng.Configuration;
import io.helidon.microprofile.testing.testng.HelidonTest;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@HelidonTest
@Configuration(configSources = {"testConfigSources.properties", "testConfigSources.yaml"})
public class TestConfigSources {

    @Inject
    @ConfigProperty(name = "some.key")
    private String someKey;

    @Inject
    @ConfigProperty(name = "another.key")
    private String anotherKey;

    @Test
    void testValue() {
        assertThat(someKey, is("some.value"));
        assertThat(anotherKey, is("another.value"));
    }
}
