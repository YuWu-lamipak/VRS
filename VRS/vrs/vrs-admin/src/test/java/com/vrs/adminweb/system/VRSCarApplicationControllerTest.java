package com.vrs.adminweb.system;

import com.vrs.common.core.domain.AjaxResult;
import com.vrs.system.domain.VRSCarApplication;
import com.vrs.system.service.IVRSCarApplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 车辆预约控制器单元测试
 */
@RunWith(MockitoJUnitRunner.class)
public class VRSCarApplicationControllerTest {

    @Mock
    private IVRSCarApplicationService VRSCarApplicationService;

    @InjectMocks
    private VRSCarApplicationController controller;

    private VRSCarApplication testApplication;

    @Before
    public void setUp() {
        testApplication = new VRSCarApplication();
        testApplication.setApplicationId("TEST001");
        testApplication.setCarNumber("粤A12345");
        testApplication.setEnterStatus(1);
    }

    @Test
    public void testEnterOrOut_WhenCarAlreadyEntered() {
        // Given
        when(VRSCarApplicationService.selectCarNumberByApplicationId("TEST001"))
            .thenReturn("粤A12345");
        when(VRSCarApplicationService.checkEnterStatusInfoExists("粤A12345"))
            .thenReturn(true);

        // When
        AjaxResult result = controller.enterOrOut(testApplication);

        // Then
        assertEquals(500, result.get("code"));
        assertTrue(result.get("msg").toString().contains("已进厂"));
        verify(VRSCarApplicationService, times(1)).selectCarNumberByApplicationId("TEST001");
        verify(VRSCarApplicationService, times(1)).checkEnterStatusInfoExists("粤A12345");
    }

    @Test
    public void testEnterOrOut_WhenCarNotEntered() {
        // Given
        when(VRSCarApplicationService.selectCarNumberByApplicationId("TEST001"))
            .thenReturn("粤A12345");
        when(VRSCarApplicationService.checkEnterStatusInfoExists("粤A12345"))
            .thenReturn(false);
        when(VRSCarApplicationService.updateVRSCarApplication(any(VRSCarApplication.class)))
            .thenReturn(1);

        // When
        AjaxResult result = controller.enterOrOut(testApplication);

        // Then
        assertEquals(200, result.get("code"));
    }
}
