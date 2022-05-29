package com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScoringDataDTO {
    private final BigDecimal amount;
    private final Integer term;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final Gender gender;
    private final LocalDate birthdate;
    private final String passportSeries;
    private final String passportNumber;
    private final LocalDate passportIssueDate;
    private final String passportIssueBranch;
    private final MaritalStatus maritalStatus;
    private final Integer dependentAmount;
    private final EmploymentDTO employment;
    private final String account;
    private final Boolean isInsuranceEnabled;
    private final Boolean isSalaryClient;
}
