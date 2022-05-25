package com.rustam_semenov.credit_conveyor.ms_conveyor.service;

import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.LoanApplicationRequestDTO;
import com.rustam_semenov.credit_conveyor.ms_conveyor.DTOs.LoanOfferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@PropertySource("classpath:application.properties")
public class ConveyorServiceImpl implements ConveyorService {

    @Value("${base.rate}")
    Integer baseRate;

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return Stream.of(
                getAvailableOfferDTO(1L,true, true, loanApplicationRequestDTO),
                getAvailableOfferDTO(2L, true, false, loanApplicationRequestDTO),
                getAvailableOfferDTO(3L, false, true, loanApplicationRequestDTO),
                getAvailableOfferDTO(4L,false, false, loanApplicationRequestDTO))
                .sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed())
                .collect(Collectors.toList());
    }

    private LoanOfferDTO getAvailableOfferDTO(Long applicationId, boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO loanApplicationRequestDTO) {
        BigDecimal rate = BigDecimal.valueOf(baseRate);
        BigDecimal totalAmount = loanApplicationRequestDTO.getAmount();
        Integer term = loanApplicationRequestDTO.getTerm();
        BigDecimal insuranceRate = BigDecimal.valueOf(0.04);


        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(2));
            insuranceRate = loanApplicationRequestDTO.getAmount().multiply(insuranceRate);
            totalAmount = totalAmount.add(insuranceRate);
        } else {
            rate = rate.add(BigDecimal.valueOf(2));
        }

        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else {
            rate = rate.add(BigDecimal.valueOf(2));
        }

        BigDecimal margin = totalAmount.multiply((rate.divide(BigDecimal.valueOf(100))));
        totalAmount = totalAmount.add(margin);
        BigDecimal monthlyPayment = totalAmount.divide(BigDecimal.valueOf(term));

        return new LoanOfferDTO(applicationId, loanApplicationRequestDTO.getAmount(), totalAmount, term, monthlyPayment, rate, isInsuranceEnabled, isSalaryClient);
    }
}
