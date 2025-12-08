package com.lemei.system.service;

import com.lemei.system.domain.LmCarApplication;
import com.lemei.system.mapper.LmCarApplicationMapper;
import com.lemei.system.service.Impl.LmCarApplicationServiceImpl;
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
public class LmCarApplicationServiceTest {

    @Mock
    private LmCarApplicationMapper lmCarApplicationMapper;

    @InjectMocks
    private LmCarApplicationServiceImpl lmCarApplicationService;

    private LmCarApplication testApplication;

    @Before
    public void setUp() {
        testApplication = new LmCarApplication();
        testApplication.setApplicationId("TEST001");
        testApplication.setCarNumber("粤A12345");
        testApplication.setAppointmentStatus(1);
    }

    @Test
    public void testSelectCarNumberByApplicationId() {
        // Given
        when(lmCarApplicationMapper.selectCarNumberByApplicationId("TEST001"))
            .thenReturn("粤A12345");

        // When
        String carNumber = lmCarApplicationService.selectCarNumberByApplicationId("TEST001");

        // Then
        assertEquals("粤A12345", carNumber);
        verify(lmCarApplicationMapper, times(1)).selectCarNumberByApplicationId("TEST001");
    }

    @Test
    public void testCheckEnterStatusInfoExists_WhenExists() {
        // Given
        when(lmCarApplicationMapper.checkEnterStatusInfoExists("粤A12345"))
            .thenReturn(true);

        // When
        boolean exists = lmCarApplicationService.checkEnterStatusInfoExists("粤A12345");

        // Then
        assertTrue(exists);
        verify(lmCarApplicationMapper, times(1)).checkEnterStatusInfoExists("粤A12345");
    }

    @Test
    public void testCheckEnterStatusInfoExists_WhenNotExists() {
        // Given
        when(lmCarApplicationMapper.checkEnterStatusInfoExists("粤B99999"))
            .thenReturn(false);

        // When
        boolean exists = lmCarApplicationService.checkEnterStatusInfoExists("粤B99999");

        // Then
        assertFalse(exists);
    }

    @Test
    public void testCheckScrapAdvanceInfoExists() {
        // Given
        String name = "测试名称";
        String date = "2025-12-03";
        when(lmCarApplicationMapper.checkScrapAdvanceInfoExists(name, date))
            .thenReturn(true);

        // When
        boolean exists = lmCarApplicationService.checkScrapAdvanceInfoExists(name, date);

        // Then
        assertTrue(exists);
        verify(lmCarApplicationMapper, times(1)).checkScrapAdvanceInfoExists(name, date);
    }

    @Test
    public void testInsertLmCarApplication() {
        // Given
        when(lmCarApplicationMapper.insertLmCarApplication(any(LmCarApplication.class)))
            .thenReturn(1);

        // When
        int result = lmCarApplicationService.insertLmCarApplication(testApplication);

        // Then
        assertEquals(1, result);
        verify(lmCarApplicationMapper, times(1)).insertLmCarApplication(testApplication);
    }

    @Test
    public void testUpdateLmCarApplication() {
        // Given
        when(lmCarApplicationMapper.updateLmCarApplication(any(LmCarApplication.class)))
            .thenReturn(1);

        // When
        int result = lmCarApplicationService.updateLmCarApplication(testApplication);

        // Then
        assertEquals(1, result);
        verify(lmCarApplicationMapper, times(1)).updateLmCarApplication(testApplication);
    }
}
