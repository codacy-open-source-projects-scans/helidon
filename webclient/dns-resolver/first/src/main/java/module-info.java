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


/**
 * Helidon WebClient DNS Resolver First Support.
 * This implementation uses the first address from DNS lookup.
 */
module io.helidon.webclient.dns.resolver.first {

    requires transitive io.helidon.webclient.api;

    exports io.helidon.webclient.dns.resolver.first;

    provides io.helidon.webclient.spi.DnsResolverProvider
            with io.helidon.webclient.dns.resolver.first.FirstDnsResolverProvider;

}