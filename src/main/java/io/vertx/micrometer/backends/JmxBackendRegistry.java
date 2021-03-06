/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertx.micrometer.backends;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.jmx.JmxMeterRegistry;
import io.vertx.micrometer.VertxJmxMetricsOptions;

/**
 * @author Joel Takvorian
 */
public final class JmxBackendRegistry implements BackendRegistry {
  private final JmxMeterRegistry registry;

  public JmxBackendRegistry(VertxJmxMetricsOptions options) {
    registry = new JmxMeterRegistry(options.toMicrometerConfig(), Clock.SYSTEM);
  }

  @Override
  public MeterRegistry getMeterRegistry() {
    return registry;
  }

  @Override
  public void init() {
  }

  @Override
  public void close() {
    registry.stop();
  }
}
