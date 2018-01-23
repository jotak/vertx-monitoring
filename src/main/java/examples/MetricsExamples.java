/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
package examples;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.docgen.Source;
import io.vertx.ext.web.Router;
import io.vertx.monitoring.common.MetricsCategory;
import io.vertx.monitoring.influxdb.AuthenticationOptions;
import io.vertx.monitoring.influxdb.VertxInfluxDbOptions;
import io.vertx.monitoring.prometheus.VertxPrometheusOptions;
import io.vertx.monitoring.prometheus.impl.PrometheusVertxMetrics;

/**
 * @author Thomas Segismont
 * @author Joel Takvorian
 */
@Source
@SuppressWarnings("unused")
public class MetricsExamples {

  Vertx vertx;

  public void setupInfluxDB() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions().setEnabled(true)
    ));
  }

  public void setupInfluxDBRemote() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions()
        .setHost("influxdb.example.com")
        .setPort(8086)
        .setEnabled(true)
    ));
  }

  public void setupInfluxDBDatabase() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions()
        .setDatabase("sales-department")
        .setEnabled(true)
    ));
  }

  public void setupInfluxDBServer() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions()
        .setDatabase("influxdb")
        .setAuthenticationOptions(
          new AuthenticationOptions()
            .setEnabled(true)
            .setUsername("username")
            .setSecret("password")
        )
        .setEnabled(true)
    ));
  }

  public void setupInfluxDBOpenshiftTokenAuthentication() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions()
        .setDatabase("my-namespace")
        .setHttpHeaders(new JsonObject()
          .put("Authorization", "Bearer xkjdksf9890-shjkjhkjlkjlk")
        )
        .setEnabled(true)
    ));
  }

  public void setupInfluxDBSecured() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions()
        .setHost("influxdb.example.com")
        .setPort(443)
        .setHttpOptions(new HttpClientOptions().setSsl(true))
        .setEnabled(true)
    ));
  }

  public void enableMetricsBridge() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions()
        .setMetricsBridgeEnabled(true)
        .setEnabled(true)
    ));
  }

  public void customMetricsBridgeAddress() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxInfluxDbOptions()
        .setMetricsBridgeEnabled(true)
        .setMetricsBridgeAddress("__influxdb_metrics")
        .setEnabled(true)
    ));
  }

  public void userDefinedMetric() {
    JsonObject message = new JsonObject()
      .put("id", "myapp.files.opened")
      .put("value", 7);
    vertx.eventBus().publish("influxdb.metrics", message);
  }

  public void userDefinedMetricExplicit() {
    JsonObject counterMetric = new JsonObject()
      .put("id", "myapp.files.opened")
      .put("type", "counter")
      .put("timestamp", 189898098098908L)
      .put("value", 7);
    vertx.eventBus().publish("influxdb.metrics", counterMetric);

    JsonObject availabilityMetric = new JsonObject()
      .put("id", "myapp.mysubsystem.status")
      .put("type", "availability")
      .put("value", "up");
    vertx.eventBus().publish("influxdb.metrics", availabilityMetric);
  }

  public void setupPrometheus() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxPrometheusOptions().setEnabled(true)));
  }

  public void setupPrometheusEmbeddedServer() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxPrometheusOptions().setEnabled(true)
        .setEmbeddedServerOptions(new HttpServerOptions())));
  }

  public void setupPrometheusEmbeddedServerWithOptions() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxPrometheusOptions().setEnabled(true)
        .setEmbeddedServerOptions(new HttpServerOptions().setPort(8080))
        .setEmbeddedServerEndpoint("/metrics/vertx")));
  }

  public void setupPrometheusBoundRouter() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxPrometheusOptions().setEnabled(true)));

    // Later on, creating a router
    Router router = Router.router(vertx);
    router.route("/custom").handler(PrometheusVertxMetrics.createMetricsHandler());
  }

  public void setupPrometheusBoundRouterWithCustomRegistry() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxPrometheusOptions().setRegistryName("my registry").setEnabled(true)));

    // Later on, creating a router
    Router router = Router.router(vertx);
    router.route("/custom").handler(PrometheusVertxMetrics.createMetricsHandler("my registry"));
  }

  public void setupPrometheusDisabledMetricsTypes() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxPrometheusOptions()
        .addDisabledMetricsCategory(MetricsCategory.HTTP_CLIENT)
        .addDisabledMetricsCategory(MetricsCategory.NET_CLIENT)
        .setEnabled(true)));
  }

}
