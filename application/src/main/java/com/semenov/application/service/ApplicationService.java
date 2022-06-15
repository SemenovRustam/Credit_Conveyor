package com.semenov.application.service;

import com.semenov.application.client.DealClient;
import com.semenov.application.dto.LoanApplicationRequestDTO;
import com.semenov.application.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final DealClient dealClient;

    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanAppRequestDTO) {
        log.debug("try deal client get offers");
        List<LoanOfferDTO> loanOfferDTOList = dealClient.requestLoanOffer(loanAppRequestDTO);
        log.info("get list of loan offers {}", loanOfferDTOList);
        return loanOfferDTOList;
    }

    public void applyOffer(LoanOfferDTO loanOffer){
        log.debug("try deal client apply offer");
        dealClient.applyOffer(loanOffer);
        log.info("offer {} apply successfully", loanOffer);
    }
}
