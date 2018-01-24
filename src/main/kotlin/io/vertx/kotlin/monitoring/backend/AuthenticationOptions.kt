package io.vertx.kotlin.monitoring.backend

import io.vertx.monitoring.backend.AuthenticationOptions

/**
 * A function providing a DSL for building [io.vertx.monitoring.backend.AuthenticationOptions] objects.
 *
 * Authentication options.
 *
 * @param enabled  Set whether authentication is enabled. Defaults to <code>false</code>.
 * @param secret  Set the secret used for authentication.
 * @param username  Set the identifier used for authentication.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.monitoring.backend.AuthenticationOptions original] using Vert.x codegen.
 */
fun AuthenticationOptions(
  enabled: Boolean? = null,
  secret: String? = null,
  username: String? = null): AuthenticationOptions = io.vertx.monitoring.backend.AuthenticationOptions().apply {

  if (enabled != null) {
    this.setEnabled(enabled)
  }
  if (secret != null) {
    this.setSecret(secret)
  }
  if (username != null) {
    this.setUsername(username)
  }
}

