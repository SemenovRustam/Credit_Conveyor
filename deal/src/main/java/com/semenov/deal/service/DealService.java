package com.semenov.deal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.semenov.deal.generator.GeneratorUtills;
import com.semenov.deal.model.AdditionalServices;
import com.semenov.deal.model.ApplicationHistory;
import com.semenov.deal.model.CreditStatus;
import com.semenov.deal.model.Employment;
import com.semenov.deal.model.Passport;
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
    private final GeneratorUtills generatorUtills;


    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        Client client = mapper.map(loanApplicationRequestDTO, Client.class);

        log.debug("CREATE NEW CLIENT {}", client);

        clientRepository.save(client);
        log.debug("save  client {}", client);

        Application application = createNewApplication(client);
        log.debug("CREATE NEW APPLICATION {}", application);
        applicationRepository.save(application);
        log.info("SAVE APPLICATION IN DATABASE {}", application);

        log.debug("START CREATE LIST OF LOAN OFFER ");
        List<LoanOfferDTO> loanOfferDTO = conveyorApplicationClient.requestLoanOffer(loanApplicationRequestDTO);
        log.info("Create  list of loan offer : {}", loanOfferDTO);
        return loanOfferDTO;
    }

    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        Long applicationId = loanOfferDTO.getApplicationId();
        log.debug("GET APPLICATION ID {}", applicationId);

        Optional<Application> applicationById = applicationRepository.findById(applicationId);

        Application application;
        if (applicationById.isPresent()) {
            application = applicationById.get();
            log.debug("FIND APPLICATION BY ID {}", application);
        } else {
            log.warn("Application id not exists");
            throw new DealAppException("Application id not exists");
        }

        updateApplicationStatus(application, Status.APPROVED);
        log.debug("UPDATE APPLICATION STATUS");

        application.setAppliedOffer(loanOfferDTO);
        log.debug("SET APPLIED OFFER");

        applicationRepository.save(application);
        log.info("SAVE APPLICATION IN DATABASE {}", application);
    }

    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        Optional<Application> applicationById = applicationRepository.findById(applicationId);
        log.debug("try find application by id {}", applicationById);

        Application application;
        if (applicationById.isPresent()) {
            application = applicationById.get();
            log.debug("application{} with id {} successfully find", application, applicationId);
        } else {
            log.warn("Application id not exists");
            throw new DealAppException("Application id not exists");
        }

        Employment employment = mapper.map(finishRegistrationRequestDTO.getEmploymentDTO(), Employment.class);

        Client client = updateClientInformation(finishRegistrationRequestDTO, application, employment);
        clientRepository.save(client);
        log.info("client information update and save in to database");

        application.setClient(client);
        log.info("client save in to application");

        ScoringDataDTO scoringData = generatorUtills.generateScoringDataDTO(finishRegistrationRequestDTO, application, client);
        log.debug("create scoring data {}", scoringData);

        CreditDTO creditDTO = conveyorApplicationClient.requestCreditCalculation(scoringData);
        log.info("calculate credit conditional from feign client service");

        AdditionalServices additionalServices = generatorUtills.generateAdditionalServices(creditDTO);

        Credit credit = mapper.map(creditDTO, Credit.class);
        log.debug("map credit from creditDTO {}", credit);

        credit.setAdditionalServices(additionalServices);
        credit.setCreditStatus(CreditStatus.CALCULATED);

        creditRepository.save(credit);
        log.info("save credit in database {}", credit);

        application.setCredit(credit);
        log.debug("set credit in application {}", application);

        updateApplicationStatus(application, Status.CC_APPROVED);
        log.info("update application status");

        applicationRepository.save(application);
        log.info("save application in database {}", application);
    }

    private Client updateClientInformation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Application application, Employment employment) {
        Client client = application.getClient();
        log.info("get client {} from application", client);
        client.setGender(finishRegistrationRequestDTO.getGender());
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        client.setEmployment(employment);
        client.setAccount(finishRegistrationRequestDTO.getAccount());

        Passport passport = client.getPassport();
        passport.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        passport.setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());

        client.setPassport(passport);
        log.info("UPDATE PASSPORT CLIENT IN APPLICATION");
        return client;
    }

    private Application createNewApplication(Client client) {
        Application application = Application.builder()
                .client(client)
                .creationDate(LocalDate.now())
                .build();

        updateApplicationStatus(application, PREAPPROVAL);
        return application;
    }

    private void updateApplicationStatus(Application application, Status newStatus) {
        application.setStatus(newStatus);
        ApplicationHistory applicationHistory = ApplicationHistory.builder()
                .date(LocalDate.now())
                .status(newStatus)
                .build();

        if (application.getStatusHistory() == null) {
            application.setStatusHistory(new ArrayList<>());
        }
        application.getStatusHistory().add(applicationHistory);
    }
}
