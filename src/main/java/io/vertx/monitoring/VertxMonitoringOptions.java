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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.monitoring.backend.VertxInfluxDbOptions;
import io.vertx.monitoring.backend.VertxPrometheusOptions;

import java.util.EnumSet;
import java.util.Set;

/**
 * Vert.x monitoring configuration.<br/>
 * It is required to set either {@link #influxDbOptions} or {@link #prometheusOptions} (but not both)
 * in order to effectively report metrics.
 *
 * @author Joel Takvorian
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class VertxMonitoringOptions extends MetricsOptions {

  /**
   * Default registry name is 'default'
   */
  public static final String DEFAULT_REGISTRY_NAME = "default";

  /**
   * By default, enables <i>remote</i> label for net/http clients.
   */
  public static final boolean DEFAULT_ENABLE_REMOTE_LABEL_FOR_CLIENTS = true;

  /**
   * By default, disables <i>remote</i> label for net/http servers.
   */
  public static final boolean DEFAULT_ENABLE_REMOTE_LABEL_FOR_SERVERS = false;

  private Set<MetricsCategory> disabledMetricsCategories;
  private String registryName;
  private boolean enableRemoteLabelForClients;
  private boolean enableRemoteLabelForServers;
  private VertxInfluxDbOptions influxDbOptions;
  private VertxPrometheusOptions prometheusOptions;

  public VertxMonitoringOptions() {
    disabledMetricsCategories = EnumSet.noneOf(MetricsCategory.class);
    registryName = DEFAULT_REGISTRY_NAME;
    enableRemoteLabelForClients = DEFAULT_ENABLE_REMOTE_LABEL_FOR_CLIENTS;
    enableRemoteLabelForServers = DEFAULT_ENABLE_REMOTE_LABEL_FOR_SERVERS;
  }

  public VertxMonitoringOptions(VertxMonitoringOptions other) {
    super(other);
    disabledMetricsCategories = other.disabledMetricsCategories != null ? EnumSet.copyOf(other.disabledMetricsCategories) : EnumSet.noneOf(MetricsCategory.class);
    registryName = other.registryName;
    enableRemoteLabelForClients = other.enableRemoteLabelForClients;
    enableRemoteLabelForServers = other.enableRemoteLabelForServers;
    if (other.influxDbOptions != null) {
      influxDbOptions = new VertxInfluxDbOptions(other.influxDbOptions);
    }
    if (other.prometheusOptions != null) {
      prometheusOptions = new VertxPrometheusOptions(other.prometheusOptions);
    }
  }

  public VertxMonitoringOptions(JsonObject json) {
    this();
    VertxMonitoringOptionsConverter.fromJson(json, this);
  }

  /**
   * Set whether metrics will be enabled on the Vert.x instance. Metrics are not enabled by default.
   */
  @Override
  public VertxMonitoringOptions setEnabled(boolean enable) {
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
  public VertxMonitoringOptions setDisabledMetricsCategories(Set<MetricsCategory> disabledMetricsCategories) {
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
  public VertxMonitoringOptions addDisabledMetricsCategory(MetricsCategory metricsCategory) {
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

  public String getRegistryName() {
    return registryName;
  }

  /**
   * Set a name for the prometheus registry, so that a new registry will be created and associated with this name.
   * To retrieve this registry later, call {@code PrometheusRegistries.get(String)}
   * Doing so allows to provide application-defined metrics to the same registry.
   * If {@code registryName} is not provided (or null), Prometheus default registry will be used.
   * @param registryName a name to uniquely identify this registry
   */
  public VertxMonitoringOptions setRegistryName(String registryName) {
    this.registryName = registryName;
    return this;
  }

  public boolean isEnableRemoteLabelForClients() {
    return enableRemoteLabelForClients;
  }

  /**
   * Set false to prevent generation of a label named "remote" on client-related metrics, used to group data points per remote.
   * This is relevant when the application makes client connections to a large number of different clients,
   * in order to reduce the number of related prometheus metrics created.<br/>
   * This option is set to <i>true</i> by default.
   */
  public VertxMonitoringOptions setEnableRemoteLabelForClients(boolean enableRemoteLabelForClients) {
    this.enableRemoteLabelForClients = enableRemoteLabelForClients;
    return this;
  }

  public boolean isEnableRemoteLabelForServers() {
    return enableRemoteLabelForServers;
  }

  /**
   * Set true to allow generation of a label named "remote" on server-related metrics, used to group data points per remote.
   * This is relevant when the number of clients connecting to the application servers is small and under control,
   * in order to reduce the number of related prometheus metrics created.<br/>
   * This option is set to <i>false</i> by default.
   */
  public VertxMonitoringOptions setEnableRemoteLabelForServers(boolean enableRemoteLabelForServers) {
    this.enableRemoteLabelForServers = enableRemoteLabelForServers;
    return this;
  }

  public VertxInfluxDbOptions getInfluxDbOptions() {
    return influxDbOptions;
  }

  /**
   * Set InfluxDB options.
   * Setting backend options is mandatory in order to effectively report metrics.
   * @param influxDbOptions backend options for InfluxDB
   */
  public VertxMonitoringOptions setInfluxDbOptions(VertxInfluxDbOptions influxDbOptions) {
    this.influxDbOptions = influxDbOptions;
    return this;
  }

  public VertxPrometheusOptions getPrometheusOptions() {
    return prometheusOptions;
  }

  /**
   * Set Prometheus options.
   * Setting backend options is mandatory in order to effectively report metrics.
   * @param prometheusOptions backend options for Prometheus
   */
  public VertxMonitoringOptions setPrometheusOptions(VertxPrometheusOptions prometheusOptions) {
    this.prometheusOptions = prometheusOptions;
    return this;
  }
}
