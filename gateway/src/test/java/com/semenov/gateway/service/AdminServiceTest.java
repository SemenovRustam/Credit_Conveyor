package com.semenov.gateway.service;

import com.semenov.gateway.client.DealClient;
import com.semenov.gateway.dto.LoanOfferDTO;
import com.semenov.gateway.entity.Application;
import com.semenov.gateway.entity.Client;
import com.semenov.gateway.entity.Credit;
import com.semenov.gateway.model.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        Application expectedApp = Application.builder()
                .id(1L)
                .sesCode(1234)
                .creationDate(LocalDate.now())
                .client(new Client())
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .credit(new Credit())
                .singDate(LocalDate.now())
                .status(Status.DOCUMENT_SIGNED)
                .build();

        when(dealClient.getApplicationById(actualId)).thenReturn(expectedApp);
        Application actualApp = adminService.getApplicationById(actualId);

        assertEquals(expectedApp, actualApp);
    }

    @Test
    public void getAllApplication() {
        Application expectedApp1 = Application.builder()
                .id(1L)
                .sesCode(1234)
                .creationDate(LocalDate.now())
                .client(new Client())
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .credit(new Credit())
                .singDate(LocalDate.now())
                .status(Status.DOCUMENT_SIGNED)
                .build();
        Application expectedApp2 = Application.builder()
                .id(1L)
                .sesCode(1234)
                .creationDate(LocalDate.now())
                .client(new Client())
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .credit(new Credit())
                .singDate(LocalDate.now())
                .status(Status.DOCUMENT_SIGNED)
                .build();
        List<Application> listAppExpected = List.of(expectedApp1, expectedApp2);

        when(dealClient.getAllApplication()).thenReturn(listAppExpected);
        List<Application> listApplicationActual = adminService.getAllApplication();

        assertEquals(listAppExpected, listApplicationActual);
    }
}