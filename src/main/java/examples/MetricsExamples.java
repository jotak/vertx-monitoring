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

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.docgen.Source;
import io.vertx.ext.web.Router;
import io.vertx.monitoring.VertxMonitoringOptions;
import io.vertx.monitoring.backend.BackendRegistries;
import io.vertx.monitoring.backend.VertxInfluxDbOptions;
import io.vertx.monitoring.backend.VertxPrometheusOptions;

/**
 * @author Joel Takvorian
 */
@Source
public class MetricsExamples {

  public void setupMinimalInfluxDB() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setInfluxDbOptions(new VertxInfluxDbOptions().setEnabled(true))
        .setEnabled(true)));
  }

  public void setupInfluxDBWithUriAndDatabase() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setInfluxDbOptions(new VertxInfluxDbOptions().setEnabled(true)
          .setUri("http://influxdb.example.com:8888")
          .setDb("sales-department"))
        .setEnabled(true)));
  }

  public void setupInfluxDBWithAuthentication() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setInfluxDbOptions(new VertxInfluxDbOptions().setEnabled(true)
          .setUserName("username")
          .setPassword("password"))
        .setEnabled(true)));
  }

  public void setupMinimalPrometheus() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
        .setEnabled(true)));
  }

  public void setupPrometheusEmbeddedServer() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true)
          .setEmbeddedServerOptions(new HttpServerOptions().setPort(8080))
          .setEmbeddedServerEndpoint("/metrics/vertx"))
        .setEnabled(true)));
  }

  public void setupPrometheusBoundRouter() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
        .setEnabled(true)));

    // Later on, creating a router
    Router router = Router.router(vertx);
    router.route("/metrics").handler(routingContext -> {
      PrometheusMeterRegistry prometheusRegistry = (PrometheusMeterRegistry) BackendRegistries.getDefaultNow();
      if (prometheusRegistry != null) {
        String response = prometheusRegistry.scrape();
        routingContext.response().end(response);
      } else {
        routingContext.fail(500);
      }
    });
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }

  public void accessDefaultRegistry() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setInfluxDbOptions(new VertxInfluxDbOptions().setEnabled(true)) // or VertxPrometheusOptions
        .setEnabled(true)));

    // Later on:
    MeterRegistry registry = BackendRegistries.getDefaultNow();
  }

  public void setupAndAccessCustomRegistry() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new VertxMonitoringOptions()
        .setInfluxDbOptions(new VertxInfluxDbOptions().setEnabled(true)) // or VertxPrometheusOptions
        .setRegistryName("my registry")
        .setEnabled(true)));

    // Later on:
    MeterRegistry registry = BackendRegistries.getNow("my registry");
  }

  public void instrumentJVM() {
    MeterRegistry registry = BackendRegistries.getDefaultNow();

    new ClassLoaderMetrics().bindTo(registry);
    new JvmMemoryMetrics().bindTo(registry);
    new JvmGcMetrics().bindTo(registry);
    new ProcessorMetrics().bindTo(registry);
    new JvmThreadMetrics().bindTo(registry);
  }
}
