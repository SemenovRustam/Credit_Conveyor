package com.semenov.deal.service;

import com.semenov.deal.client.ConveyorApplicationClient;
import com.semenov.deal.dto.CreditDTO;
import com.semenov.deal.dto.FinishRegistrationRequestDTO;
import com.semenov.deal.dto.LoanApplicationRequestDTO;
import com.semenov.deal.dto.LoanOfferDTO;
import com.semenov.deal.dto.ScoringDataDTO;
import com.semenov.deal.entity.Application;
import com.semenov.deal.entity.Client;
import com.semenov.deal.entity.Credit;
import com.semenov.deal.exceptionhandling.DealAppException;
import com.semenov.deal.model.AdditionalServices;
import com.semenov.deal.model.ApplicationHistory;
import com.semenov.deal.model.CreditStatus;
import com.semenov.deal.model.Employment;
import com.semenov.deal.model.Status;
import com.semenov.deal.repository.ApplicationRepository;
import com.semenov.deal.repository.ClientRepository;
import com.semenov.deal.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.semenov.deal.model.Status.PREAPPROVAL;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealService {

    private final ConveyorApplicationClient conveyorApplicationClient;
    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final ModelMapper mapper;

    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        Client client = mapper.map(loanApplicationRequestDTO, Client.class);
        log.info("CREATE NEW CLIENT {}", client);

        clientRepository.save(client);
        log.info("save  client {}", client);

        Application application = createNewApplication(client);
        log.info("CREATE NEW APPLICATION {}", application);
        applicationRepository.save(application);
        log.info("SAVE APPLICATION IN DATABASE {}", application);

        log.info("START CREATE LIST OF LOAN OFFER ");
        List<LoanOfferDTO> loanOfferDTO = conveyorApplicationClient.requestLoanOffer(loanApplicationRequestDTO);
        log.info("Create  list of loan offer : {}", loanOfferDTO);
        return loanOfferDTO;
    }

    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        Long applicationId = loanOfferDTO.getApplicationId();
        log.info("GET APPLICATION ID {}", applicationId);

        Optional<Application> applicationById = applicationRepository.findById(applicationId);

        Application application;
        if (applicationById.isPresent()) {
            application = applicationById.get();
            log.info("FIND APPLICATION BY ID {}", application);
        } else {
            log.warn("Application id not exists");
            throw new DealAppException("Application id not exists");
        }

        updateApplicationStatus(application, Status.APPROVED);
        log.info("UPDATE APPLICATION STATUS");

        application.setAppliedOffer(loanOfferDTO);
        log.info("SET APPLIED OFFER");

        applicationRepository.save(application);
        log.info("SAVE APPLICATION IN DATABASE {}", application);
    }

    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        Optional<Application> applicationById = applicationRepository.findById(applicationId);
        log.info("try find application by id {}", applicationById);

        Application application;
        if (applicationById.isPresent()) {
            application = applicationById.get();
            log.info("application{} with id {} successfully find", application, applicationId);
        } else {
            log.warn("Application id not exists");
            throw new DealAppException("Application id not exists");
        }

        Employment employment = mapper.map(finishRegistrationRequestDTO.getEmploymentDTO(), Employment.class);

        Client client = getClient(finishRegistrationRequestDTO, application, employment);
        clientRepository.save(client);
        log.info("client information update and save in to database");

        application.setClient(client);
        log.info("client save in to application");

        ScoringDataDTO scoringData = getScoringDataDTO(finishRegistrationRequestDTO, application, client);
        log.info("create scoring data {}", scoringData);

        CreditDTO creditDTO = conveyorApplicationClient.requestCreditCalculation(scoringData);
        log.info("calculate credit conditional from feign client service");

        AdditionalServices additionalServices = getAdditionalServices(creditDTO);

        Credit credit = mapper.map(creditDTO, Credit.class);
        log.info("map credit from creditDTO {}", credit);

        credit.setAdditionalServices(additionalServices);
        credit.setCreditStatus(CreditStatus.CALCULATED);

        creditRepository.save(credit);
        log.info("save credit in database {}", credit);

        application.setCredit(credit);
        log.info("set credit in application {}", application);

        updateApplicationStatus(application, Status.CC_APPROVED);
        log.info("update application status");

        applicationRepository.save(application);
        log.info("save application in database {}", application);
    }

    private AdditionalServices getAdditionalServices(CreditDTO creditDTO) {
        return AdditionalServices.builder()
                .isInsuranceEnabled(creditDTO.getIsInsuranceEnabled())
                .isSalaryClient(creditDTO.getIsSalaryClient())
                .build();
    }

    private ScoringDataDTO getScoringDataDTO(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Application application, Client client) {
        return ScoringDataDTO.builder()
                .amount(application.getAppliedOffer().getTotalAmount())
                .term(application.getAppliedOffer().getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistrationRequestDTO.getGender())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getSeries().toString())
                .passportNumber(client.getPassport().getNumber().toString())
                .passportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .employment(finishRegistrationRequestDTO.getEmploymentDTO())
                .account(finishRegistrationRequestDTO.getAccount())
                .isInsuranceEnabled(application.getAppliedOffer().isInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().isSalaryClient())
                .build();
    }

    private Client getClient(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Application application, Employment employment) {
        Client client = application.getClient();
        log.info("get client {} from application", client);
        client.setGender(finishRegistrationRequestDTO.getGender());
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        client.setEmployment(employment);
        client.setAccount(finishRegistrationRequestDTO.getAccount());

        return client;
    }

    private Application createNewApplication(Client client) {
        Application application = new Application();
        application.setClient(client);
        application.setCreationDate(LocalDate.now());

        updateApplicationStatus(application, PREAPPROVAL);
        return application;
    }

    private void updateApplicationStatus(Application application, Status newStatus) {
        application.setStatus(newStatus);
        ApplicationHistory newHistory = new ApplicationHistory();
        newHistory.setDate(LocalDate.now());
        newHistory.setStatus(newStatus);

        if (application.getStatusHistory() == null) {
            application.setStatusHistory(new ArrayList<>());
        }
        application.getStatusHistory().add(newHistory);
    }
}
