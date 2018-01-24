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
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.WebSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.spi.metrics.HttpClientMetrics;
import io.vertx.monitoring.meters.Counters;
import io.vertx.monitoring.meters.Gauges;
import io.vertx.monitoring.meters.Timers;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author Joel Takvorian
 */
class VertxHttpClientMetrics extends VertxNetClientMetrics {
  private final Gauges<LongAdder> requests;
  private final Counters requestCount;
  private final Timers responseTime;
  private final Counters responseCount;
  private final Gauges<LongAdder> wsConnections;

  VertxHttpClientMetrics(VertxMonitoringOptions options, MeterRegistry registry) {
    super(options, registry, "vertx_http");
    if (hasRemoteLabel) {
      requests = Gauges.longGauges("vertx_http_client_requests", "Number of requests waiting for a response",
        registry, Labels.LOCAL, Labels.REMOTE);
      requestCount = new Counters("vertx_http_client_request_count", "Number of requests sent",
        registry, Labels.LOCAL, Labels.REMOTE, Labels.METHOD);
      responseTime = new Timers("vertx_http_client_reponse_time", "Response time",
        registry, Labels.LOCAL, Labels.REMOTE);
      responseCount = new Counters("vertx_http_client_reponse_count", "Response count with codes",
        registry, Labels.LOCAL, Labels.REMOTE, Labels.CODE);
      wsConnections = Gauges.longGauges("vertx_http_client_ws_connections", "Number of websockets currently opened",
        registry, Labels.LOCAL, Labels.REMOTE);
    } else {
      requests = Gauges.longGauges("vertx_http_client_requests", "Number of requests waiting for a response",
        registry, Labels.LOCAL);
      requestCount = new Counters("vertx_http_client_request_count", "Number of requests sent",
        registry, Labels.LOCAL, Labels.METHOD);
      responseTime = new Timers("vertx_http_client_reponse_time", "Response time",
        registry, Labels.LOCAL);
      responseCount = new Counters("vertx_http_client_reponse_count", "Response count with codes",
        registry, Labels.LOCAL, Labels.CODE);
      wsConnections = Gauges.longGauges("vertx_http_client_ws_connections", "Number of websockets currently opened",
        registry, Labels.LOCAL);
    }
  }

  @Override
  HttpClientMetrics forAddress(String localAddress) {
    return new Instance(localAddress);
  }

  class Instance extends VertxNetClientMetrics.Instance implements HttpClientMetrics<VertxHttpClientMetrics.Handler, String, String, Void, Void> {
    Instance(String localAddress) {
      super(localAddress);
    }

    @Override
    public Void createEndpoint(String host, int port, int maxPoolSize) {
      return null;
    }

    @Override
    public void closeEndpoint(String host, int port, Void endpointMetric) {
    }

    @Override
    public Void enqueueRequest(Void endpointMetric) {
      return null;
    }

    @Override
    public void dequeueRequest(Void endpointMetric, Void taskMetric) {
    }

    @Override
    public void endpointConnected(Void endpointMetric, String socketMetric) {
    }

    @Override
    public void endpointDisconnected(Void endpointMetric, String socketMetric) {
    }

    @Override
    public Handler requestBegin(Void endpointMetric, String remote, SocketAddress localAddress, SocketAddress remoteAddress, HttpClientRequest request) {
      Handler handler = new Handler(remote);
      if (hasRemoteLabel) {
        requests.get(local, remote).increment();
        requestCount.get(local, remote, request.method().name()).increment();
        handler.timer = responseTime.start(local, remote);
      } else {
        requests.get(local).increment();
        requestCount.get(local, request.method().name()).increment();
        handler.timer = responseTime.start(local);
      }
      return handler;
    }

    @Override
    public void requestEnd(Handler requestMetric) {
    }

    @Override
    public void responseBegin(Handler requestMetric, HttpClientResponse response) {
    }

    @Override
    public Handler responsePushed(Void endpointMetric, String remote, SocketAddress localAddress, SocketAddress remoteAddress, HttpClientRequest request) {
      return requestBegin(null, remote, localAddress, remoteAddress, request);
    }

    @Override
    public void requestReset(Handler handler) {
      if (hasRemoteLabel) {
        requests.get(local, handler.address).decrement();
      } else {
        requests.get(local).decrement();
      }
    }

    @Override
    public void responseEnd(Handler handler, HttpClientResponse response) {
      if (hasRemoteLabel) {
        requests.get(local, handler.address).decrement();
        responseCount.get(local, handler.address, String.valueOf(response.statusCode()));
      } else {
        requests.get(local).decrement();
        responseCount.get(local, String.valueOf(response.statusCode()));
      }
      handler.timer.end();
    }

    @Override
    public String connected(Void endpointMetric, String remote, WebSocket webSocket) {
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
    private Timers.EventTiming timer;

    Handler(String address) {
      this.address = address;
    }
  }
}
