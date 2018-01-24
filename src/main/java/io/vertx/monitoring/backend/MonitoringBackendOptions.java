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
import io.vertx.core.json.JsonObject;

/**
 * Options for monitoring backend.
 *
 * @author Joel Takvorian
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class MonitoringBackendOptions {

  /**
   * The default value of backend enabled false
   */
  public static final boolean DEFAULT_BACKEND_ENABLED = false;

  private boolean enabled;
  private JsonObject json; // Keep a copy of the original json, so we don't lose info when building options subclasses

  /**
   * Default constructor
   */
  public MonitoringBackendOptions() {
    enabled = DEFAULT_BACKEND_ENABLED;
  }

  /**
   * Copy constructor
   *
   * @param other The other {@link MonitoringBackendOptions} to copy when creating this
   */
  public MonitoringBackendOptions(MonitoringBackendOptions other) {
    enabled = other.isEnabled();
  }

  /**
   * Create an instance from a {@link JsonObject}
   *
   * @param json the JsonObject to create it from
   */
  public MonitoringBackendOptions(JsonObject json) {
    this();
    MonitoringBackendOptionsConverter.fromJson(json, this);
    this.json = json.copy();
  }

  /**
   * Will backend be enabled on the Vert.x instance?
   *
   * @return true if enabled, false if not.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Set whether backend will be enabled on the Vert.x instance.
   *
   * @param enable true if backend enabled, or false if not.
   * @return a reference to this, so the API can be used fluently
   */
  public MonitoringBackendOptions setEnabled(boolean enable) {
    this.enabled = enable;
    return this;
  }

  public JsonObject toJson() {
    return json != null ? json.copy() : new JsonObject();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MonitoringBackendOptions that = (MonitoringBackendOptions) o;

    if (enabled != that.enabled) return false;
    return !(json != null ? !json.equals(that.json) : that.json != null);

  }

  @Override
  public int hashCode() {
    int result = (enabled ? 1 : 0);
    result = 31 * result + (json != null ? json.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "MonitoringBackendOptions{" +
      "enabled=" + enabled +
      ", json=" + json +
      '}';
  }
}
