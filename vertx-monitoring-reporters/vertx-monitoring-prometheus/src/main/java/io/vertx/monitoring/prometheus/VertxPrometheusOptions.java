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
package io.vertx.monitoring.prometheus;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.monitoring.common.MetricsOptionsBase;
import io.vertx.monitoring.prometheus.impl.PrometheusVertxMetrics;

/**
 * Vert.x Prometheus monitoring configuration.
 * If no embedded server is used, you can bind an existing {@link io.vertx.ext.web.Router} with {@link PrometheusVertxMetrics#createMetricsHandler()}<br/>
 * Ex:<br/>
 * {@code myRouter.route("/metrics").handler(PrometheusVertxMetrics.createMetricsHandler());}
 *
 * @author Joel Takvorian
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class VertxPrometheusOptions extends MetricsOptionsBase {

  /**
   * By default, uses the standard prometheus registry (name is null)
   */
  public static final String DEFAULT_REGISTRY_NAME = null;

  /**
   * By default, enables <i>remote</i> label for net/http clients.
   */
  public static final boolean DEFAULT_ENABLE_REMOTE_LABEL_FOR_CLIENTS = true;

  /**
   * By default, disables <i>remote</i> label for net/http servers.
   */
  public static final boolean DEFAULT_ENABLE_REMOTE_LABEL_FOR_SERVERS = false;

  /**
   * The default metrics endpoint = /metrics when using an embedded server.
   */
  public static final String DEFAULT_EMBEDDED_SERVER_ENDPOINT = "/metrics";

  private HttpServerOptions embeddedServerOptions;
  private String registryName;
  private boolean enableRemoteLabelForClients;
  private boolean enableRemoteLabelForServers;
  private String embeddedServerEndpoint;

  public VertxPrometheusOptions() {
    registryName = DEFAULT_REGISTRY_NAME;
    enableRemoteLabelForClients = DEFAULT_ENABLE_REMOTE_LABEL_FOR_CLIENTS;
    enableRemoteLabelForServers = DEFAULT_ENABLE_REMOTE_LABEL_FOR_SERVERS;
    embeddedServerEndpoint = DEFAULT_EMBEDDED_SERVER_ENDPOINT;
  }

  public VertxPrometheusOptions(VertxPrometheusOptions other) {
    super(other);
    registryName = other.registryName;
    enableRemoteLabelForClients = other.enableRemoteLabelForClients;
    enableRemoteLabelForServers = other.enableRemoteLabelForServers;
    embeddedServerEndpoint = other.embeddedServerEndpoint != null ? other.embeddedServerEndpoint : DEFAULT_EMBEDDED_SERVER_ENDPOINT;
    if (other.embeddedServerOptions != null) {
      embeddedServerOptions = new HttpServerOptions(other.embeddedServerOptions);
    }
  }

  public VertxPrometheusOptions(JsonObject json) {
    this();
    VertxPrometheusOptionsConverter.fromJson(json, this);
  }

  /**
   * Set whether metrics will be enabled on the Vert.x instance. Metrics are not enabled by default.
   */
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

  public String getRegistryName() {
    return registryName;
  }

  /**
   * Set a name for the prometheus registry, so that a new registry will be created and associated with this name.
   * To retrieve this registry later, call {@link io.vertx.monitoring.prometheus.impl.PrometheusRegistries#get(String)}
   * Doing so allows to provide application-defined metrics to the same registry.
   * If {@code registryName} is not provided (or null), Prometheus default registry will be used.
   * @param registryName a name to uniquely identify this registry
   */
  public VertxPrometheusOptions setRegistryName(String registryName) {
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
  public VertxPrometheusOptions setEnableRemoteLabelForClients(boolean enableRemoteLabelForClients) {
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
  public VertxPrometheusOptions setEnableRemoteLabelForServers(boolean enableRemoteLabelForServers) {
    this.enableRemoteLabelForServers = enableRemoteLabelForServers;
    return this;
  }

  /**
   * Set metrics endpoint. Use conjointly with {@link VertxPrometheusOptions#setEmbeddedServerOptions(HttpServerOptions)}
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
