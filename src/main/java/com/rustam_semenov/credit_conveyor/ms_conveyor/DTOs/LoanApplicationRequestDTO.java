package com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanApplicationRequestDTO {
    private final BigDecimal amount;
    private final Integer term;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String email;
    private final LocalDate birthdate;
    private final String passportSeries;
    private final String passportNumber;
}
