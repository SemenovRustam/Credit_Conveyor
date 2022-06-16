package com.semenov.application.service;


import com.semenov.application.client.DealClient;
import com.semenov.application.dto.LoanApplicationRequestDTO;
import com.semenov.application.dto.LoanOfferDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceTest {

    @InjectMocks
    private ApplicationServiceTest applicationServiceTest;

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    public void getLoanOffers() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(10000))
                .firstName("Vasya")
                .middleName("Petrovich")
                .lastName("Ivanov")
                .term(60)
                .email("zacas@yas.ry")
                .birthdate(LocalDate.of(1999, 1, 11))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        LoanOfferDTO loanOfferDTO1 = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(132000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2200.00).setScale(2))
                .rate(BigDecimal.valueOf(6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO loanOfferDTO2 = LoanOfferDTO.builder().applicationId(2L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(151800))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2530.00).setScale(2))
                .rate(BigDecimal.valueOf(10))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        LoanOfferDTO loanOfferDTO3 = LoanOfferDTO.builder().applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(155200))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2586.67))
                .rate(BigDecimal.valueOf(11))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO loanOfferDTO4 = LoanOfferDTO.builder().applicationId(4L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(175000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2916.67))
                .rate(BigDecimal.valueOf(15))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        List<LoanOfferDTO> expectedLoanOffersList = Stream.of(
                loanOfferDTO1, loanOfferDTO2, loanOfferDTO3, loanOfferDTO4
        )
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());

        when(dealClient.requestLoanOffer(loanApplicationRequestDTO)).thenReturn(expectedLoanOffersList);
        List<LoanOfferDTO> actualLoanOffersList = applicationService.getLoanOffers(loanApplicationRequestDTO);

        assertEquals(expectedLoanOffersList.get(0), actualLoanOffersList.get(0));
    }

    @Test
    public void applyOffer() {
        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(18L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(132000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(2200.00))
                .rate(BigDecimal.valueOf(6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        applicationService.applyOffer(loanOfferDTO);

        verify(dealClient, times(1)).applyOffer(loanOfferDTO);
    }
}