package io.vertx.kotlin.monitoring.backend

import io.vertx.monitoring.backend.MonitoringBackendOptions

/**
 * A function providing a DSL for building [io.vertx.monitoring.backend.MonitoringBackendOptions] objects.
 *
 * Options for monitoring backend.
 *
 * @param enabled  Set whether backend will be enabled on the Vert.x instance.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.monitoring.backend.MonitoringBackendOptions original] using Vert.x codegen.
 */
fun MonitoringBackendOptions(
  enabled: Boolean? = null): MonitoringBackendOptions = io.vertx.monitoring.backend.MonitoringBackendOptions().apply {

  if (enabled != null) {
    this.setEnabled(enabled)
  }
}

