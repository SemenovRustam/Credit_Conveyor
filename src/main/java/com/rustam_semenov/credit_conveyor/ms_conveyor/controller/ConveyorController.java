package com.rustam_semenov.credit_conveyor.ms_conveyor.controller;

import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.CreditDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.LoanApplicationRequestDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.LoanOfferDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.dto.ScoringDataDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.exception_handling.ValidationException;
import com.rustam_semenov.credit_conveyor.ms_conveyor.service.ConveyorService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ConveyorController {

    private final ConveyorService conveyorService;

    public ConveyorController(ConveyorService conveyorService) {
        this.conveyorService = conveyorService;
    }

    @PostMapping("/conveyor/offers")
    @ApiOperation(value = "Получение списка кредитных предложений", notes = "Введите данные, для расчета кредитных предложений")
    public ResponseEntity<List<LoanOfferDTO>> getLoanOffer(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO, BindingResult bindingResult) {
        catchErrors(bindingResult);
        return new ResponseEntity<>(conveyorService.getLoanOffers(loanApplicationRequestDTO), HttpStatus.OK);
    }


    @PostMapping("/conveyor/calculation")
    @ApiOperation(value = "Расчет кредита", notes = "Введите персональные данные, для расчета кредитного предложения")
    public ResponseEntity<CreditDTO> calculateCreditConditions(@RequestBody @Valid ScoringDataDTO scoringDataDTO, BindingResult bindingResult) {
        catchErrors(bindingResult);
        return new ResponseEntity<>(conveyorService.calculateCreditConditions(scoringDataDTO), HttpStatus.OK);
    }

    private void catchErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = String.format("%s : %s", bindingResult.getFieldError().getField(), bindingResult.getFieldError().getDefaultMessage());
            log.info(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
