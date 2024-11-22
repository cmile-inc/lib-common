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

package com.cmile.serviceutil.spacevalidator;

import com.cmile.serviceutil.auth.RequestContext;
import com.cmile.serviceutil.validators.space.SpaceCacheManager;
import com.cmile.serviceutil.validators.space.SpaceValidatorInterceptor;
import com.cmile.testutil.CfgSpaceValidatorTest;
import com.cmile.testutil.SpaceAbstractCommonTest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CfgSpaceValidatorTest.class})
public class SpaceValidatorTest extends SpaceAbstractCommonTest {

    @Autowired
    private SpaceCacheManager spaceCacheManager;

    @Autowired
    private SpaceValidatorInterceptor spaceValidatorInterceptor;

    @Test
    public void testSpaceCacheManager() {

        String expectedErrorMessage = "An error occurred while interacting with the API client. Error: An error occurred while interacting with the API client.";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            // Attempt to invoke the API
            spaceCacheManager.getCache(RequestContext.getRequestContextDetails().getSpaceId());

        });

        assertTrue(exception.getMessage().contains(expectedErrorMessage));
    }

    @Test
    public void testSpaceInterceptor_ResponseCommittedAlready() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setCommitted(true);
        Object handler = new Object();
        boolean res = spaceValidatorInterceptor.preHandle(request, response, handler);
        assertFalse(res);
    }

    @Test
    public void testSpaceInterceptor_ResponseForbidden() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Object handler = new Object();
        boolean res = spaceValidatorInterceptor.preHandle(request, response, handler);
        assertFalse(res);
    }

    @Test
    public void testSpaceInterceptor_RequestContextNotSet() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();
        RequestContext.clear();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spaceValidatorInterceptor.preHandle(request, response, new Object());
        });

        // Verify the exception message
        assertEquals("Space in requestContext not found, please set the configs properly", exception.getMessage());
    }


    @Test
    public void testSpaceInterceptor() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();

        String expectedErrorMessage = "An error occurred while interacting with the API client. Error: An error occurred while interacting with the API client.";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            spaceValidatorInterceptor.preHandle(request, response, new Object());

        });

        assertTrue(exception.getMessage().contains(expectedErrorMessage));
    }


}