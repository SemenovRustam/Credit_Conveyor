package com.rustam_semenov.credit_conveyor.ms_conveyor.service;

import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.*;
import com.rustam_semenov.credit_conveyor.ms_conveyor.ValidationData.ValidateLoanApplication;
import com.rustam_semenov.credit_conveyor.ms_conveyor.exception_handling.ScoringException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class ConveyorServiceImpl implements ConveyorService {

    @Value("${base.rate}")
    Integer baseRate;
    BigDecimal INSURANCE_RATE = BigDecimal.valueOf(0.02);


    private final ValidateLoanApplication validateLoanApplication;

    public ConveyorServiceImpl(ValidateLoanApplication validateLoanApplication) {
        this.validateLoanApplication = validateLoanApplication;
    }

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("STARTED  CREATED LIST OF LOAN OFFER\n");
        return Stream.of(
                getAvailableOfferDTO(1L, true, true, loanApplicationRequestDTO),
                getAvailableOfferDTO(2L, true, false, loanApplicationRequestDTO),
                getAvailableOfferDTO(3L, false, true, loanApplicationRequestDTO),
                getAvailableOfferDTO(4L, false, false, loanApplicationRequestDTO))
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());
    }

    protected LoanOfferDTO getAvailableOfferDTO(Long applicationId, boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO loanApplicationRequestDTO) {
        validateLoanApplication.validateName(loanApplicationRequestDTO.getFirstName());
        validateLoanApplication.validateName(loanApplicationRequestDTO.getLastName());
        validateLoanApplication.validateAmount(loanApplicationRequestDTO.getAmount());
        validateLoanApplication.validateTerm(loanApplicationRequestDTO.getTerm());
        validateLoanApplication.validateAge(loanApplicationRequestDTO.getBirthdate());
        validateLoanApplication.validateEmail(loanApplicationRequestDTO.getEmail());
        validateLoanApplication.validatePassportSeries(loanApplicationRequestDTO.getPassportSeries());
        validateLoanApplication.validatePassportNumber(loanApplicationRequestDTO.getPassportNumber());

        if (loanApplicationRequestDTO.getMiddleName() != null) {
            validateLoanApplication.validateName(loanApplicationRequestDTO.getMiddleName());
        }

        BigDecimal rate = BigDecimal.valueOf(10);
        BigDecimal totalAmount = loanApplicationRequestDTO.getAmount();
        Integer term = loanApplicationRequestDTO.getTerm();
        BigDecimal margin = totalAmount.multiply((rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN))); //margin - credit coast
        rate = calculateRateBySalaryClient(isSalaryClient, rate);
        rate = calculateRateByInsurance(isInsuranceEnabled, rate);

        if (isInsuranceEnabled) {
            BigDecimal insuranceCoast = totalAmount.multiply(INSURANCE_RATE);
            totalAmount = totalAmount.add(insuranceCoast);
        }
        totalAmount = totalAmount.add(margin);
        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, term);
        log.info("CREATED LOAN OFFER");
        return new LoanOfferDTO(applicationId, loanApplicationRequestDTO.getAmount(), totalAmount.setScale(0), term, monthlyPayment, rate, isInsuranceEnabled, isSalaryClient);
    }

    @Override
    public CreditDTO calculateCreditConditions(ScoringDataDTO scoringDataDTO) {
        log.info("STARTED CALCULATE CREDIT CONDITION\n");
        BigDecimal amount = scoringDataDTO.getAmount();
        BigDecimal rate = scoringRate(scoringDataDTO);
        BigDecimal psk = calculatePSK(scoringDataDTO, rate);
        Integer term = scoringDataDTO.getTerm();
        BigDecimal monthlyPayment = calculateMonthlyPayment(psk, term);
        List<PaymentScheduleElement> paymentScheduleElements = getPaymentSchedule(amount, term, psk, monthlyPayment, rate);
        log.info("END CALCULATE CREDIT CONDITION\n");
        return new CreditDTO(amount, term, monthlyPayment, rate, psk, scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient(), paymentScheduleElements);
    }

    protected List<PaymentScheduleElement> getPaymentSchedule(BigDecimal amount, Integer term, BigDecimal psk, BigDecimal monthlyPayment, BigDecimal rate) {
        List<PaymentScheduleElement> listPaymentSchedule = new ArrayList<>();

        for (int i = 1; i < term + 1; i++) {
            LocalDate dayOfPayment = LocalDate.now().plusMonths(i);
            BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(i));
            BigDecimal interestPayment = (amount.multiply(rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN))).divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
            BigDecimal debtPayment = amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
            BigDecimal remainingDebt = psk.subtract(totalPayment);

            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement(i, dayOfPayment, totalPayment, interestPayment, debtPayment, remainingDebt);
            listPaymentSchedule.add(paymentScheduleElement);
        }
        log.info("END CREATE LIST OF  PAYMENT SCHEDULE ELEMENT");
        return listPaymentSchedule;
    }

    protected BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term) {
        log.info("END CALCULATE MONTHLY PAYMENT");
        return totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
    }

    protected BigDecimal calculatePSK(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        BigDecimal PSK = scoringDataDTO.getAmount();
        BigDecimal creditCoast = (PSK.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN)).multiply(rate);
        PSK = PSK.add(creditCoast);
        if (Boolean.TRUE.equals(scoringDataDTO.getIsInsuranceEnabled())) {
            BigDecimal insuranceCoast = scoringDataDTO.getAmount().multiply(INSURANCE_RATE);
            PSK = PSK.add(insuranceCoast);
        }
        log.info("END CALCULATE PSK");
        return PSK;
    }

    protected BigDecimal scoringRate(ScoringDataDTO scoringDataDTO) {
        BigDecimal creditRate = BigDecimal.valueOf(10);
        int age = getAge(scoringDataDTO);

        validateScoringData(scoringDataDTO, age);
        creditRate = calculateRateByInsurance(scoringDataDTO.getIsInsuranceEnabled(), creditRate);
        creditRate = calculateRateByEmployeeStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByEmploymentPosition(scoringDataDTO, creditRate);
        creditRate = calculateRateByMaritalStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByDependent(scoringDataDTO, creditRate);
        creditRate = calculateRateByGender(scoringDataDTO, creditRate, age);
        creditRate = calculateRateBySalaryClient(scoringDataDTO.getIsSalaryClient(), creditRate);
        log.info("END SCORING RATE");
        return creditRate;
    }

    protected void validateScoringData(ScoringDataDTO scoringDataDTO, int age) {
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new ScoringException("Refusal. Reason - unemployed status");
        }
        if (scoringDataDTO.getEmployment().getSalary().multiply(BigDecimal.valueOf(20)).intValue() < scoringDataDTO.getAmount().intValue()) {
            throw new ScoringException("Refusal. Amount is too much");
        }
        if (age < 20 || age > 60) {
            throw new ScoringException("Refusal. Unsuitable age");
        }
        if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < 3) {
            throw new ScoringException("Refusal. Current work experience very small");
        }
        if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < 12) {
            throw new ScoringException("Refusal. Total work experience very small");
        }
        log.info("END VALIDATE SCORING DATA SUCCESSFULLY");
    }

    protected BigDecimal calculateRateByInsurance(boolean isInsuranceEnabled, BigDecimal rate) {
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(3));
        }
        log.info("END CALCULATE  RATE BY INSURANCE");
        return rate;
    }

    protected BigDecimal calculateRateBySalaryClient(boolean isSalaryClient, BigDecimal rate) {
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(2));
        }
        log.info("END CALCULATE  RATE BY SALARY CLIENT");
        return rate;
    }

    protected BigDecimal calculateRateByDependent(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        if (scoringDataDTO.getDependentAmount() > 1) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.info("END CALCULATE  RATE BY DEPENDENT");
        return creditRate;
    }

    protected BigDecimal calculateRateByGender(ScoringDataDTO scoringDataDTO, BigDecimal creditRate, Integer age) {
        if (scoringDataDTO.getGender() == Gender.FEMALE && age >= 35 && age < 60) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getGender() == Gender.MALE && age >= 30 && age < 55) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getGender() == Gender.NOT_BINARY) {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        log.info("END CALCULATE  RATE BY GENDER");
        return creditRate;
    }

    protected BigDecimal calculateRateByMaritalStatus(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.MARRIED) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.DIVORCED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.info("END CALCULATE  RATE BY MARITAL STATUS");
        return creditRate;
    }

    protected BigDecimal calculateRateByEmploymentPosition(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        if (scoringDataDTO.getEmployment().getPosition() == Position.MANAGER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(2));
        }
        if (scoringDataDTO.getEmployment().getPosition() == Position.TOP_MANAGER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(4));
        }
        log.info("END CALCULATE  RATE BY EMPLOYMENT POSITION");
        return creditRate;
    }

    protected BigDecimal calculateRateByEmployeeStatus(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.DIRECTOR) {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        log.info("END CALCULATE  RATE BY EMPLOYEE STATUS");
        return creditRate;
    }

    protected int getAge(ScoringDataDTO scoringDataDTO) {
        log.info("STARTED COUNTING AGE");
        return Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
    }
}
