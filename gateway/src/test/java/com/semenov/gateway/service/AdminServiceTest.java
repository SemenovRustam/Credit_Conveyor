package com.semenov.gateway.service;

import com.semenov.gateway.client.DealClient;
import com.semenov.gateway.entity.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private AdminService adminService;

    @Test
    public void getApplicationById() {
        Long actualId = 1L;
        Application applicationMock = mock(Application.class);

        when(dealClient.getApplicationById(actualId)).thenReturn(applicationMock);
        Application actualApp = adminService.getApplicationById(actualId);

        assertEquals(applicationMock, actualApp);
    }

    @Test
    public void getAllApplication() {
        Application appMock1 = mock(Application.class);
        Application appMock2 = mock(Application.class);
        List<Application> listAppExpected = List.of(appMock1, appMock2);

        when(dealClient.getAllApplication()).thenReturn(listAppExpected);
        List<Application> listApplicationActual = adminService.getAllApplication();

        assertEquals(listAppExpected, listApplicationActual);
    }
}