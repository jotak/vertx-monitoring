/*
 * Copyright (c) 2014 Red Hat, Inc. and others
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.monitoring.backend;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.monitoring.backend.VertxPrometheusOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.monitoring.backend.VertxPrometheusOptions} original class using Vert.x codegen.
 */
public class VertxPrometheusOptionsConverter {

  public static void fromJson(JsonObject json, VertxPrometheusOptions obj) {
    if (json.getValue("disabledMetricsCategories") instanceof JsonArray) {
      java.util.LinkedHashSet<io.vertx.monitoring.MetricsCategory> list = new java.util.LinkedHashSet<>();
      json.getJsonArray("disabledMetricsCategories").forEach( item -> {
        if (item instanceof String)
          list.add(io.vertx.monitoring.MetricsCategory.valueOf((String)item));
      });
      obj.setDisabledMetricsCategories(list);
    }
    if (json.getValue("embeddedServerEndpoint") instanceof String) {
      obj.setEmbeddedServerEndpoint((String)json.getValue("embeddedServerEndpoint"));
    }
    if (json.getValue("embeddedServerOptions") instanceof JsonObject) {
      obj.setEmbeddedServerOptions(new io.vertx.core.http.HttpServerOptions((JsonObject)json.getValue("embeddedServerOptions")));
    }
    if (json.getValue("enableRemoteLabelForClients") instanceof Boolean) {
      obj.setEnableRemoteLabelForClients((Boolean)json.getValue("enableRemoteLabelForClients"));
    }
    if (json.getValue("enableRemoteLabelForServers") instanceof Boolean) {
      obj.setEnableRemoteLabelForServers((Boolean)json.getValue("enableRemoteLabelForServers"));
    }
    if (json.getValue("enabled") instanceof Boolean) {
      obj.setEnabled((Boolean)json.getValue("enabled"));
    }
    if (json.getValue("registryName") instanceof String) {
      obj.setRegistryName((String)json.getValue("registryName"));
    }
  }

  public static void toJson(VertxPrometheusOptions obj, JsonObject json) {
    if (obj.getDisabledMetricsCategories() != null) {
      JsonArray array = new JsonArray();
      obj.getDisabledMetricsCategories().forEach(item -> array.add(item.name()));
      json.put("disabledMetricsCategories", array);
    }
    if (obj.getEmbeddedServerEndpoint() != null) {
      json.put("embeddedServerEndpoint", obj.getEmbeddedServerEndpoint());
    }
    if (obj.getEmbeddedServerOptions() != null) {
      json.put("embeddedServerOptions", obj.getEmbeddedServerOptions().toJson());
    }
    json.put("enableRemoteLabelForClients", obj.isEnableRemoteLabelForClients());
    json.put("enableRemoteLabelForServers", obj.isEnableRemoteLabelForServers());
    json.put("enabled", obj.isEnabled());
    if (obj.getRegistryName() != null) {
      json.put("registryName", obj.getRegistryName());
    }
  }
}