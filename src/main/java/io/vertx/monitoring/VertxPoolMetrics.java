/*
 * Copyright (c) 2011-2017 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.core.spi.metrics.PoolMetrics;
import io.vertx.monitoring.meters.Counters;
import io.vertx.monitoring.meters.Gauges;
import io.vertx.monitoring.meters.Timers;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Joel Takvorian
 */
class VertxPoolMetrics {
  private final Timers queueDelay;
  private final Gauges<LongAdder> queueSize;
  private final Timers usage;
  private final Gauges<LongAdder> inUse;
  private final Gauges<AtomicReference<Double>> usageRatio;
  private final Counters completed;

  VertxPoolMetrics(MeterRegistry registry) {
    queueDelay = new Timers("vertx_pool_queue_delay", "Queue time for a resource",
      registry, "pool_type", "pool_name");
    queueSize = Gauges.longGauges("vertx_pool_queue_size", "Number of elements waiting for a resource",
      registry, "pool_type", "pool_name");
    usage = new Timers("vertx_pool_usage", "Time using a resource",
      registry, "pool_type", "pool_name");
    inUse = Gauges.longGauges("vertx_pool_in_use", "Number of resources used",
      registry, "pool_type", "pool_name");
    usageRatio = Gauges.doubleGauges("vertx_pool_ratio", "Pool usage ratio, only present if maximum pool size could be determined",
      registry, "pool_type", "pool_name", "max_pool_size");
    completed = new Counters("vertx_pool_completed", "Number of elements done with the resource",
      registry, "pool_type", "pool_name");
  }

  PoolMetrics forInstance(String poolType, String poolName, int maxPoolSize) {
    return new Instance(poolType, poolName, maxPoolSize);
  }

  class Instance implements PoolMetrics<Timers.EventTiming> {
    private final String poolType;
    private final String poolName;
    private final int maxPoolSize;

    Instance(String poolType, String poolName, int maxPoolSize) {
      this.poolType = poolType;
      this.poolName = poolName;
      this.maxPoolSize = maxPoolSize;
    }

    @Override
    public Timers.EventTiming submitted() {
      queueSize.get(poolType, poolName).increment();
      return queueDelay.start(poolType, poolName);
    }

    @Override
    public void rejected(Timers.EventTiming submitted) {
      queueSize.get(poolType, poolName).decrement();
      submitted.end();
    }

    @Override
    public Timers.EventTiming begin(Timers.EventTiming submitted) {
      queueSize.get(poolType, poolName).decrement();
      submitted.end();
      LongAdder l = inUse.get(poolType, poolName);
      l.increment();
      checkRatio(l.longValue());
      return usage.start(poolType, poolName);
    }

    @Override
    public void end(Timers.EventTiming begin, boolean succeeded) {
      LongAdder l = inUse.get(poolType, poolName);
      l.decrement();
      checkRatio(l.longValue());
      begin.end();
      completed.get(poolType, poolName).increment();
    }

    @Override
    public boolean isEnabled() {
      return true;
    }

    @Override
    public void close() {
    }

    private void checkRatio(long inUse) {
      if (maxPoolSize > 0) {
        usageRatio.get(poolType, poolName, String.valueOf(maxPoolSize))
          .set((double)inUse / maxPoolSize);
      }
    }
  }
}
