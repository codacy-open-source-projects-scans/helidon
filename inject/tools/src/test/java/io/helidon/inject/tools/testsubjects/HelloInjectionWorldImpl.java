/*
 * Copyright (c) 2023 Oracle and/or its affiliates.
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

package io.helidon.inject.tools.testsubjects;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.helidon.inject.api.RunLevel;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
@RunLevel(0)
@SuppressWarnings("unused")
public class HelloInjectionWorldImpl implements HelloInjectionWorld {

    @Inject
    InjectionWorld world;

    @Inject
    Provider<InjectionWorld> worldRef;

    @Inject
    List<Provider<InjectionWorld>> listOfWorldRefs;

    @Inject
    List<InjectionWorld> listOfWorlds;

    @Inject @Named("red")
    Optional<InjectionWorld> redWorld;

    private InjectionWorld setWorld;

    int postConstructCallCount;
    int preDestroyCallCount;

    @Inject
    HelloInjectionWorldImpl(InjectionWorld injectionWorld) {
        Objects.requireNonNull(injectionWorld);
    }

    @Override
    public String sayHello() {
        assert(postConstructCallCount == 1);
        assert(preDestroyCallCount == 0);
        assert(world == worldRef.get());
        assert(world == setWorld);
        assert(redWorld.isEmpty());

        return "Hello " + world.name();
    }

    @Inject
    void world(InjectionWorld world) {
        this.setWorld = world;
    }

    @PostConstruct
    public void postConstruct() {
        postConstructCallCount++;
    }

    @PreDestroy
    public void preDestroy() {
        preDestroyCallCount++;
    }

    public int postConstructCallCount() {
        return postConstructCallCount;
    }

    public int preDestroyCallCount() {
        return preDestroyCallCount;
    }

}
