package com.semenov.gateway.controller;

import com.semenov.gateway.dto.FinishRegistrationRequestDTO;
import com.semenov.gateway.dto.LoanApplicationRequestDTO;
import com.semenov.gateway.dto.LoanOfferDTO;
import com.semenov.gateway.service.GatewayService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    @PostMapping("/gateway/application")
    @ApiOperation(value = "Получение списка кредитных предложений", notes = "Введите данные, для расчета кредитных предложений")
    public ResponseEntity<List<LoanOfferDTO>> getLoanOffer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return new ResponseEntity<>(gatewayService.getLoanOffer(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/gateway/offer")
    @ApiOperation(value = "Принять заявку", notes = "Принять заявку")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        gatewayService.applyOffer(loanOfferDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/gateway/calculate/{applicationId}")
    @ApiOperation(value = "Расчет кредита", notes = "Введите данные")
    public ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) {
        gatewayService.calculateCredit(finishRegistrationRequestDTO, applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/gateway/document/{applicationId}/send")
    @ApiOperation(value = "Запрос на отправку документов", notes = "Отправить документы")
    public ResponseEntity<Void> sendDocumentsRequest(@PathVariable Long applicationId) {
        gatewayService.send(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/gateway/document/{applicationId}/sign")
    @ApiOperation(value = "Запрос на подписание документов", notes = "Запрос на подписание документов")
    public ResponseEntity<Void> signDocumentsRequest(@PathVariable Long applicationId) {
        gatewayService.signDocumentRequest(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/gateway/document/{applicationId}/code")
    @ApiOperation(value = "Подписать документы", notes = "Подписать документы")
    public ResponseEntity<Void> signDocuments(@PathVariable Long applicationId) {
        gatewayService.signDocument(applicationId);
        return ResponseEntity.ok().build();
    }
}
