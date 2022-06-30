package com.semenov.creditconveyor.msconveyor.service;

import com.semenov.creditconveyor.msconveyor.dto.CreditDTO;
import com.semenov.creditconveyor.msconveyor.dto.EmploymentStatus;
import com.semenov.creditconveyor.msconveyor.dto.Gender;
import com.semenov.creditconveyor.msconveyor.dto.LoanApplicationRequestDTO;
import com.semenov.creditconveyor.msconveyor.dto.LoanOfferDTO;
import com.semenov.creditconveyor.msconveyor.dto.MaritalStatus;
import com.semenov.creditconveyor.msconveyor.dto.PaymentScheduleElement;
import com.semenov.creditconveyor.msconveyor.dto.Position;
import com.semenov.creditconveyor.msconveyor.dto.ScoringDataDTO;
import com.semenov.creditconveyor.msconveyor.exceptionhandling.ScoringException;
import com.semenov.creditconveyor.msconveyor.exceptionhandling.ValidationException;
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
public class ConveyorServiceImpl implements ConveyorService {

    private static final BigDecimal INSURANCE_RATE = BigDecimal.valueOf(0.02);
    private static final BigDecimal PERCENT = BigDecimal.valueOf(100);
    private static final BigDecimal BASE_RATE = BigDecimal.TEN;
    private static final BigDecimal MAX_NUMBER_OF_SALARIES = BigDecimal.valueOf(20);
    private static final int MIN_AGE = 20;
    private static final int MAX_AGE = 60;
    private static final int MIN_CURRENT_WORK_EXPERIENCE = 3;
    private static final int MIN_TOTAL_WORK_EXPERIENCE = 12;
    private static final int ADULT = 18;
    private static final int QUANTITY_MONTHS = 12;

