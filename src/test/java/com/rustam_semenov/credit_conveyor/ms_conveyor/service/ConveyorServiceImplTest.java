package com.rustam_semenov.credit_conveyor.ms_conveyor.service;

import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(MockitoJUnitRunner.class)
class ConveyorServiceImplTest {

    @Spy
    @InjectMocks
    private final ConveyorServiceImpl conveyorService = new ConveyorServiceImpl();

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(conveyorService, "baseRate", 10);
    }

    @Test
    void calculateRateByInsuranceTest() {
        BigDecimal rate = BigDecimal.TEN;

        BigDecimal actualRate = conveyorService.calculateRateByInsurance(true, rate);
        BigDecimal expectedRate = BigDecimal.valueOf(8);

        Assertions.assertEquals(expectedRate, actualRate);
    }

    @Test
    void calculateCreditConditionsTest() {
        int expectedRate = 5;

        EmploymentDTO employmentDTOTest = new EmploymentDTO(EmploymentStatus.DIRECTOR,
                "inn", BigDecimal.valueOf(15000), Position.MANAGER, 13, 4);

        ScoringDataDTO scoringDataDTOTest = new ScoringDataDTO(BigDecimal.valueOf(100000), 24, "Ivan", "Ivanon", "Ivanonovich",
                Gender.MALE, LocalDate.EPOCH, "123456", "1234", LocalDate.ofEpochDay(1996-01-17), "KRSK", MaritalStatus.MARRIED,
                1, employmentDTOTest, "account", true, false);

        CreditDTO actualCreditDTO = conveyorService.calculateCreditConditions(scoringDataDTOTest);

        Assertions.assertEquals(expectedRate, actualCreditDTO.getRate().intValue());
    }

    @Test
    void getLoanOffersTest() {
        LoanApplicationRequestDTO loanAppMock = Mockito.mock(LoanApplicationRequestDTO.class);
        Mockito.when(loanAppMock.getAmount()).thenReturn(BigDecimal.valueOf(100000));
        Mockito.when(loanAppMock.getTerm()).thenReturn(60);
        Mockito.when(loanAppMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1996-01-17));

        List<LoanOfferDTO> expectedLoanOffers = Stream.of(
                new LoanOfferDTO(1L, BigDecimal.valueOf(100000), BigDecimal.valueOf(112000), 60, BigDecimal.valueOf(1866.67), BigDecimal.valueOf(6), true, true),
                new LoanOfferDTO(2L, BigDecimal.valueOf(100000), BigDecimal.valueOf(112000), 60, BigDecimal.valueOf(1866.67), BigDecimal.valueOf(10), true, false),
                new LoanOfferDTO(3L, BigDecimal.valueOf(100000), BigDecimal.valueOf(110000), 60, BigDecimal.valueOf(1833.33), BigDecimal.valueOf(11), false, true),
                new LoanOfferDTO(4L, BigDecimal.valueOf(100000), BigDecimal.valueOf(110000), 60, BigDecimal.valueOf(1833.33), BigDecimal.valueOf(15), false, false)
        )
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());
        List<LoanOfferDTO> actualLoanOffers = conveyorService.getLoanOffers(loanAppMock);

        Assertions.assertEquals(expectedLoanOffers, actualLoanOffers);
    }

    @Test
    void getAvailableOfferDTO() {
        LoanApplicationRequestDTO loanAppMock1 = Mockito.mock(LoanApplicationRequestDTO.class);
        Mockito.when(loanAppMock1.getAmount()).thenReturn(BigDecimal.valueOf(100000));
        Mockito.when(loanAppMock1.getTerm()).thenReturn(60);
        Mockito.when(loanAppMock1.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1996-01-17));

        LoanOfferDTO expectedOfferDTO = new LoanOfferDTO(1L, BigDecimal.valueOf(100000), BigDecimal.valueOf(112000).setScale(0), 60, BigDecimal.valueOf(1866.67),
                BigDecimal.valueOf(6), true, true);
        LoanOfferDTO actualOfferDTO = conveyorService.getAvailableOfferDTO(1L, true, true, loanAppMock1);

        Assertions.assertEquals(expectedOfferDTO, actualOfferDTO);
    }
}