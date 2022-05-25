package com.rustam_semenov.credit_conveyor.ms_conveyor.service;

import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.LoanApplicationRequestDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.LoanOfferDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
