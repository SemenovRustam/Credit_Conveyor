package com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs;

import lombok.*;

import java.math.BigDecimal;


@Data
public class LoanOfferDTO {
    private final Long applicationId;
    private final BigDecimal requestedAmount;
    private final BigDecimal totalAmount;
    private final Integer term;
    private final BigDecimal monthlyPayment;
    private final BigDecimal rate;
    private final boolean isInsuranceEnabled;
    private final boolean isSalaryClient;
}
