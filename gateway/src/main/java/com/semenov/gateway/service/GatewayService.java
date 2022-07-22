package com.semenov.gateway.service;

import com.semenov.gateway.client.ConveyorApplicationClient;
import com.semenov.gateway.client.DealClient;
import com.semenov.gateway.dto.FinishRegistrationRequestDTO;
import com.semenov.gateway.dto.LoanApplicationRequestDTO;
import com.semenov.gateway.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayService {

    private final DealClient dealClient;
    private final ConveyorApplicationClient conveyorApplicationClient;

    public List<LoanOfferDTO> getLoanOffer(LoanApplicationRequestDTO requestDTO) {
        log.debug("START CREATE LIST OF LOAN OFFER ");
        List<LoanOfferDTO> loanOffer = conveyorApplicationClient.getLoanOffer(requestDTO);
        log.info("Create  list of loan offer : {}", loanOffer);
        return loanOffer;
    }

    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        conveyorApplicationClient.applyOffer(loanOfferDTO);
        log.info("OFFER {} APPLY SUCCESSFULLY", loanOfferDTO);
    }

    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        dealClient.calculateCredit(finishRegistrationRequestDTO, applicationId);
        log.info("CREDIT CALCULATE SUCCESSFULLY");
    }

    public void sendDocumentsRequest(Long applicationId) {
        dealClient.sendDocumentsRequest(applicationId);
        log.info("REQUEST DOCUMENT SEND");
    }

    public void signDocumentRequest(Long applicationId) {
        dealClient.signDocumentsRequest(applicationId);
        log.info("SIGN DOCUMENTS REQUEST");
    }

    public void signDocument(Long applicationId, Integer sesCode) {
        dealClient.signDocuments(applicationId, sesCode);
        log.info("DOCUMENTS SIGN");
    }
}
