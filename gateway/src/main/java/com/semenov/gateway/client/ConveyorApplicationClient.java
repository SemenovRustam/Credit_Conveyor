package com.semenov.gateway.client;


import com.semenov.gateway.dto.LoanApplicationRequestDTO;
import com.semenov.gateway.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "conveyor-application-client", url = "http://localhost:8088/api")
public interface ConveyorApplicationClient {

    @PutMapping("/application/offer")
    void applyOffer(@RequestBody LoanOfferDTO loanOfferDTO);

    @PostMapping("/application")
    List<LoanOfferDTO> getLoanOffer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

}
