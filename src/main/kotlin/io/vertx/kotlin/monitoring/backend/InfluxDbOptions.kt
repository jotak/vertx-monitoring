package io.vertx.kotlin.monitoring.backend

import io.vertx.monitoring.backend.VertxInfluxDbOptions

/**
 * A function providing a DSL for building [io.vertx.monitoring.backend.VertxInfluxDbOptions] objects.
 *
 * Vert.x InfluxDb monitoring configuration.
 *
 * @param batchSize
 * @param compressed
 * @param connectTimeout
 * @param db
 * @param enabled  Set whether backend will be enabled on the Vert.x instance.
 * @param numThreads
 * @param password
 * @param prefix
 * @param readTimeout
 * @param retentionPolicy
 * @param step
 * @param uri
 * @param userName
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.monitoring.backend.VertxInfluxDbOptions original] using Vert.x codegen.
 */
fun InfluxDbOptions(
  batchSize: Int? = null,
  compressed: Boolean? = null,
  connectTimeout: Int? = null,
  db: String? = null,
  enabled: Boolean? = null,
  numThreads: Int? = null,
  password: String? = null,
  prefix: String? = null,
  readTimeout: Int? = null,
  retentionPolicy: String? = null,
  step: Int? = null,
  uri: String? = null,
  userName: String? = null): VertxInfluxDbOptions = VertxInfluxDbOptions().apply {

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

