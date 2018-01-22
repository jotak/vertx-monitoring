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

package io.vertx.monitoring.collector;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.monitoring.common.MetricsOptionsBase;

/**
 * Common options for reporters sending metrics in batches.
 *
 * @author Thomas Segismont
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class BatchingReporterOptions extends MetricsOptionsBase {

  /**
   * The default metric name prefix (empty).
   */
  public static final String DEFAULT_PREFIX = "";

  /**
   * Default value for metric collection interval (in seconds) = 1.
   */
  public static final int DEFAULT_SCHEDULE = 1;

  /**
   * Default value for the maximum number of metrics in a batch = 50.
   */
  public static final int DEFAULT_BATCH_SIZE = 50;

  /**
   * Default value for the maximum delay between two consecutive batches (in seconds) = 1.
   */
  public static final int DEFAULT_BATCH_DELAY = 1;

  /**
   * Default event bus address where applications can send business-related metrics. The metrics are sent as JSON
   * message containing at least the <code>source</code> and <code>value</code> fields.
   */
  public static final String DEFAULT_METRICS_BRIDGE_ADDRESS = "metrics.bridge";

  /**
   * The default value to enable / disable the metrics bridge. Disable by default.
   */
  public static final boolean DEFAULT_METRICS_BRIDGE_ENABLED = false;

  private String prefix;
  private int schedule;
  private int batchSize;
  private int batchDelay;
  private boolean metricsBridgeEnabled;
  private String metricsBridgeAddress;

  public BatchingReporterOptions() {
    prefix = DEFAULT_PREFIX;
    schedule = DEFAULT_SCHEDULE;
    batchSize = DEFAULT_BATCH_SIZE;
    batchDelay = DEFAULT_BATCH_DELAY;
    metricsBridgeEnabled = DEFAULT_METRICS_BRIDGE_ENABLED;
    metricsBridgeAddress = DEFAULT_METRICS_BRIDGE_ADDRESS;
  }

  public BatchingReporterOptions(BatchingReporterOptions other) {
    super(other);
    prefix = other.prefix;
    schedule = other.schedule;
    batchSize = other.batchSize;
    batchDelay = other.batchDelay;
    metricsBridgeAddress = other.metricsBridgeAddress;
    metricsBridgeEnabled = other.metricsBridgeEnabled;
  }

  public BatchingReporterOptions(JsonObject json) {
    this();
    BatchingReporterOptionsConverter.fromJson(json, this);
  }

  /**
   * @return the metric name prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Set the metric name prefix. Metric names are not prefixed by default. Prefixing metric names is required to
   * distinguish data sent by different Vert.x instances.
   */
  public BatchingReporterOptions setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  /**
   * @return the metric collection interval (in seconds)
   */
  public int getSchedule() {
    return schedule;
  }

  /**
   * Set the metric collection interval (in seconds). Defaults to {@code 1}.
   */
  public BatchingReporterOptions setSchedule(int schedule) {
    this.schedule = schedule;
    return this;
  }

  /**
   * @return the maximum number of metrics in a batch
   */
  public int getBatchSize() {
    return batchSize;
  }

  /**
   * Set the maximum number of metrics in a batch. To reduce the number of HTTP exchanges, metric data is sent by the
   * reporter in batches. A batch is sent as soon as the number of metrics collected reaches the configured
   * {@code batchSize}, or after the {@code batchDelay} expires. Defaults to {@code 50}.
   */
  public BatchingReporterOptions setBatchSize(int batchSize) {
    this.batchSize = batchSize;
    return this;
  }

  /**
   * @return the maximum delay between two consecutive batches
   */
  public int getBatchDelay() {
    return batchDelay;
  }

  /**
   * Set the maximum delay between two consecutive batches (in seconds). To reduce the number of HTTP exchanges, metric
   * data is sent by the reporter in batches. A batch is sent as soon as the number of metrics collected reaches
   * the configured {@code batchSize}, or after the {@code batchDelay} expires. Defaults to {@code 1} second.
   */
  public BatchingReporterOptions setBatchDelay(int batchDelay) {
    this.batchDelay = batchDelay;
    return this;
  }

  /**
   * @return the metric bridge address. If enabled the metric bridge transfers metrics collected from the event bus to
   * the reporter. The metrics are sent as message on the event bus to the return address. The message is a
   * JSON object specifying at least the {@code source} and {@code value} fields ({@code value} is a double).
   */
  public String getMetricsBridgeAddress() {
    return metricsBridgeAddress;
  }

  /**
   * Sets the metric bridge address on which the application is sending the custom metrics. Application can send
   * metrics to this event bus address. The message is a JSON object specifying at least the {@code id} and
   * {@code value} fields.
   * <p/>
   * Don't forget to also enable the bridge with {@code metricsBridgeEnabled}.
   *
   * @param metricsBridgeAddress the address
   * @return a reference to this, so that the API can be used fluently
   */
  public BatchingReporterOptions setMetricsBridgeAddress(String metricsBridgeAddress) {
    this.metricsBridgeAddress = metricsBridgeAddress;
    return this;
  }

  /**
   * Checks whether or not the metrics bridge is enabled.
   *
   * @return {@code true} if the metrics bridge is enabled, {@code false} otherwise.
   */
  public boolean isMetricsBridgeEnabled() {
    return metricsBridgeEnabled;
  }

  /**
   * Sets whether or not the metrics bridge should be enabled. The metrics bridge is disabled by default.
   *
   * @param metricsBridgeEnabled {@code true} to enable the bridge, {@code false} to disable it.
   * @return a reference to this, so that the API can be used fluently
   */
  public BatchingReporterOptions setMetricsBridgeEnabled(boolean metricsBridgeEnabled) {
    this.metricsBridgeEnabled = metricsBridgeEnabled;
    return this;
  }
}
