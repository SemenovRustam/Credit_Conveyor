package com.semenov.creditconveyor.msconveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Пол")
public enum Gender {
    MALE,
    FEMALE,
    NOT_BINARY
}
