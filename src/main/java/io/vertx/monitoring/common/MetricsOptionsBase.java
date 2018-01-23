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
package io.vertx.monitoring.common;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.core.metrics.MetricsOptions;

import java.util.EnumSet;
import java.util.Set;

/**
 * Base options for metrics reporting.
 *
 * @author Thomas Segismont
 */
@DataObject(generateConverter = true, inheritConverter = true)
public abstract class MetricsOptionsBase extends MetricsOptions {
  private Set<MetricsCategory> disabledMetricsCategories;

  public MetricsOptionsBase() {
    disabledMetricsCategories = EnumSet.noneOf(MetricsCategory.class);
  }

  public MetricsOptionsBase(MetricsOptionsBase other) {
    super(other);
    disabledMetricsCategories = other.disabledMetricsCategories != null ? EnumSet.copyOf(other.disabledMetricsCategories) : EnumSet.noneOf(MetricsCategory.class);
  }

  public MetricsOptionsBase(JsonObject json) {
    this();
    MetricsOptionsBaseConverter.fromJson(json, this);
  }

  /**
   * Set whether metrics will be enabled on the Vert.x instance. Metrics are not enabled by default.
   */
  @Override
  public MetricsOptionsBase setEnabled(boolean enable) {
    super.setEnabled(enable);
    return this;
  }

  /**
   * @return the disabled metrics types.
   */
  public Set<MetricsCategory> getDisabledMetricsCategories() {
    return disabledMetricsCategories;
  }

  /**
   * Sets metrics types that are disabled.
   *
   * @param disabledMetricsCategories to specify the set of metrics types to be disabled.
   * @return a reference to this, so that the API can be used fluently
   */
  public MetricsOptionsBase setDisabledMetricsCategories(Set<MetricsCategory> disabledMetricsCategories) {
    this.disabledMetricsCategories = disabledMetricsCategories;
    return this;
  }

  /**
   * Set metric that will not be registered. Schedulers will check the set {@code disabledMetricsCategories} when
   * registering metrics suppliers
   *
   * @param metricsCategory the type of metrics
   * @return a reference to this, so that the API can be used fluently
   */
  @GenIgnore
  public MetricsOptionsBase addDisabledMetricsCategory(MetricsCategory metricsCategory) {
    if (disabledMetricsCategories == null) {
      disabledMetricsCategories = EnumSet.noneOf(MetricsCategory.class);
    }
    this.disabledMetricsCategories.add(metricsCategory);
    return this;
  }

  @GenIgnore
  public boolean isMetricsCategoryDisabled(MetricsCategory metricsCategory) {
    return disabledMetricsCategories != null && disabledMetricsCategories.contains(metricsCategory);
  }
}
