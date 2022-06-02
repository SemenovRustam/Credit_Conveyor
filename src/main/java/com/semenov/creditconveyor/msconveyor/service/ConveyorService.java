package com.semenov.creditconveyor.msconveyor.service;

import com.semenov.creditconveyor.msconveyor.dto.CreditDTO;
import com.semenov.creditconveyor.msconveyor.dto.LoanApplicationRequestDTO;
import com.semenov.creditconveyor.msconveyor.dto.LoanOfferDTO;
import com.semenov.creditconveyor.msconveyor.dto.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    CreditDTO calculateCreditConditions(ScoringDataDTO scoringDataDTO);
}
