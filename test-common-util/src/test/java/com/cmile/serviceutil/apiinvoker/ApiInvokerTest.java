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

package com.cmile.serviceutil.apiinvoker;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cmile.platform.client.api.PlatformDeploymentUnitsApi;
import com.cmile.platform.client.model.SpaceResponse;
import com.cmile.serviceutil.auth.RequestContext;
import com.cmile.testutil.CfgApiInvokerTest;
import com.cmile.testutil.SpaceAbstractCommonTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(classes = {CfgApiInvokerTest.class})
public class ApiInvokerTest extends SpaceAbstractCommonTest {
  private static final Logger logger = LoggerFactory.getLogger(ApiInvokerTest.class);

  @Autowired private ApiInvoker apiInvoker;
  @Autowired private WireMockServer wireMockServer;

  @BeforeEach
  public void setUp() {
    wireMockServer.start();
  }

  @AfterEach
  public void tearDown() {
    wireMockServer.stop();
  }

  @Test
  public void testApiInvoker_success() throws JsonProcessingException {
    mockDUReponse();
    PlatformDeploymentUnitsApi deploymentUnitsApi = new PlatformDeploymentUnitsApi();
    Object result =
        apiInvoker.invoke(
            deploymentUnitsApi.getApiClient(),
            () ->
                deploymentUnitsApi.getDeploymentUnitsSpace(
                    gcpServiceProject.getDu(),
                    RequestContext.getRequestContextDetails().getSpaceId()));

    logger.debug(
        "Space details for ID: {} are loaded",
        RequestContext.getRequestContextDetails().getSpaceId());
    SpaceResponse spaceResult = objectMapper.convertValue(result, SpaceResponse.class);
    assertEquals(deploymentUnitSpaces().getSpaceId(), spaceResult.getSpaceId());
  }

  @Test
  public void testApiInvoker_fail() throws JsonProcessingException {
    mockDUReponse();

    PlatformDeploymentUnitsApi deploymentUnitsApi = new PlatformDeploymentUnitsApi();
    String expectedErrorMessage =
        "An error occurred while interacting with the API client. Error: An error occurred while interacting with the API client.";

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              apiInvoker.invoke(
                  deploymentUnitsApi.getApiClient(),
                  () ->
                      deploymentUnitsApi.getDeploymentUnitsSpace(
                          gcpServiceProject.getDu(), "dummy")); // Space not exist
            });

    Assertions.assertTrue(exception.getMessage().contains(expectedErrorMessage));
  }

  private void mockDUReponse() throws JsonProcessingException {
    String basePathUrl = "/platform/deployment-units";
    String getUrl =
        String.format(
            "/%s/spaces/%s",
            gcpServiceProject.getDu(), RequestContext.getRequestContextDetails().getSpaceId());

    wireMockServer.stubFor(
        get(urlPathEqualTo(basePathUrl + getUrl))
            .inScenario("DU Management")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(
                aResponse()
                    .withHeader(
                        "Content-Type", "application/json") // Set Content-Type to application/json
                    .withBody(objectMapper.writeValueAsString(deploymentUnitSpaces()))
                    .withStatus(HttpStatus.OK.value())));
  }

  private SpaceResponse deploymentUnitSpaces() {
    SpaceResponse deploymentUnitSpaces = new SpaceResponse();
    deploymentUnitSpaces.spaceId("SP01");
    return deploymentUnitSpaces;
  }
}
