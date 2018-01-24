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
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.spi.metrics.EventBusMetrics;
import io.vertx.monitoring.meters.Counters;
import io.vertx.monitoring.meters.Gauges;
import io.vertx.monitoring.meters.Summaries;
import io.vertx.monitoring.meters.Timers;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author Joel Takvorian
 */
class VertxEventBusMetrics implements EventBusMetrics<VertxEventBusMetrics.Handler> {
  private final Gauges<LongAdder> handlers;
  private final Gauges<LongAdder> pending;
  private final Counters published;
  private final Counters sent;
  private final Counters received;
  private final Counters delivered;
  private final Counters errorCount;
  private final Counters replyFailures;
  private final Timers processTime;
  private final Summaries bytesRead;
  private final Summaries bytesWritten;

  VertxEventBusMetrics(MeterRegistry registry) {
    handlers = Gauges.longGauges("vertx_eventbus_handlers", "Number of event bus handlers in use",
      registry, Labels.ADDRESS);
    pending = Gauges.longGauges("vertx_eventbus_pending", "Number of messages not processed yet",
      registry, Labels.ADDRESS, Labels.SIDE);
    published = new Counters("vertx_eventbus_published", "Number of messages published (publish / subscribe)",
      registry, Labels.ADDRESS, Labels.SIDE);
    sent = new Counters("vertx_eventbus_sent", "Number of messages sent (point-to-point)",
      registry, Labels.ADDRESS, Labels.SIDE);
    received = new Counters("vertx_eventbus_received", "Number of messages received",
      registry, Labels.ADDRESS, Labels.SIDE);
    delivered = new Counters("vertx_eventbus_delivered", "Number of messages delivered to handlers",
      registry, Labels.ADDRESS, Labels.SIDE);
    errorCount = new Counters("vertx_eventbus_errors", "Number of errors",
      registry, Labels.ADDRESS, Labels.CLASS);
    replyFailures = new Counters("vertx_eventbus_reply_failures", "Number of message reply failures",
      registry, Labels.ADDRESS, "failure");
    processTime = new Timers("vertx_eventbus_processing_time", "Processing time",
      registry, Labels.ADDRESS);
    bytesRead = new Summaries("vertx_eventbus_bytes_read", "Number of bytes received while reading messages from event bus cluster peers",
      registry, Labels.ADDRESS);
    bytesWritten = new Summaries("vertx_eventbus_bytes_written", "Number of bytes sent while sending messages to event bus cluster peers",
      registry, Labels.ADDRESS);
  }

  @Override
  public Handler handlerRegistered(String address, String repliedAddress) {
    handlers.get(address).increment();
    return new Handler(address);
  }

  @Override
  public void handlerUnregistered(Handler handler) {
    handlers.get(handler.address).decrement();
  }

  @Override
  public void scheduleMessage(Handler handler, boolean b) {
  }

  @Override
  public void beginHandleMessage(Handler handler, boolean local) {
    pending.get(handler.address, Labels.getSide(local)).decrement();
    handler.timer = processTime.start(handler.address);
  }

  @Override
  public void endHandleMessage(Handler handler, Throwable failure) {
    handler.timer.end();
    if (failure != null) {
      errorCount.get(handler.address, failure.getClass().getSimpleName()).increment();
    }
  }

  @Override
  public void messageSent(String address, boolean publish, boolean local, boolean remote) {
    if (publish) {
      published.get(address, Labels.getSide(local)).increment();
    } else {
      sent.get(address, Labels.getSide(local)).increment();
    }
  }

  @Override
  public void messageReceived(String address, boolean publish, boolean local, int handlers) {
    String origin = Labels.getSide(local);
    pending.get(address, origin).add(handlers);
    received.get(address, origin).increment();
    if (handlers > 0) {
      delivered.get(address, origin).increment();
    }
  }

  @Override
  public void messageWritten(String address, int numberOfBytes) {
    bytesWritten.get(address).record(numberOfBytes);
  }

  @Override
  public void messageRead(String address, int numberOfBytes) {
    bytesRead.get(address).record(numberOfBytes);
  }

  @Override
  public void replyFailure(String address, ReplyFailure failure) {
    replyFailures.get(address, failure.name()).increment();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {
  }

  public static class Handler {
    private final String address;
    private Timers.EventTiming timer;

    Handler(String address) {
      this.address = address;
    }
  }
}
