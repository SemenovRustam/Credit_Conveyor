package com.rustam_semenov.credit_conveyor.ms_conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Трудоустройство")
public class EmploymentDTO {

    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private final EmploymentStatus employmentStatus;

    @Schema(description = "ИНН", example = "123456")
    private final String employerINN;

    @Schema(description = "Оклад", example = "15000")
    private final BigDecimal salary;

    @Schema(description = "Должность", example = "MANAGER")
    private final Position position;

    @Schema(description = "Общий трудовой стаж", example = "12")
    private final Integer workExperienceTotal;

    @Schema(description = "Текущий трудовой стаж", example = "3")
    private final Integer workExperienceCurrent;
}
