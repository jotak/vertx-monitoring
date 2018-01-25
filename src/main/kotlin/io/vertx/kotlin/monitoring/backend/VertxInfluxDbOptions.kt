package io.vertx.kotlin.monitoring.backend

import io.vertx.monitoring.backend.VertxInfluxDbOptions
import io.vertx.monitoring.MetricsCategory

/**
 * A function providing a DSL for building [io.vertx.monitoring.backend.VertxInfluxDbOptions] objects.
 *
 * Vert.x InfluxDb monitoring configuration.
 *
 * @param batchSize 
 * @param compressed 
 * @param connectTimeout 
 * @param db 
 * @param disabledMetricsCategories  Sets metrics types that are disabled.
 * @param enableRemoteLabelForClients  Set false to prevent generation of a label named "remote" on client-related metrics, used to group data points per remote. This is relevant when the application makes client connections to a large number of different clients, in order to reduce the number of related prometheus metrics created.<br/> This option is set to <i>true</i> by default.
 * @param enableRemoteLabelForServers  Set true to allow generation of a label named "remote" on server-related metrics, used to group data points per remote. This is relevant when the number of clients connecting to the application servers is small and under control, in order to reduce the number of related prometheus metrics created.<br/> This option is set to <i>false</i> by default.
 * @param enabled  Set whether metrics will be enabled on the Vert.x instance. Metrics are not enabled by default.
 * @param numThreads 
 * @param password 
 * @param prefix 
 * @param readTimeout 
 * @param registryName  Set a name for the prometheus registry, so that a new registry will be created and associated with this name. To retrieve this registry later, call <code>PrometheusRegistries.get(String)</code> Doing so allows to provide application-defined metrics to the same registry. If <code>registryName</code> is not provided (or null), Prometheus default registry will be used.
 * @param retentionPolicy 
 * @param step 
 * @param uri 
 * @param userName 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.monitoring.backend.VertxInfluxDbOptions original] using Vert.x codegen.
 */
fun VertxInfluxDbOptions(
  batchSize: Int? = null,
  compressed: Boolean? = null,
  connectTimeout: Int? = null,
  db: String? = null,
  disabledMetricsCategories: Iterable<MetricsCategory>? = null,
  enableRemoteLabelForClients: Boolean? = null,
  enableRemoteLabelForServers: Boolean? = null,
  enabled: Boolean? = null,
  numThreads: Int? = null,
  password: String? = null,
  prefix: String? = null,
  readTimeout: Int? = null,
  registryName: String? = null,
  retentionPolicy: String? = null,
  step: Int? = null,
  uri: String? = null,
  userName: String? = null): VertxInfluxDbOptions = io.vertx.monitoring.backend.VertxInfluxDbOptions().apply {

  if (batchSize != null) {
    this.setBatchSize(batchSize)
  }
  if (compressed != null) {
    this.setCompressed(compressed)
  }
  if (connectTimeout != null) {
    this.setConnectTimeout(connectTimeout)
  }
  if (db != null) {
    this.setDb(db)
  }
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
  if (numThreads != null) {
    this.setNumThreads(numThreads)
  }
  if (password != null) {
    this.setPassword(password)
  }
  if (prefix != null) {
    this.setPrefix(prefix)
  }
  if (readTimeout != null) {
    this.setReadTimeout(readTimeout)
  }
  if (registryName != null) {
    this.setRegistryName(registryName)
  }
  if (retentionPolicy != null) {
    this.setRetentionPolicy(retentionPolicy)
  }
  if (step != null) {
    this.setStep(step)
  }
  if (uri != null) {
    this.setUri(uri)
  }
  if (userName != null) {
    this.setUserName(userName)
  }
}

