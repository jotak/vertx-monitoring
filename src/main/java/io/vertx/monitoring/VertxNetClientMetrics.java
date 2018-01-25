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
import io.vertx.core.net.SocketAddress;
import io.vertx.core.net.impl.SocketAddressImpl;
import io.vertx.core.spi.metrics.TCPMetrics;
import io.vertx.monitoring.meters.Counters;
import io.vertx.monitoring.meters.Gauges;
import io.vertx.monitoring.meters.Summaries;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author Joel Takvorian
 */
class VertxNetClientMetrics {
  private final Gauges<LongAdder> connections;
  private final Summaries bytesReceived;
  private final Summaries bytesSent;
  private final Counters errorCount;
  final boolean hasRemoteLabel;

  VertxNetClientMetrics(VertxMonitoringOptions options, MeterRegistry registry) {
    this(options, registry, "vertx.net");
  }

  VertxNetClientMetrics(VertxMonitoringOptions options, MeterRegistry registry, String prefix) {
    if (options.isEnableRemoteLabelForClients()) {
      hasRemoteLabel = true;
      connections = Gauges.longGauges(prefix + ".client.connections", "Number of connections to the remote host currently opened",
        registry, Labels.LOCAL, Labels.REMOTE);
      bytesReceived = new Summaries(prefix + ".client.bytesReceived", "Number of bytes received from the remote host",
        registry, Labels.LOCAL, Labels.REMOTE);
      bytesSent = new Summaries(prefix + ".client.bytesSent", "Number of bytes sent to the remote host",
        registry, Labels.LOCAL, Labels.REMOTE);
      errorCount = new Counters(prefix + ".client.errors", "Number of errors",
        registry, Labels.LOCAL, Labels.REMOTE, Labels.CLASS);
    } else {
      hasRemoteLabel = false;
      connections = Gauges.longGauges(prefix + ".client.connections", "Number of connections to the remote host currently opened",
        registry, Labels.LOCAL);
      bytesReceived = new Summaries(prefix + ".client.bytesReceived", "Number of bytes received from the remote host",
        registry, Labels.LOCAL);
      bytesSent = new Summaries(prefix + ".client.bytesSent", "Number of bytes sent to the remote host",
        registry, Labels.LOCAL);
      errorCount = new Counters(prefix + ".client.errors", "Number of errors",
        registry, Labels.LOCAL, Labels.CLASS);
    }
  }

  TCPMetrics forAddress(String localAddress) {
    return new Instance(localAddress);
  }

  class Instance implements TCPMetrics<String> {
    protected final String local;

    Instance(String localAddress) {
      this.local = localAddress == null ? "?" : localAddress;
    }

    @Override
    public String connected(SocketAddress remoteAddress, String remoteName) {
      String remote = Labels.fromAddress(new SocketAddressImpl(remoteAddress.port(), remoteName));
      if (hasRemoteLabel) {
        connections.get(local, remote).increment();
      } else {
        connections.get(local).increment();
      }
      return remote;
    }

    @Override
    public void disconnected(String remote, SocketAddress remoteAddress) {
      if (hasRemoteLabel) {
        connections.get(local, remote).decrement();
      } else {
        connections.get(local).decrement();
      }
    }

    @Override
    public void bytesRead(String remote, SocketAddress remoteAddress, long numberOfBytes) {
      if (hasRemoteLabel) {
        bytesReceived.get(local, remote).record(numberOfBytes);
      } else {
        bytesReceived.get(local).record(numberOfBytes);
      }
    }

    @Override
    public void bytesWritten(String remote, SocketAddress remoteAddress, long numberOfBytes) {
      if (hasRemoteLabel) {
        bytesSent.get(local, remote).record(numberOfBytes);
      } else {
        bytesSent.get(local).record(numberOfBytes);
      }
    }

    @Override
    public void exceptionOccurred(String remote, SocketAddress remoteAddress, Throwable t) {
      if (hasRemoteLabel) {
        errorCount.get(local, remote, t.getClass().getSimpleName()).increment();
      } else {
        errorCount.get(local, t.getClass().getSimpleName()).increment();
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
}
