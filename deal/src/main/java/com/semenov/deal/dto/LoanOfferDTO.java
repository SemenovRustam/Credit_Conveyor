package com.semenov.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class LoanOfferDTO {

    @Schema(description = "Номер кредитного предложения", example = "1")
    private Long applicationId;

    @Schema(description = "Запрашиваемая сумма", example = "10000")
    private BigDecimal requestedAmount;

    @Schema(description = "Итоговая сумма", example = "15000")
    private BigDecimal totalAmount;

    @Schema(description = "Срок займа в месяцах", example = "10")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "1500")
    private BigDecimal monthlyPayment;

    @Schema(description = "Процент кредита", example = "15")
    private BigDecimal rate;

    @Schema(description = "Страховка", example = "true")
    private boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент", example = "true")
    private boolean isSalaryClient;
}

