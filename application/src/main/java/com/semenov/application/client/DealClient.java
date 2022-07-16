package com.semenov.application.client;


import com.semenov.application.dto.LoanApplicationRequestDTO;
import com.semenov.application.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "conveyor-deal-client", url = "http://deal:8090/api")
public interface DealClient {

    @PostMapping(value = "/deal/application")
    List<LoanOfferDTO> requestLoanOffer(@RequestBody LoanApplicationRequestDTO requestDTO);

    @PutMapping("/deal/offer")
    void applyOffer(@RequestBody LoanOfferDTO loanOfferDTO);
}
