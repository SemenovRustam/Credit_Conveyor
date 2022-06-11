package com.semenov.deal.service;

import com.semenov.deal.client.ConveyorApplicationClient;
import com.semenov.deal.dto.EmploymentDTO;
import com.semenov.deal.dto.FinishRegistrationRequestDTO;
import com.semenov.deal.dto.LoanApplicationRequestDTO;
import com.semenov.deal.dto.LoanOfferDTO;
import com.semenov.deal.entity.Application;
import com.semenov.deal.entity.Client;
import com.semenov.deal.model.EmploymentStatus;
import com.semenov.deal.model.Gender;
import com.semenov.deal.model.MaritalStatus;
import com.semenov.deal.model.Position;
import com.semenov.deal.repository.ApplicationRepository;
import com.semenov.deal.repository.ClientRepository;
import com.semenov.deal.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class DealServiceTest {

    @Autowired
    private ConveyorApplicationClient conveyorApplicationClient;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DealService dealService;

    @Test
    void getLoanOffersTest() {

//        LoanApplicationRequestDTO loanAppMock = mock(LoanApplicationRequestDTO.class);
//        when(loanAppMock.getAmount()).thenReturn(BigDecimal.valueOf(100000));
//        when(loanAppMock.getTerm()).thenReturn(60);
//        when(loanAppMock.getFirstName()).thenReturn("Ivan");
//        when(loanAppMock.getLastName()).thenReturn("Ivanov");
//        when(loanAppMock.getMiddleName()).thenReturn("Petrovich");
//        when(loanAppMock.getEmail()).thenReturn("mail@mai.ru");
//        when(loanAppMock.getBirthdate()).thenReturn(LocalDate.EPOCH);
//        when(loanAppMock.getPassportSeries()).thenReturn("1234");
//        when(loanAppMock.getPassportNumber()).thenReturn("123456");


//        LoanApplicationRequestDTO loanAppMock = mock(LoanApplicationRequestDTO.class);
//        when(loanAppMock.getAmount()).thenReturn(BigDecimal.valueOf(100000));
//        when(loanAppMock.getTerm()).thenReturn(60);
//        when(loanAppMock.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1996-01-17));


// dont worked with loanAppMock

        int expectedSizeofListWithLoanOffers = 4;

        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(60)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Petrovih")
                .email("mail@mail.ru")
                .birthdate(LocalDate.ofEpochDay(1996 - 1 - 17))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        List<LoanOfferDTO> loanOffers = dealService.getLoanOffers(loanApplicationRequestDTO);
        assertEquals(expectedSizeofListWithLoanOffers, loanOffers.size());
    }

    @Test
    void applyOfferTest() {
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

        dealService.applyOffer(loanOfferDTO);
        Optional<Application> optionalApplication = applicationRepository.findById(18L);

        Application application = null;
        if (optionalApplication.isPresent()) {
            application = optionalApplication.get();
        }
        assert application != null;
        assertNotNull(application.getAppliedOffer());
    }

    @Test
    void calculateCredit() {
        Long actualId = 36L;
        Integer actualTermFromCredit = 60;
        BigDecimal expectedRate = BigDecimal.valueOf(6);

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .employerINN("1234556")
                .salary(BigDecimal.valueOf(10000))
                .position(Position.MID_MANAGER)
                .workExperienceTotal(20)
                .workExperienceCurrent(5)
                .build();

        FinishRegistrationRequestDTO finishRegistrationRequestDTO = FinishRegistrationRequestDTO.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .passportIssueDate(LocalDate.of(2020, 1, 10))
                .passportIssueBranch("krsk")
                .employmentDTO(employmentDTO)
                .account("in100gramm")
                .build();

        Long applicationId = 18L;

        dealService.calculateCredit(finishRegistrationRequestDTO, applicationId);
        Optional<Application> optionalApplication = applicationRepository.findById(18L);

        Application application = null;
        if (optionalApplication.isPresent()) {
            application = optionalApplication.get();
        }

        assert application != null;
        Client actualClient = application.getClient();
        Integer actualTerm = application.getCredit().getTerm();
        BigDecimal actualRate = application.getAppliedOffer().getRate();

        assertEquals(actualId, actualClient.getId());
        assertEquals(actualTermFromCredit, actualTerm);
        assertEquals(expectedRate, actualRate);
    }
}