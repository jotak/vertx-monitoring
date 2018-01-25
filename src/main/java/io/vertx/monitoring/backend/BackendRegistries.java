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
package io.vertx.monitoring.backend;

import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.core.Vertx;
import io.vertx.monitoring.VertxMonitoringOptions;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Joel Takvorian
 */
public final class BackendRegistries {
  private static final Map<String, BackendRegistry> REGISTRIES = new ConcurrentHashMap<>();

  private BackendRegistries() {
  }

  public static BackendRegistry setupBackend(Vertx vertx, VertxMonitoringOptions options) {
    return REGISTRIES.computeIfAbsent(options.getRegistryName(), k -> {
      final BackendRegistry reg;
      if (options instanceof VertxInfluxDbOptions) {
        reg = new InfluxDbBackendRegistry((VertxInfluxDbOptions) options);
      } else if (options instanceof VertxPrometheusOptions) {
        reg = new PrometheusBackendRegistry(vertx, (VertxPrometheusOptions) options);
      } else {
        // Unknown/no backend setup, use global registry
        reg = NoopBackendRegistry.INSTANCE;
      }
      return reg;
    });
  }

  public static Optional<MeterRegistry> getNow(String registryName) {
    return Optional.ofNullable(REGISTRIES.get(registryName))
      .map(BackendRegistry::getMeterRegistry);
  }

  public static void stop(String registryName) {
    BackendRegistry reg = REGISTRIES.remove(registryName);
    if (reg != null) {
      reg.close();
    }
  }
}
