package com.semenov.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Заявка на получение кредита", name = "Заявка")
public class LoanApplicationRequestDTO {

    @Schema(description = "Запрашиваемая сумма", example = "100000")
    private BigDecimal amount;

    @Schema(description = "Срок займа в месяцах", example = "6")
    private Integer term;

    @Schema(description = "Имя заемщика", example = "Ivan")
    private String firstName;

    @Schema(description = "Фамилия заемщика", example = "Ivanov")
    private String lastName;

    @Schema(description = "Отчество заемщика", example = "Petrovich")
    private String middleName;

    @Schema(description = "Электронная почта", example = "ivanov@gmail.com")
    private String email;

    @Schema(description = "Дата рождения", example = "2000-01-10")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;
}
