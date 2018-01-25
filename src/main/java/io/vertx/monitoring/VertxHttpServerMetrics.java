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
package io.vertx.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.HttpServerMetrics;
import io.vertx.monitoring.meters.Counters;
import io.vertx.monitoring.meters.Gauges;
import io.vertx.monitoring.meters.Timers;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author Joel Takvorian
 */
class VertxHttpServerMetrics extends VertxNetServerMetrics {
  private final Gauges<LongAdder> requests;
  private final Counters requestCount;
  private final Counters requestResetCount;
  private final Timers processingTime;
  private final Gauges<LongAdder> wsConnections;

  VertxHttpServerMetrics(VertxMonitoringOptions options, MeterRegistry registry) {
    super(options, registry, "vertx.http");
    if (hasRemoteLabel) {
      requests = Gauges.longGauges("vertx.http.server.requests", "Number of requests being processed",
        registry, Labels.LOCAL, Labels.REMOTE);
      requestCount = new Counters("vertx.http.server.requestCount", "Number of processed requests",
        registry, Labels.LOCAL, Labels.REMOTE, Labels.METHOD, Labels.CODE);
      requestResetCount = new Counters("vertx.http.server.requestResetCount", "Number of requests reset",
        registry, Labels.LOCAL, Labels.REMOTE);
      processingTime = new Timers("vertx.http.server.responseTime", "Request processing time",
        registry, Labels.LOCAL, Labels.REMOTE);
      wsConnections = Gauges.longGauges("vertx.http.server.wsConnections", "Number of websockets currently opened",
        registry, Labels.LOCAL, Labels.REMOTE);
    } else {
      requests = Gauges.longGauges("vertx.http.server.requests", "Number of requests being processed",
        registry, Labels.LOCAL);
      requestCount = new Counters("vertx.http.server.requestCount", "Number of processed requests",
        registry, Labels.LOCAL, Labels.METHOD, Labels.CODE);
      requestResetCount = new Counters("vertx.http.server.requestResetCount", "Number of requests reset",
        registry, Labels.LOCAL);
      processingTime = new Timers("vertx.http.server.responseTime", "Request processing time",
        registry, Labels.LOCAL);
      wsConnections = Gauges.longGauges("vertx.http.server.wsConnections", "Number of websockets currently opened",
        registry, Labels.LOCAL);
    }
  }

  @Override
  HttpServerMetrics forAddress(SocketAddress localAddress) {
    String local = Labels.fromAddress(localAddress);
    return new Instance(local);
  }

  class Instance extends VertxNetServerMetrics.Instance implements HttpServerMetrics<Handler, String, String> {
    Instance(String local) {
      super(local);
    }

    @Override
    public Handler requestBegin(String remote, HttpServerRequest request) {
      Handler handler = new Handler(remote, request.method().name());
      if (hasRemoteLabel) {
        requests.get(local, remote).increment();
        handler.timer = processingTime.start(local, remote);
      } else {
        requests.get(local).increment();
        handler.timer = processingTime.start(local);
      }
      return handler;
    }

    @Override
    public void requestReset(Handler handler) {
      if (hasRemoteLabel) {
        requestResetCount.get(local, handler.address).increment();
        requests.get(local, handler.address).decrement();
      } else {
        requestResetCount.get(local).increment();
        requests.get(local).decrement();
      }
    }

    @Override
    public Handler responsePushed(String remote, HttpMethod method, String uri, HttpServerResponse response) {
      Handler handler = new Handler(remote, method.name());
      if (hasRemoteLabel) {
        requests.get(local, remote).increment();
      } else {
        requests.get(local).increment();
      }
      return handler;
    }

    @Override
    public void responseEnd(Handler handler, HttpServerResponse response) {
      handler.timer.end();
      if (hasRemoteLabel) {
        requestCount.get(local, handler.address, handler.method, String.valueOf(response.getStatusCode())).increment();
        requests.get(local, handler.address).decrement();
      } else {
        requestCount.get(local, handler.method, String.valueOf(response.getStatusCode())).increment();
        requests.get(local).decrement();
      }
    }

    @Override
    public String upgrade(Handler handler, ServerWebSocket serverWebSocket) {
      return handler.address;
    }

    @Override
    public String connected(String remote, ServerWebSocket serverWebSocket) {
      if (hasRemoteLabel) {
        wsConnections.get(local, remote).increment();
      } else {
        wsConnections.get(local).increment();
      }
      return remote;
    }

    @Override
    public void disconnected(String remote) {
      if (hasRemoteLabel) {
        wsConnections.get(local, remote).decrement();
      } else {
        wsConnections.get(local).decrement();
      }
    }

    @Override
    public boolean isEnabled() {
      return true;
    }

    @Override
    public void close() {
    }
  }

  public static class Handler {
    private final String address;
    private final String method;
    private Timers.EventTiming timer;

    Handler(String address, String method) {
      this.address = address;
      this.method = method;
    }
  }
}
