package com.lemei.adminweb.system;

import com.lemei.common.core.domain.AjaxResult;
import com.lemei.system.domain.LmCarApplication;
import com.lemei.system.service.ILmCarApplicationService;
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
public class LmCarApplicationControllerTest {

    @Mock
    private ILmCarApplicationService lmCarApplicationService;

    @InjectMocks
    private LmCarApplicationController controller;

    private LmCarApplication testApplication;

    @Before
    public void setUp() {
        testApplication = new LmCarApplication();
        testApplication.setApplicationId("TEST001");
        testApplication.setCarNumber("粤A12345");
        testApplication.setEnterStatus(1);
    }

    @Test
    public void testEnterOrOut_WhenCarAlreadyEntered() {
        // Given
        when(lmCarApplicationService.selectCarNumberByApplicationId("TEST001"))
            .thenReturn("粤A12345");
        when(lmCarApplicationService.checkEnterStatusInfoExists("粤A12345"))
            .thenReturn(true);

        // When
        AjaxResult result = controller.enterOrOut(testApplication);

        // Then
        assertEquals(500, result.get("code"));
        assertTrue(result.get("msg").toString().contains("已进厂"));
        verify(lmCarApplicationService, times(1)).selectCarNumberByApplicationId("TEST001");
        verify(lmCarApplicationService, times(1)).checkEnterStatusInfoExists("粤A12345");
    }

    @Test
    public void testEnterOrOut_WhenCarNotEntered() {
        // Given
        when(lmCarApplicationService.selectCarNumberByApplicationId("TEST001"))
            .thenReturn("粤A12345");
        when(lmCarApplicationService.checkEnterStatusInfoExists("粤A12345"))
            .thenReturn(false);
        when(lmCarApplicationService.updateLmCarApplication(any(LmCarApplication.class)))
            .thenReturn(1);

        // When
        AjaxResult result = controller.enterOrOut(testApplication);

        // Then
        assertEquals(200, result.get("code"));
    }
}