    @Value("${custom.rate.base}")
    private Integer baseRate;

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("STARTED  CREATED LIST OF LOAN OFFER\n");
        List<LoanOfferDTO> listLoanOfferDTO = Stream.of(
                getAvailableOfferDTO(1L, true, true, loanApplicationRequestDTO),
                getAvailableOfferDTO(2L, true, false, loanApplicationRequestDTO),
                getAvailableOfferDTO(3L, false, true, loanApplicationRequestDTO),
                getAvailableOfferDTO(4L, false, false, loanApplicationRequestDTO))
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());
        log.info("FINISHED  CREATED LIST OF LOAN OFFER\n {}", listLoanOfferDTO);
        return listLoanOfferDTO;
    }

    private LoanOfferDTO getAvailableOfferDTO(Long applicationId, boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("STARTED CREATED LOAN OFFER");

        if (Period.between(loanApplicationRequestDTO.getBirthdate(), LocalDate.now()).getYears() < ADULT) {
            throw new ValidationException("Age must be over 18");
        }
        BigDecimal rate = BASE_RATE;
        BigDecimal requestAmount = loanApplicationRequestDTO.getAmount();
        Integer term = loanApplicationRequestDTO.getTerm();
        rate = calculateRateBySalaryClient(isSalaryClient, rate);
        rate = calculateRateByInsurance(isInsuranceEnabled, rate);
        BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(QUANTITY_MONTHS), 2, RoundingMode.HALF_EVEN);
        BigDecimal creditCoast = (requestAmount.divide(PERCENT, 2, RoundingMode.HALF_EVEN)).multiply(monthlyPercent).multiply(BigDecimal.valueOf(term));

        BigDecimal totalAmount = requestAmount;
        if (isInsuranceEnabled) {
            BigDecimal insuranceCoast = requestAmount.multiply(INSURANCE_RATE);
            totalAmount = totalAmount.add(insuranceCoast);
        }
        totalAmount = totalAmount.add(creditCoast);
        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, term);
        LoanOfferDTO loanOffer = LoanOfferDTO.builder().
                applicationId(applicationId).
                requestedAmount(loanApplicationRequestDTO.getAmount())
                .totalAmount(totalAmount.setScale(0))
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.info("FINISHED CREATED LOAN OFFER");
        return loanOffer;
    }

    // https://www.sravni.ru/enciklopediya/info/formula-rascheta-kredita/   ссылка для расчета кредита

    @Override
    public CreditDTO calculateCreditConditions(ScoringDataDTO scoringDataDTO) {
        log.info("STARTED CALCULATE CREDIT CONDITION\n");
        BigDecimal amount = scoringDataDTO.getAmount();
        BigDecimal rate = scoringRate(scoringDataDTO);
        Integer term = scoringDataDTO.getTerm();
        BigDecimal psk = calculatePSK(scoringDataDTO, rate);
        BigDecimal monthlyPayment = calculateMonthlyPayment(psk, term);
        List<PaymentScheduleElement> paymentScheduleElements = getPaymentSchedule(amount, term, psk, monthlyPayment, rate);
        log.info("FINISHED CALCULATE CREDIT CONDITION\n");
        return new CreditDTO(amount, term, monthlyPayment, rate, psk, scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient(), paymentScheduleElements);
    }

    private List<PaymentScheduleElement> getPaymentSchedule(BigDecimal amount, Integer term, BigDecimal psk, BigDecimal monthlyPayment, BigDecimal rate) {
        log.info("STARTED CREATE LIST OF  PAYMENT SCHEDULE ELEMENT");
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
        log.info("FINISHED CREATE LIST OF  PAYMENT SCHEDULE ELEMENT");
        return listPaymentSchedule;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term) {
        log.info("STARTED CALCULATE MONTHLY PAYMENT");
        BigDecimal monthlyPayment = totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_EVEN);
        log.info("FINISHED CALCULATE MONTHLY PAYMENT");
        return monthlyPayment;
    }

    private BigDecimal calculatePSK(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("STARTED CALCULATE PSK");
        BigDecimal monthlyPercent = rate.divide(BigDecimal.valueOf(QUANTITY_MONTHS), 2, RoundingMode.HALF_EVEN);
        BigDecimal requestAmount = scoringDataDTO.getAmount();
        BigDecimal creditCoast = (requestAmount.divide(PERCENT, 2, RoundingMode.HALF_EVEN))
                .multiply(monthlyPercent).multiply(BigDecimal.valueOf(scoringDataDTO.getTerm()));
        BigDecimal psk = requestAmount.add(creditCoast);
        if (Boolean.TRUE.equals(scoringDataDTO.getIsInsuranceEnabled())) {
            BigDecimal insuranceCoast = requestAmount.multiply(INSURANCE_RATE);
            psk = psk.add(insuranceCoast);
        }
        log.info("FINISHED CALCULATE PSK");
        return psk;
    }

    private BigDecimal scoringRate(ScoringDataDTO scoringDataDTO) {
        log.info("STARTED SCORING RATE");
        BigDecimal creditRate = BigDecimal.valueOf(baseRate);
        validateScoringData(scoringDataDTO);
        creditRate = calculateRateByInsurance(scoringDataDTO.getIsInsuranceEnabled(), creditRate);
        creditRate = calculateRateByEmployeeStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByEmploymentPosition(scoringDataDTO, creditRate);
        creditRate = calculateRateByMaritalStatus(scoringDataDTO, creditRate);
        creditRate = calculateRateByDependent(scoringDataDTO, creditRate);
        creditRate = calculateRateByGender(scoringDataDTO, creditRate);
        creditRate = calculateRateBySalaryClient(scoringDataDTO.getIsSalaryClient(), creditRate);
        log.info("FINISHED SCORING RATE");
        return creditRate;
    }

    private void validateScoringData(ScoringDataDTO scoringDataDTO) {
        log.info("STARTED VALIDATE SCORING DATA");
        List<String> listExceptionInfo = new ArrayList<>();
        int age = getAge(scoringDataDTO);
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            listExceptionInfo.add("Unemployed status");
        }
        if (scoringDataDTO.getEmployment().getSalary().multiply(MAX_NUMBER_OF_SALARIES).intValue() < scoringDataDTO.getAmount().intValue()) {
            listExceptionInfo.add("Amount is too much");
        }
        if (age < MIN_AGE || age > MAX_AGE) {
            listExceptionInfo.add("Unsuitable age");
        }
        if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < MIN_CURRENT_WORK_EXPERIENCE) {
            listExceptionInfo.add("Current work experience very small");
        }
        if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < MIN_TOTAL_WORK_EXPERIENCE) {
            listExceptionInfo.add("Total work experience very small");
        }
        if (!listExceptionInfo.isEmpty()) {
            listExceptionInfo.add(0, "Refusal: ");
            log.error("FINISHED VALIDATE SCORING DATA UNSUCCESSFULLY");
            throw new ScoringException(listExceptionInfo.toString());
        } else {
            log.info("FINISHED VALIDATE SCORING DATA SUCCESSFULLY");
        }
    }

    private BigDecimal calculateRateByInsurance(boolean isInsuranceEnabled, BigDecimal rate) {
        log.info("STARTED CALCULATE  RATE BY INSURANCE");
        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(3));
        }
        log.info("FINISHED CALCULATE  RATE BY INSURANCE");
        return rate;
    }

    private BigDecimal calculateRateBySalaryClient(boolean isSalaryClient, BigDecimal rate) {
        log.info("STARTED CALCULATE  RATE BY DEPENDENT");
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(2));
        }
        log.info("FINISHED CALCULATE  RATE BY SALARY CLIENT");
        return rate;
    }

    private BigDecimal calculateRateByDependent(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE  RATE BY DEPENDENT");
        if (scoringDataDTO.getDependentAmount() > 1) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.info("FINISHED CALCULATE  RATE BY DEPENDENT");
        return creditRate;
    }

    private BigDecimal calculateRateByGender(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE  RATE BY GENDER");
        int age = getAge(scoringDataDTO);
        if (scoringDataDTO.getGender() == Gender.FEMALE && age >= 35 && age < 60) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getGender() == Gender.MALE && age >= 30 && age < 55) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getGender() == Gender.NOT_BINARY) {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        log.info("FINISHED CALCULATE  RATE BY GENDER");
        return creditRate;
    }

    private BigDecimal calculateRateByMaritalStatus(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE  RATE BY MARITAL STATUS");
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.MARRIED) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(3));
        }
        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.DIVORCED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        log.info("FINISHED CALCULATE  RATE BY MARITAL STATUS");
        return creditRate;
    }

    private BigDecimal calculateRateByEmploymentPosition(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE  RATE BY EMPLOYMENT POSITION");
        if (scoringDataDTO.getEmployment().getPosition() == Position.WORKER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(2));
        }
        if (scoringDataDTO.getEmployment().getPosition() == Position.TOP_MANAGER) {
            creditRate = creditRate.subtract(BigDecimal.valueOf(4));
        }
        log.info("FINISHED CALCULATE  RATE BY EMPLOYMENT POSITION");
        return creditRate;
    }

    private BigDecimal calculateRateByEmployeeStatus(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        log.info("STARTED CALCULATE  RATE BY EMPLOYEE STATUS");
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            creditRate = creditRate.add(BigDecimal.ONE);
        }
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.BUSINESS_OWNER) {
            creditRate = creditRate.add(BigDecimal.valueOf(3));
        }
        log.info("FINISHED CALCULATE  RATE BY EMPLOYEE STATUS");
        return creditRate;
    }

    private int getAge(ScoringDataDTO scoringDataDTO) {
        log.info("STARTED COUNTING AGE");
        int years = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
        log.info("FINISHED COUNTING AGE");
        return years;
    }
}
