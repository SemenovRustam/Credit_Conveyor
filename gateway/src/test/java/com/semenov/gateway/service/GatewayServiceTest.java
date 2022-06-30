package com.semenov.gateway.service;

import com.semenov.gateway.dto.FinishRegistrationRequestDTO;
import com.semenov.gateway.dto.LoanApplicationRequestDTO;
import com.semenov.gateway.dto.LoanOfferDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GatewayServiceTest {

    @Mock
    private GatewayService gatewayService;

    @Test
    public void getLoanOffer() {
        LoanApplicationRequestDTO loanAppMock = mock(LoanApplicationRequestDTO.class);

        gatewayService.getLoanOffer(loanAppMock);

        verify(gatewayService, times(1)).getLoanOffer(loanAppMock);
    }

    @Test
    public void applyOffer() {
        LoanOfferDTO loanOfferMock = mock(LoanOfferDTO.class);

        gatewayService.applyOffer(loanOfferMock);

        verify(gatewayService, times(1)).applyOffer(loanOfferMock);
    }

    @Test
    public void calculateCredit() {
        long appId = 91L;
        FinishRegistrationRequestDTO finishRegistrationRequestMock = mock(FinishRegistrationRequestDTO.class);

        gatewayService.calculateCredit(finishRegistrationRequestMock, appId);

        verify(gatewayService, times(1)).calculateCredit(finishRegistrationRequestMock, appId);
    }

    @Test
    public void send() {
        long appId = 91L;

        gatewayService.send(appId);

        verify(gatewayService, times(1)).send(appId);
    }

    @Test
    public void signDocumentRequest() {
        long appId = 91L;

        gatewayService.signDocumentRequest(appId);

        verify(gatewayService, times(1)).signDocumentRequest(appId);
    }

    @Test
    public void signDocument() {
        long appId = 91L;

        gatewayService.signDocument(appId);

        verify(gatewayService, times(1)).signDocument(appId);
    }
}