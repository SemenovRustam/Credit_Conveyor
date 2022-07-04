package com.semenov.gateway.service;

import com.semenov.gateway.client.ConveyorApplicationClient;
import com.semenov.gateway.client.DealClient;
import com.semenov.gateway.dto.FinishRegistrationRequestDTO;
import com.semenov.gateway.dto.LoanApplicationRequestDTO;
import com.semenov.gateway.dto.LoanOfferDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GatewayServiceTest {

    @Mock
    private DealClient dealClient;

    @Mock
    private ConveyorApplicationClient conveyorAppClient;

    @InjectMocks
    private GatewayService gatewayService;

    @Test
    public void getLoanOffer() {
        LoanApplicationRequestDTO loanAppMock = mock(LoanApplicationRequestDTO.class);
        LoanOfferDTO loanOfferMock1 = mock(LoanOfferDTO.class);
        LoanOfferDTO loanOfferMock2 = mock(LoanOfferDTO.class);
        List<LoanOfferDTO> listLoanOfferExpected = List.of(loanOfferMock1, loanOfferMock2);

        when(conveyorAppClient.getLoanOffer(loanAppMock)).thenReturn(listLoanOfferExpected);
        List<LoanOfferDTO> listLoanOfferActual = gatewayService.getLoanOffer(loanAppMock);

        assertEquals(listLoanOfferExpected, listLoanOfferActual);
    }

    @Test
    public void applyOffer() {
        long expectedAppId = 91L;

        LoanOfferDTO offerDTO = LoanOfferDTO.builder()
                .applicationId(91L)
                .build();

        gatewayService.applyOffer(offerDTO);

        verify(conveyorAppClient, times(1)).applyOffer(argThat(offer -> offer.getApplicationId().equals(expectedAppId)));
    }

    @Test
    public void calculateCredit() {
        long expectedAppId = 91L;

        FinishRegistrationRequestDTO finishRegistrationRequestDTO = FinishRegistrationRequestDTO.builder()
                .passportIssueDate(LocalDate.now())
                .build();

        gatewayService.calculateCredit(finishRegistrationRequestDTO, expectedAppId);

        verify(dealClient, times(1))
                .calculateCredit(
                        argThat(finishRegistrationRequestDTO1 -> finishRegistrationRequestDTO1.getPassportIssueDate().equals(LocalDate.now())),
                        argThat((Long id) -> id.equals(expectedAppId))
                );
    }

    @Test
    public void sendDocumentsRequest() {
        long appIdExpected = 91L;

        gatewayService.sendDocumentsRequest(appIdExpected);

        verify(dealClient, times(1))
                .sendDocumentsRequest(
                        argThat(applicationId -> applicationId.equals(appIdExpected))
                );
    }

    @Test
    public void signDocumentRequest() {
        long appIdExpected = 91L;

        gatewayService.signDocumentRequest(appIdExpected);

        verify(dealClient, times(1)).signDocumentsRequest(
                argThat(applicationId -> applicationId.equals(appIdExpected))
        );
    }

    @Test
    public void signDocument() {
        long appIdExpected = 91L;

        gatewayService.signDocument(appIdExpected);

        verify(dealClient, times(1)).signDocuments(
                argThat(applicationId -> applicationId.equals(appIdExpected))
        );
    }
}