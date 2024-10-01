/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
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
package io.helidon.messaging.connectors.jms.shim;

import jakarta.jms.JMSException;
import jakarta.jms.ServerSession;
import jakarta.jms.Session;

import static io.helidon.messaging.connectors.jms.shim.ShimUtil.call;
import static io.helidon.messaging.connectors.jms.shim.ShimUtil.run;

/**
 * Exposes Jakarta API, delegates to javax API.
 */
class JakartaServerSession implements ServerSession {
    private final javax.jms.ServerSession delegate;

    JakartaServerSession(javax.jms.ServerSession delegate) {
        this.delegate = delegate;
    }

    @Override
    public Session getSession() throws JMSException {
        return JakartaJms.create(call(delegate::getSession));
    }

    @Override
    public void start() throws JMSException {
        run(delegate::start);
    }
}
