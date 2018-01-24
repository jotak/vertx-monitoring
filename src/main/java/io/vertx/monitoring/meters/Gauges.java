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
package io.vertx.monitoring.meters;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.vertx.monitoring.Labels;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Joel Takvorian
 */
public class Gauges<T> {
  private final String name;
  private final String description;
  private final String[] keys;
  private final Supplier<T> tSupplier;
  private final ToDoubleFunction<T> dGetter;
  private final MeterRegistry registry;
  private final Map<Labels.Values, T> gauges = new ConcurrentHashMap<>();

  public Gauges(String name,
                String description,
                Supplier<T> tSupplier,
                ToDoubleFunction<T> dGetter,
                MeterRegistry registry,
                String... keys) {
    this.name = name;
    this.description = description;
    this.tSupplier = tSupplier;
    this.dGetter = dGetter;
    this.registry = registry;
    this.keys = keys;
  }

  public T get(String... values) {
    return gauges.computeIfAbsent(new Labels.Values(values), v -> {
      // Create a new Gauge for this handler
      T t = tSupplier.get();
      List<Tag> tags = IntStream.range(0, Math.min(keys.length, values.length))
        .mapToObj(n -> Tag.of(keys[n], values[n]))
        .collect(Collectors.toList());
      Gauge.builder(name, t, dGetter)
        .description(description)
        .tags(tags)
        .register(registry);
      return t;
    });
  }

  public static Gauges<LongAdder> longGauges(String name, String description, MeterRegistry registry, String... keys) {
    return new Gauges<>(name, description, LongAdder::new, LongAdder::doubleValue, registry, keys);
  }

  public static Gauges<AtomicReference<Double>> doubleGauges(String name, String description, MeterRegistry registry, String... keys) {
    return new Gauges<>(name, description, () -> new AtomicReference<>(0d), AtomicReference::get, registry, keys);
  }
}
