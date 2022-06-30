package com.semenov.gateway.entity;


import com.semenov.gateway.dto.PaymentScheduleElement;
import com.semenov.gateway.model.AdditionalServices;
import com.semenov.gateway.model.CreditStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credit {

    private Long id;

    private BigDecimal amount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    private List<PaymentScheduleElement> paymentSchedule;

    private AdditionalServices additionalServices;

    private CreditStatus creditStatus;
}
