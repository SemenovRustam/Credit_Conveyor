package com.semenov.application.controller;

import com.semenov.application.dto.LoanApplicationRequestDTO;
import com.semenov.application.dto.LoanOfferDTO;
import com.semenov.application.service.ApplicationService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/application")
    @ApiOperation(value = "Получение списка кредитных предложений", notes = "Введите данные, для расчета кредитных предложений")
    public ResponseEntity<List<LoanOfferDTO>> getLoanOffer(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return new ResponseEntity<>(applicationService.getLoanOffers(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/application/offer")
    @ApiOperation(value = "Выбрать и сохранить заявку", notes = "Выберите  заявку")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        applicationService.applyOffer(loanOfferDTO);
        return  ResponseEntity.ok().build();
    }
}
