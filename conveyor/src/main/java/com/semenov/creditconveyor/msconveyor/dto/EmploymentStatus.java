package com.semenov.creditconveyor.msconveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Рабочий статус")
public enum EmploymentStatus {
    UNEMPLOYED,
    SELF_EMPLOYED,
    EMPLOYED,
    BUSINESS_OWNER
}
