package com.semenov.deal.controller;

import com.semenov.deal.dto.FinishRegistrationRequestDTO;
import com.semenov.deal.dto.LoanApplicationRequestDTO;
import com.semenov.deal.dto.LoanOfferDTO;
import com.semenov.deal.service.DealService;
import com.semenov.deal.service.MessageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;
    private final MessageService messageService;

    @PostMapping("/deal/application")
    @ApiOperation(value = "Получение списка кредитных предложений", notes = "Введите данные, для расчета кредитных предложений")
    public ResponseEntity<List<LoanOfferDTO>> getLoanOffer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return new ResponseEntity<>(dealService.getLoanOffers(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/deal/offer")
    @ApiOperation(value = "Выбрать и сохранить заявку", notes = "Выберите  заявку")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        dealService.applyOffer(loanOfferDTO);
        messageService.finishRegistration(loanOfferDTO.getApplicationId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deal/calculate/{applicationId}")
    @ApiOperation(value = "Расчет кредита", notes = "Введите данные")
    public ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) {
        dealService.calculateCredit(finishRegistrationRequestDTO, applicationId);
        return ResponseEntity.ok().build();
    }
}
