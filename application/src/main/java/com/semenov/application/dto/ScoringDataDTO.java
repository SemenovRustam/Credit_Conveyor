package com.semenov.application.dto;


import com.semenov.application.model.Gender;
import com.semenov.application.model.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Schema(description = "Данные для оценки")
@Builder
public class ScoringDataDTO {

    @Schema(description = "Сумма", example = "10000")
    private BigDecimal amount;

    @Schema(description = "Срок займа", example = "6")
    private Integer term;

    @Schema(description = "Имя", example = "Ivanov")
    private String firstName;

    @Schema(description = "Фамилия", example = "Petrov")
    private String lastName;

    @Schema(description = "Отчество", example = "Petrovich")
    private String middleName;

    @Schema(description = "Пол", example = "MALE")
    private Gender gender;

    @Schema(description = "Дата рождения", example = "2000-01-10")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта", example = "2000-01-10")
    private LocalDate passportIssueDate;

    @Schema(description = "Отдел выдачи паспорта", example = "Krasnoyarsk")
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "1")
    private Integer dependentAmount;

    @Schema(description = "Трудоустройство", example = "Netflix")
    private EmploymentDTO employment;

    @Schema(description = "Учетная запись", example = "telegram123")
    private String account;

    @Schema(description = "Страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент", example = "true")
    private Boolean isSalaryClient;
}
