package com.cmile.serviceutil.spacevalidator;

import com.cmile.serviceutil.apiinvoker.ApiInvoker;
import com.cmile.serviceutil.gcp.GCPServiceProject;
import com.cmile.serviceutil.validators.space.SpaceCacheManager;
import com.cmile.serviceutil.validators.space.SpacePlatformService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpaceCacheManagerTest {

    @Mock
    private GCPServiceProject gcpServiceProject;

    @Mock
    private ApiInvoker apiInvoker;

    @Mock
    private SpacePlatformService platformService;

    private SpaceCacheManager spaceCacheManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        spaceCacheManager = new SpaceCacheManager(
            gcpServiceProject,
            apiInvoker,
            objectMapper,
            platformService
        );
    }

    @Test
    public void testLoadSpaceDetails_Success() {
        String spaceId = "test-space-id";
        Object mockSpaceDetails = new Object();

        when(platformService.fetchSpaceDetails()).thenReturn(mockSpaceDetails);

        Object result = spaceCacheManager.getCache(spaceId);
        assertNotNull(result);
        assertEquals(mockSpaceDetails, result);

        verify(platformService, times(1)).fetchSpaceDetails();
    }

    @Test
    public void testLoadSpaceDetails_Error() {
        String spaceId = "test-space-id";

        when(platformService.fetchSpaceDetails())
            .thenThrow(new RuntimeException("Failed to fetch space details"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spaceCacheManager.getCache(spaceId);
        });

        assertEquals("Failed to fetch space details", exception.getMessage());
        verify(platformService, times(1)).fetchSpaceDetails();
    }
}
