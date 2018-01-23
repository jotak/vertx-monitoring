package io.vertx.kotlin.monitoring.influxdb

import io.vertx.monitoring.influxdb.VertxInfluxDbOptions
import io.vertx.core.http.HttpClientOptions
import io.vertx.monitoring.common.MetricsCategory
import io.vertx.monitoring.influxdb.AuthenticationOptions

/**
 * A function providing a DSL for building [io.vertx.monitoring.influxdb.VertxInfluxDbOptions] objects.
 *
 * Vert.x InfluxDb monitoring configuration.
 *
 * @param authenticationOptions  Set the options for authentication.
 * @param batchDelay  Set the maximum delay between two consecutive batches (in seconds). To reduce the number of HTTP exchanges, metric data is sent by the reporter in batches. A batch is sent as soon as the number of metrics collected reaches the configured <code>batchSize</code>, or after the <code>batchDelay</code> expires. Defaults to <code>1</code> second.
 * @param batchSize  Set the maximum number of metrics in a batch. To reduce the number of HTTP exchanges, metric data is sent by the reporter in batches. A batch is sent as soon as the number of metrics collected reaches the configured <code>batchSize</code>, or after the <code>batchDelay</code> expires. Defaults to <code>50</code>.
 * @param database  Set the InfluxDb database. Defaults to <code>default</code>.
 * @param disabledMetricsCategories  Sets metrics types that are disabled.
 * @param enabled  Set whether metrics will be enabled on the Vert.x instance. Metrics are not enabled by default.
 * @param gzipEnabled 
 * @param host  Set the InfluxDb Metrics service host. Defaults to <code>localhost</code>.
 * @param httpHeaders  Set specific headers to include in HTTP requests.
 * @param httpOptions  Set the configuration of the InfluxDb Metrics HTTP client.
 * @param measurement  Set the InfluxDb measurement. Defaults to <code>vert.x</code>.
 * @param metricsBridgeAddress  Sets the metric bridge address on which the application is sending the custom metrics. Application can send metrics to this event bus address. The message is a JSON object specifying at least the <code>id</code> and <code>value</code> fields. <p/> Don't forget to also enable the bridge with <code>metricsBridgeEnabled</code>.
 * @param metricsBridgeEnabled  Sets whether or not the metrics bridge should be enabled. The metrics bridge is disabled by default.
 * @param metricsServiceUri  Set the InfluxDb Metrics service URI. Defaults to <code>/InfluxDb/metrics</code>. This can be useful if you host the InfluxDb server behind a proxy and manipulate the default service URI.
 * @param port  Set the InfluxDb Metrics service port.  Defaults to <code>8080</code>.
 * @param prefix  Set the metric name prefix. Metric names are not prefixed by default. Prefixing metric names is required to distinguish data sent by different Vert.x instances.
 * @param schedule  Set the metric collection interval (in seconds). Defaults to <code>1</code>.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.monitoring.influxdb.VertxInfluxDbOptions original] using Vert.x codegen.
 */
fun VertxInfluxDbOptions(
  authenticationOptions: io.vertx.monitoring.influxdb.AuthenticationOptions? = null,
  batchDelay: Int? = null,
  batchSize: Int? = null,
  database: String? = null,
  disabledMetricsCategories: Iterable<MetricsCategory>? = null,
  enabled: Boolean? = null,
  gzipEnabled: Boolean? = null,
  host: String? = null,
  httpHeaders: io.vertx.core.json.JsonObject? = null,
  httpOptions: io.vertx.core.http.HttpClientOptions? = null,
  measurement: String? = null,
  metricsBridgeAddress: String? = null,
  metricsBridgeEnabled: Boolean? = null,
  metricsServiceUri: String? = null,
  port: Int? = null,
  prefix: String? = null,
  schedule: Int? = null): VertxInfluxDbOptions = io.vertx.monitoring.influxdb.VertxInfluxDbOptions().apply {

  if (authenticationOptions != null) {
    this.setAuthenticationOptions(authenticationOptions)
  }
  if (batchDelay != null) {
    this.setBatchDelay(batchDelay)
  }
  if (batchSize != null) {
    this.setBatchSize(batchSize)
  }
  if (database != null) {
    this.setDatabase(database)
  }
  if (disabledMetricsCategories != null) {
    this.setDisabledMetricsCategories(disabledMetricsCategories.toSet())
  }
  if (enabled != null) {
    this.setEnabled(enabled)
  }
  if (gzipEnabled != null) {
    this.setGzipEnabled(gzipEnabled)
  }
  if (host != null) {
    this.setHost(host)
  }
  if (httpHeaders != null) {
    this.setHttpHeaders(httpHeaders)
  }
  if (httpOptions != null) {
    this.setHttpOptions(httpOptions)
  }
  if (measurement != null) {
    this.setMeasurement(measurement)
  }
  if (metricsBridgeAddress != null) {
    this.setMetricsBridgeAddress(metricsBridgeAddress)
  }
  if (metricsBridgeEnabled != null) {
    this.setMetricsBridgeEnabled(metricsBridgeEnabled)
  }
  if (metricsServiceUri != null) {
    this.setMetricsServiceUri(metricsServiceUri)
  }
  if (port != null) {
    this.setPort(port)
  }
  if (prefix != null) {
    this.setPrefix(prefix)
  }
  if (schedule != null) {
    this.setSchedule(schedule)
  }
}

