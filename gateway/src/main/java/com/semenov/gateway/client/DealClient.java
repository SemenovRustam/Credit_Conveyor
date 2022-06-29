package com.semenov.gateway.client;


import com.semenov.gateway.dto.FinishRegistrationRequestDTO;
import com.semenov.gateway.dto.LoanApplicationRequestDTO;
import com.semenov.gateway.dto.LoanOfferDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "deal-client", url = "http://localhost:8090/api")
public interface DealClient {

    @PutMapping("/deal/calculate/{applicationId}")
    void calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId);

    @PostMapping("/deal/document/{applicationId}/send")
    void sendDocumentsRequest(@PathVariable Long applicationId);

    @PostMapping("/deal/document/{applicationId}/sign")
    void signDocumentsRequest(@PathVariable Long applicationId);

    @PostMapping("/deal/document/{applicationId}/code")
    void signDocuments(@PathVariable Long applicationId);
}
