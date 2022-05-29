package com.rustam_semenov.credit_conveyor.ms_conveyor.service;

import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.*;
import com.rustam_semenov.credit_conveyor.ms_conveyor.ValidationData.ValidateLoanApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ConveyorServiceImplTest {
    ValidateLoanApplication validateLoanApplication = new ValidateLoanApplication();
    ConveyorServiceImpl conveyorService = new ConveyorServiceImpl(validateLoanApplication);

    @Test
    void calculateRateByInsuranceTest() {
        BigDecimal rate = BigDecimal.valueOf(10);

        BigDecimal actualRate = conveyorService.calculateRateByInsurance(true, rate);
        BigDecimal expectedRate = BigDecimal.valueOf(8);
        Assertions.assertEquals(expectedRate, actualRate);
    }

    @Test
    void calculateCreditConditionsTest() {
        EmploymentDTO employmentDTOTest = new EmploymentDTO(EmploymentStatus.DIRECTOR, Mockito.any(), BigDecimal.valueOf(15000), Position.MANAGER, 13, 4);
        ScoringDataDTO scoringDataDTOTest = new ScoringDataDTO(BigDecimal.valueOf(100000), 24, "Rustam", "Semenov", "Viktorovich", Gender.MALE,
                LocalDate.EPOCH, "4444", "111111", Mockito.any(), Mockito.any(), MaritalStatus.MARRIED,
                1, employmentDTOTest, Mockito.any(), true, false);

        CreditDTO actualCreditDTO = conveyorService.calculateCreditConditions(scoringDataDTOTest);

        Assertions.assertEquals(5, actualCreditDTO.getRate().intValue());
    }


    @Test
    void getLoanOffersTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO(BigDecimal.valueOf(100000), 60, "Vasiliy", "Petrov",
                "Ivanov", "zevdgfh@yandex.ru", LocalDate.EPOCH, "4444", "999333");

        List<LoanOfferDTO> expectedLoanOffers = Stream.of(
                new LoanOfferDTO(1L, BigDecimal.valueOf(100000), BigDecimal.valueOf(112000), 60, BigDecimal.valueOf(1866.67), BigDecimal.valueOf(6), true, true),
                new LoanOfferDTO(2L, BigDecimal.valueOf(100000), BigDecimal.valueOf(112000), 60, BigDecimal.valueOf(1866.67), BigDecimal.valueOf(10), true, false),
                new LoanOfferDTO(3L, BigDecimal.valueOf(100000), BigDecimal.valueOf(110000), 60, BigDecimal.valueOf(1833.33), BigDecimal.valueOf(11), false, true),
                new LoanOfferDTO(4L, BigDecimal.valueOf(100000), BigDecimal.valueOf(110000), 60, BigDecimal.valueOf(1833.33), BigDecimal.valueOf(15), false, false)
        )
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());
        List<LoanOfferDTO> actualLoanOffers = conveyorService.getLoanOffers(loanApplicationRequestDTO);

        Assertions.assertEquals(expectedLoanOffers, actualLoanOffers);
    }


    @Test
    void getAvailableOfferDTO() {
        LoanApplicationRequestDTO testLoanApplicationRequestDTO = new LoanApplicationRequestDTO(BigDecimal.valueOf(100000), 60, "Rustam", "Semenov",
                "Viktorovich", "zeevvss@yandex.ru", LocalDate.EPOCH, "4444", "999333");
        LoanOfferDTO expectedOfferDTO = new LoanOfferDTO(1L, BigDecimal.valueOf(100000), BigDecimal.valueOf(112000).setScale(0), 60, BigDecimal.valueOf(1866.67),
                BigDecimal.valueOf(6), true, true);

        LoanOfferDTO actualOfferDTO = conveyorService.getAvailableOfferDTO(1L, true, true, testLoanApplicationRequestDTO);

        System.out.println(actualOfferDTO);
        System.out.println(expectedOfferDTO);
        Assertions.assertEquals(expectedOfferDTO, actualOfferDTO);
    }


}