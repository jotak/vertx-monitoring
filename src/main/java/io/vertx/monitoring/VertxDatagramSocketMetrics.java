/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates
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
package io.vertx.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.net.impl.SocketAddressImpl;
import io.vertx.core.spi.metrics.DatagramSocketMetrics;
import io.vertx.monitoring.meters.Counters;
import io.vertx.monitoring.meters.Summaries;

/**
 * @author Joel Takvorian
 */
class VertxDatagramSocketMetrics implements DatagramSocketMetrics {

  private final Summaries bytesReceived;
  private final Summaries bytesSent;
  private final Counters errorCount;

  private volatile String localAddress;

  VertxDatagramSocketMetrics(MeterRegistry registry) {
    bytesReceived = new Summaries("vertx_datagram_bytes_received", "Total number of datagram bytes received",
      registry, Labels.LOCAL);
    bytesSent = new Summaries("vertx_datagram_bytes_sent", "Total number of datagram bytes sent",
      registry);
    errorCount = new Counters("vertx_datagram_errors", "Total number of datagram errors",
      registry, Labels.CLASS);
  }

  @Override
  public void listening(String localName, SocketAddress localAddress) {
    this.localAddress = Labels.fromAddress(new SocketAddressImpl(localAddress.port(), localName));
  }

  @Override
  public void bytesRead(Void socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    if (localAddress != null) {
      bytesReceived.get(localAddress).record(numberOfBytes);
    }
  }

  @Override
  public void bytesWritten(Void socketMetric, SocketAddress remoteAddress, long numberOfBytes) {
    bytesSent.get().record(numberOfBytes);
  }

  @Override
  public void exceptionOccurred(Void socketMetric, SocketAddress remoteAddress, Throwable t) {
    errorCount.get(t.getClass().getSimpleName()).increment();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {
  }
}
