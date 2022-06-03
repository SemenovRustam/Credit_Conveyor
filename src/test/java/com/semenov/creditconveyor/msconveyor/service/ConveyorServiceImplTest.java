package com.semenov.creditconveyor.msconveyor.service;

import com.semenov.creditconveyor.msconveyor.dto.CreditDTO;
import com.semenov.creditconveyor.msconveyor.dto.EmploymentDTO;
import com.semenov.creditconveyor.msconveyor.dto.EmploymentStatus;
import com.semenov.creditconveyor.msconveyor.dto.Gender;
import com.semenov.creditconveyor.msconveyor.dto.LoanApplicationRequestDTO;
import com.semenov.creditconveyor.msconveyor.dto.LoanOfferDTO;
import com.semenov.creditconveyor.msconveyor.dto.MaritalStatus;
import com.semenov.creditconveyor.msconveyor.dto.Position;
import com.semenov.creditconveyor.msconveyor.dto.ScoringDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void calculateCreditConditionsTest() {
        int expectedRate = 5;

        EmploymentDTO employmentDTOTest = EmploymentDTO.builder().employmentStatus(EmploymentStatus.DIRECTOR)
                .employerINN("inn")
                .salary(BigDecimal.valueOf(15000))
                .position(Position.MANAGER)
                .workExperienceTotal(13)
                .workExperienceCurrent(4)
                .build();

        ScoringDataDTO scoringDataMock = mock(ScoringDataDTO.class);
        when(scoringDataMock.getAmount()).thenReturn(BigDecimal.valueOf(100000));
        when(scoringDataMock.getTerm()).thenReturn(24);
        when(scoringDataMock.getGender()).thenReturn(Gender.MALE);
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1996-1-17));
        when(scoringDataMock.getEmployment()).thenReturn(employmentDTOTest);
        when(scoringDataMock.getIsInsuranceEnabled()).thenReturn(true);
        when(scoringDataMock.getIsSalaryClient()).thenReturn(false);
        when(scoringDataMock.getDependentAmount()).thenReturn(1);
        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.MARRIED);

        CreditDTO actualCreditDTO = conveyorService.calculateCreditConditions(scoringDataMock);

        assertEquals(expectedRate, actualCreditDTO.getRate().intValue());
    }

    @Test
    void getLoanOffersTest() {
        LoanApplicationRequestDTO loanAppMock = mock(LoanApplicationRequestDTO.class);
        when(loanAppMock.getAmount()).thenReturn(BigDecimal.valueOf(100000));
        when(loanAppMock.getTerm()).thenReturn(60);
        when(loanAppMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1996-01-17));

        LoanOfferDTO loanOfferDTO1 = LoanOfferDTO.builder().applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(112000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(1866.67))
                .rate(BigDecimal.valueOf(6))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO loanOfferDTO2 = LoanOfferDTO.builder().applicationId(2L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(112000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(1866.67))
                .rate(BigDecimal.valueOf(10))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        LoanOfferDTO loanOfferDTO3 = LoanOfferDTO.builder().applicationId(3L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(110000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(1833.33))
                .rate(BigDecimal.valueOf(11))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO loanOfferDTO4 = LoanOfferDTO.builder().applicationId(4L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(110000))
                .term(60)
                .monthlyPayment(BigDecimal.valueOf(1833.33))
                .rate(BigDecimal.valueOf(15))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        List<LoanOfferDTO> expectedLoanOffers = Stream.of(
               loanOfferDTO1, loanOfferDTO2, loanOfferDTO3, loanOfferDTO4
        )
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());

        List<LoanOfferDTO> actualLoanOffers = conveyorService.getLoanOffers(loanAppMock);

        assertEquals(expectedLoanOffers, actualLoanOffers);
    }
}