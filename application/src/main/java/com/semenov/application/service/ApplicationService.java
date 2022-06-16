package com.semenov.application.service;

import com.semenov.application.client.DealClient;
import com.semenov.application.dto.LoanApplicationRequestDTO;
import com.semenov.application.dto.LoanOfferDTO;
import com.semenov.application.exceptionhandling.ApplicationException;
import com.semenov.application.model.EmploymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final DealClient dealClient;

    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanAppRequestDTO) {
        validateScoringData(loanAppRequestDTO);
        log.debug("try deal client get offers");
        List<LoanOfferDTO> loanOfferDTOList = dealClient.requestLoanOffer(loanAppRequestDTO);
        log.info("get list of loan offers {}", loanOfferDTOList);
        return loanOfferDTOList;
    }

    public void applyOffer(LoanOfferDTO loanOffer) {
        log.debug("try deal client apply offer");
        dealClient.applyOffer(loanOffer);
        log.info("offer {} apply successfully", loanOffer);
    }

    private void validateScoringData(LoanApplicationRequestDTO loanAppRequest) {
        log.info("STARTED  PRESCORING DATA");
        List<String> listExceptionInfo = new ArrayList<>();
        int age = getAge(loanAppRequest);

        if (!loanAppRequest.getFirstName().matches("[a-zA-Z]{2,30}")) {
            listExceptionInfo.add("Incorrect first name");
        }

        if (!loanAppRequest.getLastName().matches("[a-zA-Z]{2,30}")) {
            listExceptionInfo.add("Incorrect last name");
        }

        if (loanAppRequest.getMiddleName() != null && !loanAppRequest.getMiddleName().matches("[a-zA-Z]{2,30}")) {
            listExceptionInfo.add("Incorrect middle name");
        }

        if (age < 18 || age > 60) {
            listExceptionInfo.add("Unsuitable age");
        }

        if (loanAppRequest.getAmount().intValue() < 10000) {
            listExceptionInfo.add("Amount cannot be less than 10000");
        }

        if (loanAppRequest.getTerm() < 6) {
            listExceptionInfo.add("Term cannot be less than 6");
        }

        if (!loanAppRequest.getEmail().matches("[\\w\\.]{2,50}@[\\w\\.]{2,20}")) {
            listExceptionInfo.add("Invalid email format");
        }

        if (!loanAppRequest.getPassportSeries().matches("[\\d]{4}")) {
            listExceptionInfo.add("Passport series must be 4 digit");
        }

        if (!loanAppRequest.getPassportNumber().matches("[\\d]{6}")) {
            listExceptionInfo.add("Passport number must be 6 digit");
        }

        if (!listExceptionInfo.isEmpty()) {
            listExceptionInfo.add(0, "Refusal: ");
            log.error("FINISHED VALIDATE SCORING DATA UNSUCCESSFULLY");
            throw new ApplicationException(listExceptionInfo.toString());
        } else {
            log.info("FINISHED VALIDATE SCORING DATA SUCCESSFULLY");
        }
    }

    private int getAge(LoanApplicationRequestDTO loanAppRequest) {
        log.info("STARTED COUNTING AGE");
        int years = Period.between(loanAppRequest.getBirthdate(), LocalDate.now()).getYears();
        log.info("FINISHED COUNTING AGE");
        return years;
    }
}
