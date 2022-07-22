package com.semenov.dossier.dto;



import com.semenov.dossier.model.Gender;
import com.semenov.dossier.model.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Schema(description = "Запрос на финальную регистрацию")
@Builder
public class FinishRegistrationRequestDTO {


    @Schema(description = "Пол", example = "MALE")
    private Gender gender;

    @Schema(description = "Семейное положение", example = "MARRIED")
    private MaritalStatus maritalStatus;


    @Schema(description = "Количество иждивенцев", example = "1")
    private Integer dependentAmount;


    @Schema(description = "Дата выдачи паспорта", example = "2000-01-10")
    private LocalDate passportIssueDate;


    @Schema(description = "Отдел выдачи паспорта", example = "Krasnoyarsk")
    private String passportIssueBranch;


    @Schema(description = "Трудоустройство", example = "Netflix")
    private EmploymentDTO employmentDTO;


    @Schema(description = "Учетная запись", example = "telegram123")
    private String account;

}
