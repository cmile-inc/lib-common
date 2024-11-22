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

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cmile.serviceutil.apiinvoker.ApiInvoker;
import com.cmile.serviceutil.apiinvoker.CfgApiInvoker;
import com.cmile.serviceutil.gcp.CfgGCPProject;
import com.cmile.serviceutil.gcp.GCPServiceProject;
import com.cmile.serviceutil.metric.CfgMetricRegistry;
import com.cmile.serviceutil.metric.MetricsService;
import com.cmile.serviceutil.mongo.DynamicMongoTemplate;
import com.cmile.testutil.AbstractCommonTest;
import com.cmile.testutil.CfgMongoTest;
import com.cmile.testutil.CfgTaskExecutorTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;

@SpringBootTest(classes = {CfgMongoTest.class, CfgGCPProject.class, CfgTaskExecutorTest.class,
        CfgApiInvoker.class, CfgMetricRegistry.class})
public class GlobalCacheEagerHydrationServiceTest extends AbstractCommonTest {

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private MetricsService metricsService;

    @Autowired
    private DynamicMongoTemplate dynamicMongoTemplate;

    private List<CacheRepository<?, ?>> cacheRepos;

    @Autowired
    private ApiInvoker apiInvoker;

    @Autowired
    private GCPServiceProject gcpServiceProject;

    @Autowired
    private ObjectMapper objectMapper;

    private CacheEagerHydrationService cacheEagerHydrationService;

    @BeforeEach
    public void setUp() {
        cacheRepos = new ArrayList<>();
        cacheRepos.add(new TestCacheRepository(metricsService, dynamicMongoTemplate.getMongoTemplate(), gcpServiceProject));
        cacheEagerHydrationService = new CacheEagerHydrationService(taskExecutor, cacheRepos, apiInvoker, gcpServiceProject, objectMapper);
    }

    @Test
    public void testHydrateAllCachesGlobalScope() throws Exception {
        cacheEagerHydrationService.hydrateAllCaches();
        for (CacheRepository<?, ?> cacheRepo : cacheRepos) {
            assertTrue(cacheRepo.getAll().isEmpty(), "Cache should be hydrated in global scope.");
        }
    }

}
