package com.semenov.application.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ApplicationHistory {

    private Status status;

    private LocalDate date;
}
