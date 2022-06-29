package com.semenov.gateway.dto;


import com.semenov.gateway.model.EmploymentStatus;
import com.semenov.gateway.model.Position;
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
@Schema(description = "Трудоустройство")
public class EmploymentDTO {

    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН", example = "123456")
    private String employerINN;

    @Schema(description = "Оклад", example = "15000")
    private BigDecimal salary;

    @Schema(description = "Должность", example = "WORKER")
    private Position position;

    @Schema(description = "Общий трудовой стаж", example = "12")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий трудовой стаж", example = "3")
    private Integer workExperienceCurrent;
}
