package com.semenov.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentScheduleElement {
    @Schema(description = "Номер платежа", example = "1")
    private Integer number;

    @Schema(description = "Дата платежа", example = "2022-08-17")
    private LocalDate date;

    @Schema(description = "Всего к оплате ", example = "1500")
    private BigDecimal totalPayment;

    @Schema(description = "Выплата процентов ", example = "1500")
    private BigDecimal interestPayment;

    @Schema(description = "Выплата долга ", example = "2000")
    private BigDecimal debtPayment;

    @Schema(description = "Оставшийся долг ", example = "5000")
    private BigDecimal remainingDebt;
}

