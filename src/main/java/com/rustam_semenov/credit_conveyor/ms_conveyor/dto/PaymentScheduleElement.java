package com.rustam_semenov.credit_conveyor.ms_conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Расчетный лист")
public class PaymentScheduleElement {
    @Schema(description = "Номер платежа", example = "1")
    private final Integer number;

    @Schema(description = "Дата платежа", example = "2022-08-17")
    private final LocalDate date;

    @Schema(description = "Всего к оплате ", example = "1500")
    private final BigDecimal totalPayment;

    @Schema(description = "Выплата процентов ", example = "1500")
    private final BigDecimal interestPayment;

    @Schema(description = "Выплата долга ", example = "2000")
    private final BigDecimal debtPayment;

    @Schema(description = "Оставшийся долг ", example = "5000")
    private final BigDecimal remainingDebt;
}
