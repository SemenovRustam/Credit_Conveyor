package com.semenov.gateway.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    @Mock
    private AdminService adminService;

    @Test
    public void getApplicationById() {
        Long id = 91L;

        adminService.getApplicationById(id);

        verify(adminService, times(1)).getApplicationById(id);
    }

    @Test
    public void getAllApplication() {
        adminService.getAllApplication();

        verify(adminService, times(1)).getAllApplication();
    }
}