package com.rustam_semenov.credit_conveyor.ms_conveyor.service;

import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.CreditDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.LoanApplicationRequestDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.LoanOfferDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    CreditDTO calculateCreditConditions(ScoringDataDTO scoringDataDTO);
}
