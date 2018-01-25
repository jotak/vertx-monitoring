package io.vertx.kotlin.monitoring.backend

import io.vertx.monitoring.backend.VertxPrometheusOptions
import io.vertx.core.http.HttpServerOptions
import io.vertx.monitoring.MetricsCategory

/**
 * A function providing a DSL for building [io.vertx.monitoring.backend.VertxPrometheusOptions] objects.
 *
 * Options for Prometheus metrics backend.
 *
 * @param disabledMetricsCategories  Sets metrics types that are disabled.
 * @param embeddedServerEndpoint  Set metrics endpoint. Use conjointly with the embedded server options.
 * @param embeddedServerOptions  An embedded server will start to expose metrics with Prometheus format
 * @param enableRemoteLabelForClients  Set false to prevent generation of a label named "remote" on client-related metrics, used to group data points per remote. This is relevant when the application makes client connections to a large number of different clients, in order to reduce the number of related prometheus metrics created.<br/> This option is set to <i>true</i> by default.
 * @param enableRemoteLabelForServers  Set true to allow generation of a label named "remote" on server-related metrics, used to group data points per remote. This is relevant when the number of clients connecting to the application servers is small and under control, in order to reduce the number of related prometheus metrics created.<br/> This option is set to <i>false</i> by default.
 * @param enabled  Set whether metrics will be enabled on the Vert.x instance. Metrics are not enabled by default.
 * @param registryName  Set a name for the prometheus registry, so that a new registry will be created and associated with this name. To retrieve this registry later, call <code>PrometheusRegistries.get(String)</code> Doing so allows to provide application-defined metrics to the same registry. If <code>registryName</code> is not provided (or null), Prometheus default registry will be used.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.monitoring.backend.VertxPrometheusOptions original] using Vert.x codegen.
 */
fun VertxPrometheusOptions(
  disabledMetricsCategories: Iterable<MetricsCategory>? = null,
  embeddedServerEndpoint: String? = null,
  embeddedServerOptions: io.vertx.core.http.HttpServerOptions? = null,
  enableRemoteLabelForClients: Boolean? = null,
  enableRemoteLabelForServers: Boolean? = null,
  enabled: Boolean? = null,
  registryName: String? = null): VertxPrometheusOptions = io.vertx.monitoring.backend.VertxPrometheusOptions().apply {

  if (disabledMetricsCategories != null) {
    this.setDisabledMetricsCategories(disabledMetricsCategories.toSet())
  }
  if (embeddedServerEndpoint != null) {
    this.setEmbeddedServerEndpoint(embeddedServerEndpoint)
  }
  if (embeddedServerOptions != null) {
    this.setEmbeddedServerOptions(embeddedServerOptions)
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
  if (registryName != null) {
    this.setRegistryName(registryName)
  }
}

