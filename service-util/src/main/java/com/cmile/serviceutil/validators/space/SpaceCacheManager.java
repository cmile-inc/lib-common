/*
 * Copyright 2024 cmile inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cmile.serviceutil.validators.space;

import com.cmile.platform.client.api.PlatformDeploymentUnitsApi;
import com.cmile.platform.client.model.SpaceResponse;
import com.cmile.serviceutil.apiinvoker.ApiInvoker;
import com.cmile.serviceutil.auth.jwt.JwtTokenProvider;
import com.cmile.serviceutil.cache.CacheManager;
import com.cmile.serviceutil.gcp.GCPServiceProject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpaceCacheManager extends CacheManager<String, SpaceResponse> {

  private final GCPServiceProject gcpServiceProject;
  private final ApiInvoker apiInvoker;

  private final ObjectMapper objectMapper;

  @Autowired
  public SpaceCacheManager(
      GCPServiceProject gcpServiceProject,
      ApiInvoker apiInvoker,
      ObjectMapper objectMapper) {
    super(5, TimeUnit.MINUTES, 500); // Call the superclass constructor first
    this.gcpServiceProject = gcpServiceProject;
    this.apiInvoker = apiInvoker;
    this.objectMapper = objectMapper;
    this.setLoader(this::loadSpaceDetails); // Set the loader after the object is initialized
  }

  private SpaceResponse loadSpaceDetails(String id) {

    log.debug("Loading space details for ID: {}", id);
    PlatformDeploymentUnitsApi deploymentUnitsApi = new PlatformDeploymentUnitsApi();
    Object result = apiInvoker.invoke(
        deploymentUnitsApi.getApiClient(),
        () -> deploymentUnitsApi.getDeploymentUnitsSpace(gcpServiceProject.getDu(), id));
    log.debug("Space details for ID: {} are loaded", id);
    return objectMapper.convertValue(result, SpaceResponse.class);
  }
}
