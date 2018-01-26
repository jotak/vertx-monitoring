package io.vertx.kotlin.monitoring

import io.vertx.monitoring.VertxMonitoringOptions
import io.vertx.monitoring.MetricsCategory
import io.vertx.monitoring.backend.VertxInfluxDbOptions
import io.vertx.monitoring.backend.VertxPrometheusOptions

/**
 * A function providing a DSL for building [io.vertx.monitoring.VertxMonitoringOptions] objects.
 *
 * Vert.x monitoring configuration.<br/>
 * It is required to set either [io.vertx.monitoring.VertxMonitoringOptions] or [io.vertx.monitoring.VertxMonitoringOptions] (but not both)
 * in order to effectively report metrics.
 *
 * @param disabledMetricsCategories  Sets metrics types that are disabled.
 * @param enableRemoteLabelForClients  Set false to prevent generation of a label named "remote" on client-related metrics, used to group data points per remote. This is relevant when the application makes client connections to a large number of different clients, in order to reduce the number of related prometheus metrics created.<br/> This option is set to <i>true</i> by default.
 * @param enableRemoteLabelForServers  Set true to allow generation of a label named "remote" on server-related metrics, used to group data points per remote. This is relevant when the number of clients connecting to the application servers is small and under control, in order to reduce the number of related prometheus metrics created.<br/> This option is set to <i>false</i> by default.
 * @param enabled  Set whether metrics will be enabled on the Vert.x instance. Metrics are not enabled by default.
 * @param influxDbOptions  Set InfluxDB options. Setting backend options is mandatory in order to effectively report metrics.
 * @param prometheusOptions  Set Prometheus options. Setting backend options is mandatory in order to effectively report metrics.
 * @param registryName  Set a name for the prometheus registry, so that a new registry will be created and associated with this name. To retrieve this registry later, call <code>PrometheusRegistries.get(String)</code> Doing so allows to provide application-defined metrics to the same registry. If <code>registryName</code> is not provided (or null), Prometheus default registry will be used.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.monitoring.VertxMonitoringOptions original] using Vert.x codegen.
 */
fun VertxMonitoringOptions(
  disabledMetricsCategories: Iterable<MetricsCategory>? = null,
  enableRemoteLabelForClients: Boolean? = null,
  enableRemoteLabelForServers: Boolean? = null,
  enabled: Boolean? = null,
  influxDbOptions: io.vertx.monitoring.backend.VertxInfluxDbOptions? = null,
  prometheusOptions: io.vertx.monitoring.backend.VertxPrometheusOptions? = null,
  registryName: String? = null): VertxMonitoringOptions = io.vertx.monitoring.VertxMonitoringOptions().apply {

  if (disabledMetricsCategories != null) {
    this.setDisabledMetricsCategories(disabledMetricsCategories.toSet())
  }
  if (enableRemoteLabelForClients != null) {
    this.setEnableRemoteLabelForClients(enableRemoteLabelForClients)
  }
  if (enableRemoteLabelForServers != null) {
    this.setEnableRemoteLabelForServers(enableRemoteLabelForServers)
  }
  if (enabled != null) {
    this.setEnabled(enabled)
  }
  if (influxDbOptions != null) {
    this.setInfluxDbOptions(influxDbOptions)
  }
  if (prometheusOptions != null) {
    this.setPrometheusOptions(prometheusOptions)
  }
  if (registryName != null) {
    this.setRegistryName(registryName)
  }
}

