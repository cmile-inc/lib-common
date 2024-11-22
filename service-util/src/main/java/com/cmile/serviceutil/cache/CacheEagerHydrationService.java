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

package com.cmile.serviceutil.cache;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import com.cmile.platform.client.api.PlatformDeploymentUnitsApi;
import com.cmile.platform.client.model.GetAllSpacesResponse;
import com.cmile.platform.client.model.SpaceResponse;
import com.cmile.serviceutil.apiinvoker.ApiInvoker;
import com.cmile.serviceutil.gcp.GCPServiceProject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CacheEagerHydrationService {
    private final TaskExecutor taskExecutor;
    private final List<CacheRepository<?, ?>> cacheRepos;
    private final ApiInvoker apiInvoker;
    private final GCPServiceProject gcpServiceProject;
    private final ObjectMapper objectMapper;

    @Autowired
    public CacheEagerHydrationService(
            @Qualifier("cacheTaskExecutor") TaskExecutor taskExecutor, List<CacheRepository<?, ?>> cacheRepos,
            ApiInvoker apiInvoker, GCPServiceProject gcpServiceProject, ObjectMapper objectMapper) {
        this.taskExecutor = taskExecutor;
        this.cacheRepos = cacheRepos;
        this.apiInvoker = apiInvoker;
        this.gcpServiceProject = gcpServiceProject;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void hydrateAllCaches() {

        String scope = gcpServiceProject.getAppDeploymentScope();
        if (scope == null || scope.equalsIgnoreCase("global")) {
            // This is global scope application.
            // Therefore, only hydrate the cache for the default space.
            hydrateAllCachesForOneSpace(null);
        } else {
            // This is space specific application.
            // Therefore, hydrate the cache for all spaces.
            log.info("Hydrating all caches across all spaces");
            GetAllSpacesResponse spaces = loadSpaceDetails(gcpServiceProject.getDu());

            if (spaces != null && spaces.getData() != null && !spaces.getData().isEmpty()) {
                for (SpaceResponse space : spaces.getData()) {
                    hydrateAllCachesForOneSpace(space.getSpaceId());
                }
            }
        }
    }

    private void hydrateAllCachesForOneSpace(String spaceId) {
        if (spaceId == null) {
            log.info("Hydrating all caches global scope");
        } else {
            log.info("Hydrating all caches for space ID: {}", spaceId);
        }
        for (CacheRepository<?, ?> cacheRepo : cacheRepos) {
            taskExecutor.execute(() -> cacheRepo.hydrateCache(spaceId)); // Non-blocking hydration
        }
    }

    private GetAllSpacesResponse loadSpaceDetails(String duId) {
        log.debug("Loading space details for duId: {}", duId);
        PlatformDeploymentUnitsApi deploymentUnitsApi = new PlatformDeploymentUnitsApi();

        Object result = apiInvoker.invoke(
                deploymentUnitsApi.getApiClient(),
                () -> deploymentUnitsApi.getAllDeploymentUnitsSpaces(duId, 0, 99));

        log.debug("output of getDeploymentUnitsSpace: {}", result);

        return objectMapper.convertValue(result, GetAllSpacesResponse.class);
    }

}
