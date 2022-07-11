package com.semenov.creditconveyor.msconveyor.controller;

import com.semenov.creditconveyor.msconveyor.dto.CreditDTO;
import com.semenov.creditconveyor.msconveyor.dto.LoanApplicationRequestDTO;
import com.semenov.creditconveyor.msconveyor.dto.LoanOfferDTO;
import com.semenov.creditconveyor.msconveyor.dto.ScoringDataDTO;
import com.semenov.creditconveyor.msconveyor.service.ConveyorService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api",  produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ConveyorController {

    private final ConveyorService conveyorService;

    @PostMapping(path = "/conveyor/offers")
    @ApiOperation(value = "Получение списка кредитных предложений", notes = "Введите данные, для расчета кредитных предложений")
    public ResponseEntity<List<LoanOfferDTO>> getLoanOffer(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return new ResponseEntity<>(conveyorService.getLoanOffers(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/conveyor/calculation")
    @ApiOperation(value = "Расчет кредита", notes = "Введите персональные данные, для расчета кредитного предложения")
    public ResponseEntity<CreditDTO> calculateCreditConditions(@RequestBody @Valid ScoringDataDTO scoringDataDTO) {
        return new ResponseEntity<>(conveyorService.calculateCreditConditions(scoringDataDTO), HttpStatus.OK);
    }
}
