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

package io.vertx.monitoring.prometheus.impl;

import io.prometheus.client.CollectorRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.monitoring.common.MetricsCategory;
import io.vertx.monitoring.prometheus.VertxPrometheusOptions;
import io.vertx.monitoring.prometheus.VertxPrometheusServerOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(VertxUnitRunner.class)
public class PrometheusMetricsTest {

  private Vertx vertx;

  @Before
  public void setUp(TestContext context) {
    CollectorRegistry.defaultRegistry.clear();
  }

  @After
  public void after(TestContext context) {
    CollectorRegistry.defaultRegistry.clear();
  }

  @Test
  public void shouldStartDedicatedServer(TestContext context) {
    vertx = Vertx.vertx(new VertxOptions()
      .setMetricsOptions(new VertxPrometheusOptions().setEnabled(true)
        .embedServer(new VertxPrometheusServerOptions())));

    Async async = context.async();
    HttpClientRequest req = vertx.createHttpClient()
      .get(9090, "localhost", "/metrics")
      .handler(res -> {
        context.assertEquals(200, res.statusCode());
        res.bodyHandler(body -> {
          context.verify(v -> assertThat(body.toString())
            .contains("# HELP vertx_verticle"));
          async.complete();
        });
      });
    req.end();
  }

  @Test
  public void shouldBindExistingServer(TestContext context) {
    vertx = Vertx.vertx(new VertxOptions()
      .setMetricsOptions(new VertxPrometheusOptions().setEnabled(true)));

    Router router = Router.router(vertx);
    router.route("/custom").handler(PrometheusVertxMetrics.createMetricsHandler());
    vertx.createHttpServer().requestHandler(router::accept).listen(8081);

    Async async = context.async();
    HttpClientRequest req = vertx.createHttpClient()
      .get(8081, "localhost", "/custom")
      .handler(res -> {
        context.assertEquals(200, res.statusCode());
        res.bodyHandler(body -> {
          context.verify(v -> assertThat(body.toString())
            .contains("# HELP vertx_verticle"));
          async.complete();
        });
      });
    req.end();
  }

  @Test
  public void shouldExcludeVerticleMetrics(TestContext context) {
    vertx = Vertx.vertx(new VertxOptions()
      .setMetricsOptions(new VertxPrometheusOptions().setEnabled(true)
        .embedServer(new VertxPrometheusServerOptions().port(8080))
        .addDisabledMetricsCategory(MetricsCategory.VERTICLES)));

    Async async = context.async();
    HttpClientRequest req = vertx.createHttpClient()
      .get(8080, "localhost", "/metrics")
      .handler(res -> {
        context.assertEquals(200, res.statusCode());
        res.bodyHandler(body -> {
          context.verify(v -> assertThat(body.toString())
            .contains("# HELP vertx_pool_queue_size")
            .doesNotContain("# HELP vertx_verticle"));
          async.complete();
        });
      });
    req.end();
  }

  @Test
  public void shouldExposeEventBusMetrics(TestContext context) {
    vertx = Vertx.vertx(new VertxOptions()
      .setMetricsOptions(new VertxPrometheusOptions().setEnabled(true)
        .embedServer(new VertxPrometheusServerOptions())));

    // Send something on the eventbus and wait til it's received
    Async asyncEB = context.async();
    vertx.eventBus().consumer("test-eb", msg -> asyncEB.complete());
    vertx.eventBus().publish("test-eb", "test message");
    asyncEB.await(2000);

    // Read metrics on HTTP endpoint for eventbus metrics
    Async async = context.async();
    HttpClientRequest req = vertx.createHttpClient()
      .get(9090, "localhost", "/metrics")
      .handler(res -> {
        context.assertEquals(200, res.statusCode());
        res.bodyHandler(body -> {
          String str = body.toString();
          context.verify(v -> assertThat(str)
            .contains("vertx_eventbus_published{address=\"test-eb\",side=\"local\",} 1.0")
            .contains("vertx_eventbus_received{address=\"test-eb\",side=\"local\",} 1.0")
            .contains("vertx_eventbus_handlers{address=\"test-eb\",} 1.0")
            .contains("vertx_eventbus_delivered{address=\"test-eb\",side=\"local\",} 1.0")
            .contains("vertx_eventbus_processing_time_count{address=\"test-eb\",} 1.0"));
          async.complete();
        });
      });
    req.end();
  }
}
