/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.exec.store.mock;

import com.dremio.common.logical.StoragePluginConfigBase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(MockStorageEngineConfig.NAME)
public class MockStorageEngineConfig extends StoragePluginConfigBase {

  static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(MockStorageEngineConfig.class);

  private String url;

  public static final String NAME = "mock";

  @JsonCreator
  public MockStorageEngineConfig(@JsonProperty("url") String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MockStorageEngineConfig that = (MockStorageEngineConfig) o;

    if (url != null ? !url.equals(that.url) : that.url != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return url != null ? url.hashCode() : 0;
  }
}
