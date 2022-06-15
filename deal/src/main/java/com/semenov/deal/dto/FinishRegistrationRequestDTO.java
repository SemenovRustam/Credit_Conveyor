package com.semenov.deal.dto;

import com.semenov.deal.model.Gender;
import com.semenov.deal.model.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Schema(description = "Запрос на финальную регистрацию")
@Builder
public class FinishRegistrationRequestDTO {

    @NotNull
    @Schema(description = "Пол", example = "MALE")
    private Gender gender;

    @NotNull
    @Schema(description = "Семейное положение", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @NotNull
    @Schema(description = "Количество иждивенцев", example = "1")
    private Integer dependentAmount;

    @NotNull
    @Schema(description = "Дата выдачи паспорта", example = "2000-01-10")
    private LocalDate passportIssueDate;

    @NotNull
    @Schema(description = "Отдел выдачи паспорта", example = "Krasnoyarsk")
    private String passportIssueBranch;

    @NotNull
    @Schema(description = "Трудоустройство", example = "Netflix")
    private EmploymentDTO employmentDTO;

    @NotNull
    @Schema(description = "Учетная запись", example = "telegram123")
    private String account;

}
