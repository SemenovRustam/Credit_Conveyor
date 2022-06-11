package com.semenov.deal.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalServices {

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}
