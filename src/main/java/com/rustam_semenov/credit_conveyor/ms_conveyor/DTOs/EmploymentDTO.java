package com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmploymentDTO {

    private final Enum employmentStatus;

    private final String employerINN;

    private final BigDecimal salary;

    private final Enum position;

    private final Integer workExperienceTotal;

    private final Integer workExperienceCurrent;

}
