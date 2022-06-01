package com.rustam_semenov.credit_conveyor.ms_conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Рабочий статус")
public enum EmploymentStatus {
    UNEMPLOYED,
    SELF_EMPLOYED,
    DIRECTOR
}
