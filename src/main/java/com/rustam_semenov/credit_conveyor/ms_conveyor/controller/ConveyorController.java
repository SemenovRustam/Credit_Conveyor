package com.rustam_semenov.credit_conveyor.ms_conveyor.controller;

import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.LoanApplicationRequestDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.LoanOfferDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.service.ConveyorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ConveyorController {

    private final ConveyorService conveyorService;

    public ConveyorController(ConveyorService conveyorService) {
        this.conveyorService = conveyorService;
    }

    @PostMapping("/conveyor/offers")
    public ResponseEntity<List<LoanOfferDTO>> getLoanOffer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO){
        final List<LoanOfferDTO> offerDTOList = conveyorService.getLoanOffers(loanApplicationRequestDTO);
        return  new ResponseEntity<>(offerDTOList, HttpStatus.OK);
    }
}
