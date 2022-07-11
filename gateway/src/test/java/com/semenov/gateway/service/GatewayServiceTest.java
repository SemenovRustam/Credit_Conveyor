package com.semenov.gateway.service;

import com.semenov.gateway.client.ConveyorApplicationClient;
import com.semenov.gateway.client.DealClient;
import com.semenov.gateway.dto.EmploymentDTO;
import com.semenov.gateway.dto.FinishRegistrationRequestDTO;
import com.semenov.gateway.dto.LoanApplicationRequestDTO;
import com.semenov.gateway.dto.LoanOfferDTO;
import com.semenov.gateway.model.Gender;
import com.semenov.gateway.model.MaritalStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
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
        LoanApplicationRequestDTO loanAppRequest = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(10000))
                .firstName("Vasya")
                .middleName("Petrovich")
                .lastName("Ivanov")
                .term(60)
                .email("vasya@mail.ru")
                .birthdate(LocalDate.of(1999, 1, 11))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        LoanOfferDTO loanOffer1 = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(132000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2200.00))
                .rate(BigDecimal.valueOf(6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO loanOffer2 = LoanOfferDTO.builder().applicationId(2L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(151800))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2530.00))
                .rate(BigDecimal.valueOf(10))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        List<LoanOfferDTO> listLoanOfferExpected = List.of(loanOffer1, loanOffer2);

        when(conveyorAppClient.getLoanOffer(loanAppRequest)).thenReturn(listLoanOfferExpected);
        List<LoanOfferDTO> listLoanOfferActual = gatewayService.getLoanOffer(loanAppRequest);

        assertEquals(listLoanOfferExpected, listLoanOfferActual);
    }

    @Test
    public void applyOffer() {
        long expectedAppId = 1L;
        LoanOfferDTO loanOffer = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(132000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2200.00))
                .rate(BigDecimal.valueOf(6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        gatewayService.applyOffer(loanOffer);

        verify(conveyorAppClient, times(1)).applyOffer(
                argThat(offer -> offer.getApplicationId().equals(expectedAppId))
        );
    }

    @Test
    public void calculateCredit() {
        long expectedAppId = 91L;
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = FinishRegistrationRequestDTO.builder()
                .account("account")
                .employmentDTO(new EmploymentDTO())
                .dependentAmount(1)
                .maritalStatus(MaritalStatus.MARRIED)
                .gender(Gender.MALE)
                .passportIssueBranch("1344")
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