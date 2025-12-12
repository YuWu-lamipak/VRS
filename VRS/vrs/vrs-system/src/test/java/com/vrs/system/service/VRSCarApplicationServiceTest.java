package com.vrs.system.service;

import com.vrs.system.domain.VRSCarApplication;
import com.vrs.system.mapper.VRSCarApplicationMapper;
import com.vrs.system.service.Impl.VRSCarApplicationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 车辆预约服务单元测试
 */
@RunWith(MockitoJUnitRunner.class)
public class VRSCarApplicationServiceTest {

    @Mock
    private VRSCarApplicationMapper VRSCarApplicationMapper;

    @InjectMocks
    private VRSCarApplicationServiceImpl VRSCarApplicationService;

    private VRSCarApplication testApplication;

    @Before
    public void setUp() {
        testApplication = new VRSCarApplication();
        testApplication.setApplicationId("TEST001");
        testApplication.setCarNumber("粤A12345");
        testApplication.setAppointmentStatus(1);
    }

    @Test
    public void testSelectCarNumberByApplicationId() {
        // Given
        when(VRSCarApplicationMapper.selectCarNumberByApplicationId("TEST001"))
            .thenReturn("粤A12345");

        // When
        String carNumber = VRSCarApplicationService.selectCarNumberByApplicationId("TEST001");

        // Then
        assertEquals("粤A12345", carNumber);
        verify(VRSCarApplicationMapper, times(1)).selectCarNumberByApplicationId("TEST001");
    }

    @Test
    public void testCheckEnterStatusInfoExists_WhenExists() {
        // Given
        when(VRSCarApplicationMapper.checkEnterStatusInfoExists("粤A12345"))
            .thenReturn(true);

        // When
        boolean exists = VRSCarApplicationService.checkEnterStatusInfoExists("粤A12345");

        // Then
        assertTrue(exists);
        verify(VRSCarApplicationMapper, times(1)).checkEnterStatusInfoExists("粤A12345");
    }

    @Test
    public void testCheckEnterStatusInfoExists_WhenNotExists() {
        // Given
        when(VRSCarApplicationMapper.checkEnterStatusInfoExists("粤B99999"))
            .thenReturn(false);

        // When
        boolean exists = VRSCarApplicationService.checkEnterStatusInfoExists("粤B99999");

        // Then
        assertFalse(exists);
    }

    @Test
    public void testCheckScrapAdvanceInfoExists() {
        // Given
        String name = "测试名称";
        String date = "2025-12-03";
        when(VRSCarApplicationMapper.checkScrapAdvanceInfoExists(name, date))
            .thenReturn(true);

        // When
        boolean exists = VRSCarApplicationService.checkScrapAdvanceInfoExists(name, date);

        // Then
        assertTrue(exists);
        verify(VRSCarApplicationMapper, times(1)).checkScrapAdvanceInfoExists(name, date);
    }

    @Test
    public void testInsertVRSCarApplication() {
        // Given
        when(VRSCarApplicationMapper.insertVRSCarApplication(any(VRSCarApplication.class)))
            .thenReturn(1);

        // When
        int result = VRSCarApplicationService.insertVRSCarApplication(testApplication);

        // Then
        assertEquals(1, result);
        verify(VRSCarApplicationMapper, times(1)).insertVRSCarApplication(testApplication);
    }

    @Test
    public void testUpdateVRSCarApplication() {
        // Given
        when(VRSCarApplicationMapper.updateVRSCarApplication(any(VRSCarApplication.class)))
            .thenReturn(1);

        // When
        int result = VRSCarApplicationService.updateVRSCarApplication(testApplication);

        // Then
        assertEquals(1, result);
        verify(VRSCarApplicationMapper, times(1)).updateVRSCarApplication(testApplication);
    }
}
