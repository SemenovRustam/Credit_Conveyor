package com.semenov.application.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Семейное положение")
public enum MaritalStatus {
    @Schema(description = "Замужем/женат")
    MARRIED,

    @Schema(description = "В разводе")
    DIVORCED,
}
