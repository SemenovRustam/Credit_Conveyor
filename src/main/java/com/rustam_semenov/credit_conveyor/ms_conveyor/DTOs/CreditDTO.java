package com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreditDTO {
    private final BigDecimal amount;
    private final Integer term;
    private final BigDecimal monthlyPayment;
    private final BigDecimal rate;
    private final BigDecimal psk;
    private final Boolean isInsuranceEnabled;
    private final Boolean isSalaryClient;
    private final List<PaymentScheduleElement> paymentSchedule;
}
