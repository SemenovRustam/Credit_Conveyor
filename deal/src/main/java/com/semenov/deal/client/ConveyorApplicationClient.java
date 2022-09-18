package com.semenov.deal.client;

import com.semenov.deal.dto.CreditDTO;
import com.semenov.deal.dto.LoanApplicationRequestDTO;
import com.semenov.deal.dto.LoanOfferDTO;
import com.semenov.deal.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "conveyor-application-client", url = "http://localhost:8080/api")
public interface ConveyorApplicationClient {

    @PostMapping(value = "/conveyor/offers")
    List<LoanOfferDTO> requestLoanOffer(@RequestBody LoanApplicationRequestDTO requestDTO);

    @PostMapping(value = "/conveyor/calculation")
    CreditDTO requestCreditCalculation(@RequestBody ScoringDataDTO scoringDataDTO);
}
