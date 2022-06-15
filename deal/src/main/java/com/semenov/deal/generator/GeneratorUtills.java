package com.semenov.deal.generator;

import com.semenov.deal.dto.CreditDTO;
import com.semenov.deal.dto.FinishRegistrationRequestDTO;
import com.semenov.deal.dto.ScoringDataDTO;
import com.semenov.deal.entity.Application;
import com.semenov.deal.entity.Client;
import com.semenov.deal.model.AdditionalServices;
import org.springframework.stereotype.Component;

@Component
public class GeneratorUtills {

    public  ScoringDataDTO generateScoringDataDTO(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Application application, Client client) {
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

    public  AdditionalServices generateAdditionalServices(CreditDTO creditDTO) {
        return AdditionalServices.builder()
                .isInsuranceEnabled(creditDTO.getIsInsuranceEnabled())
                .isSalaryClient(creditDTO.getIsSalaryClient())
                .build();
    }
}
