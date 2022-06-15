package com.semenov.deal.service;

import com.semenov.deal.client.ConveyorApplicationClient;
import com.semenov.deal.dto.CreditDTO;
import com.semenov.deal.dto.EmploymentDTO;
import com.semenov.deal.dto.FinishRegistrationRequestDTO;
import com.semenov.deal.dto.LoanApplicationRequestDTO;
import com.semenov.deal.dto.LoanOfferDTO;
import com.semenov.deal.dto.ScoringDataDTO;
import com.semenov.deal.entity.Application;
import com.semenov.deal.entity.Client;
import com.semenov.deal.entity.Credit;
import com.semenov.deal.generator.GeneratorUtills;
import com.semenov.deal.model.AdditionalServices;
import com.semenov.deal.model.CreditStatus;
import com.semenov.deal.model.Employment;
import com.semenov.deal.model.EmploymentStatus;
import com.semenov.deal.model.Gender;
import com.semenov.deal.model.MaritalStatus;
import com.semenov.deal.model.Passport;
import com.semenov.deal.model.Position;
import com.semenov.deal.model.Status;
import com.semenov.deal.repository.ApplicationRepository;
import com.semenov.deal.repository.ClientRepository;
import com.semenov.deal.repository.CreditRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DealServiceTest {

    @Mock
    private ConveyorApplicationClient conveyorApplicationClient;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private GeneratorUtills generatorUtills;

    @InjectMocks
    private DealService dealService;

    @Test
    public void getLoanOffersTest() {
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

        List<LoanOfferDTO> expectedLoanOffers = Stream.of(
                loanOfferDTO1, loanOfferDTO2, loanOfferDTO3, loanOfferDTO4
        )
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());

        when(conveyorApplicationClient.requestLoanOffer(loanApplicationRequestDTO)).thenReturn(expectedLoanOffers);

        List<LoanOfferDTO> actualLoanOffers = dealService.getLoanOffers(loanApplicationRequestDTO);

        assertEquals(expectedLoanOffers.get(0), actualLoanOffers.get(0));
    }

    @Test
    public void applyOfferTest() {
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

        Application application = Application.builder()
                .id(1L)
                .client(mock(Client.class))
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .build();

        when(applicationRepository.findById(18L)).thenReturn(Optional.of(application));

        dealService.applyOffer(loanOfferDTO);
        Optional<Application> optionalApplication = applicationRepository.findById(18L);

        Application application1 = null;
        if (optionalApplication.isPresent()) {
            application1 = optionalApplication.get();
        }
        assert application1 != null;
        assertNotNull(application1.getAppliedOffer());
    }

    @Test
    public void calculateCredit() {
        Long expectedId = 1L;
        Integer expectedTermFromCredit = 60;

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

        Client client = Client.builder()
                .id(1L)
                .firstName("ivan")
                .middleName("ivanic")
                .lastName("ivanov")
                .email("zasds@sdaf.ru")
                .application(new Application())
                .passport(new Passport())
                .birthDate(LocalDate.of(2000, 2, 20))
                .account("Sdfs")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(new Employment())
                .build();

        Application application = Application.builder()
                .id(1L)
                .client(client)
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .build();

        ScoringDataDTO scoringDataMock = mock(ScoringDataDTO.class);

        when(applicationRepository.findById(18L)).thenReturn(Optional.of(application));
        Long applicationId = 18L;


        CreditDTO creditDTO = CreditDTO.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .build();


        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .creditStatus(CreditStatus.CALCULATED)
                .additionalServices(new AdditionalServices())
                .build();

        when(modelMapper.map(creditDTO, Credit.class)).thenReturn(credit);
        when(generatorUtills.generateScoringDataDTO(finishRegistrationRequestDTO, application, client)).thenReturn(scoringDataMock);
        when(conveyorApplicationClient.requestCreditCalculation(scoringDataMock)).thenReturn(creditDTO);
        when(applicationRepository.findById(18L)).thenReturn(Optional.of(application));

        dealService.calculateCredit(finishRegistrationRequestDTO, applicationId);
        Optional<Application> optionalApplication = applicationRepository.findById(18L);

        Application application1 = null;
        if (optionalApplication.isPresent()) {
            application1 = optionalApplication.get();
        }

        assert application1 != null;
        Client actualClient = application1.getClient();
        Integer actualTerm = application1.getCredit().getTerm();

        assertEquals(expectedId, actualClient.getId());
        assertEquals(expectedTermFromCredit, actualTerm);
    }
}