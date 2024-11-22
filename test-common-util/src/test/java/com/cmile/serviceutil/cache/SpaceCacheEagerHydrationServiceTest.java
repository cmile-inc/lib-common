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

import com.cmile.platform.client.api.PlatformDeploymentUnitsApi;
import com.cmile.platform.client.model.SpaceResponse;
import com.cmile.platform.client.model.GetAllSpacesResponse;
import com.cmile.serviceutil.apiinvoker.ApiInvoker;
import com.cmile.serviceutil.apiinvoker.CfgApiInvoker;
import com.cmile.serviceutil.gcp.CfgGCPProject;
import com.cmile.serviceutil.gcp.GCPServiceProject;
import com.cmile.serviceutil.metric.CfgMetricRegistry;
import com.cmile.serviceutil.metric.MetricsService;
import com.cmile.serviceutil.mongo.DynamicMongoTemplate;
import com.cmile.testutil.CfgMongoTest;
import com.cmile.testutil.CfgTaskExecutorTest;
import com.cmile.testutil.SpaceAbstractCommonTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CfgMongoTest.class, CfgGCPProject.class, CfgTaskExecutorTest.class,
        CfgApiInvoker.class, CfgMetricRegistry.class})
public class SpaceCacheEagerHydrationServiceTest extends SpaceAbstractCommonTest {

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private MetricsService metricsService;

    @Autowired
    private DynamicMongoTemplate dynamicMongoTemplate;

    private List<CacheRepository<?, ?>> cacheRepos;

    @Mock
    private ApiInvoker apiInvoker;
    @Autowired
    private GCPServiceProject gcpServiceProject;

    @Autowired
    private ObjectMapper objectMapper;

    private CacheEagerHydrationService cacheEagerHydrationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheRepos = new ArrayList<>();
        cacheRepos.add(new TestCacheRepository(metricsService, dynamicMongoTemplate.getMongoTemplate(), gcpServiceProject));
        cacheEagerHydrationService = new CacheEagerHydrationService(taskExecutor, cacheRepos, apiInvoker, gcpServiceProject, objectMapper);
    }

    @Test
    public void testHydrateAllCachesGlobalScope() throws Exception {
        GetAllSpacesResponse allSpaces = new GetAllSpacesResponse();
        List<SpaceResponse> data = new ArrayList<>();
        SpaceResponse ds = new SpaceResponse();
        ds.setSpaceId("SP01");
        data.add(ds);
        allSpaces.setData(data);

        PlatformDeploymentUnitsApi mockDUApi = mock(PlatformDeploymentUnitsApi.class);
        when(mockDUApi.getAllDeploymentUnitsSpaces("DU01", 0, 99)).thenReturn(Mono.just(allSpaces));

        Callable<Mono<GetAllSpacesResponse>> apiMethodCallable = () -> mockDUApi.getAllDeploymentUnitsSpaces("DU01", 0, 99);

        doReturn(Mono.just(allSpaces)).when(apiInvoker).invoke(any(), any());

        cacheEagerHydrationService.hydrateAllCaches();
        for (CacheRepository<?, ?> cacheRepo : cacheRepos) {
            assertTrue(cacheRepo.getAll().isEmpty(), "Cache should be hydrated in global scope.");
        }
    }
}
