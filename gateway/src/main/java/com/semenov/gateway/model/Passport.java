package com.semenov.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passport {

    private Integer series;

    private Integer number;

    private LocalDate issueDate;

    private String issueBranch;
}
