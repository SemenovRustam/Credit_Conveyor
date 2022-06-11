package com.semenov.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Кредит")
public class CreditDTO {

    @Schema(description = "Сумма", example = "10000")
    private BigDecimal amount;

    @Schema(description = "Срок займа", example = "6")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "1500")
    private BigDecimal monthlyPayment;

    @Schema(description = "Процент кредита", example = "15")
    private BigDecimal rate;

    @Schema(description = "Полная стоимость кредита", example = "15")
    private BigDecimal psk;

    @Schema(description = "Страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент", example = "true")
    private Boolean isSalaryClient;

    @Schema(description = "График платежей")
    private List<PaymentScheduleElement> paymentSchedule;
}
