/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertx.monitoring.backend;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.monitoring.VertxMonitoringOptions;

/**
 * Options for Prometheus metrics backend.
 *
 * @author Joel Takvorian
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class VertxPrometheusOptions extends VertxMonitoringOptions {

  /**
   * The default metrics endpoint = /metrics when using an embedded server.
   */
  public static final String DEFAULT_EMBEDDED_SERVER_ENDPOINT = "/metrics";

  private HttpServerOptions embeddedServerOptions;
  private String embeddedServerEndpoint;

  /**
   * Default constructor
   */
  public VertxPrometheusOptions() {
    super();
    embeddedServerEndpoint = DEFAULT_EMBEDDED_SERVER_ENDPOINT;
  }

  /**
   * Copy constructor
   *
   * @param other The other {@link VertxPrometheusOptions} to copy when creating this
   */
  public VertxPrometheusOptions(VertxPrometheusOptions other) {
    super(other);
    embeddedServerEndpoint = other.embeddedServerEndpoint != null ? other.embeddedServerEndpoint : DEFAULT_EMBEDDED_SERVER_ENDPOINT;
    if (other.embeddedServerOptions != null) {
      embeddedServerOptions = new HttpServerOptions(other.embeddedServerOptions);
    }
  }

  /**
   * Create an instance from a {@link io.vertx.core.json.JsonObject}
   *
   * @param json the JsonObject to create it from
   */
  public VertxPrometheusOptions(JsonObject json) {
    this();
    VertxPrometheusOptionsConverter.fromJson(json, this);
  }

  @Override
  public VertxPrometheusOptions setEnabled(boolean enable) {
    super.setEnabled(enable);
    return this;
  }

  public HttpServerOptions getEmbeddedServerOptions() {
    return embeddedServerOptions;
  }

  /**
   * An embedded server will start to expose metrics with Prometheus format
   * @param embeddedServerOptions the server options
   */
  public VertxPrometheusOptions setEmbeddedServerOptions(HttpServerOptions embeddedServerOptions) {
    this.embeddedServerOptions = embeddedServerOptions;
    return this;
  }

  /**
   * Set metrics endpoint. Use conjointly with the embedded server options.
   * @param embeddedServerEndpoint metrics endpoint
   */
  public VertxPrometheusOptions setEmbeddedServerEndpoint(String embeddedServerEndpoint) {
    this.embeddedServerEndpoint = embeddedServerEndpoint;
    return this;
  }

  public String getEmbeddedServerEndpoint() {
    return embeddedServerEndpoint;
  }
}
