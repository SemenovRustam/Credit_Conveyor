package com.semenov.creditconveyor.msconveyor.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Должность")
public enum Position {
    WORKER,
    MID_MANAGER,
    TOP_MANAGER,
    OWNER
}
