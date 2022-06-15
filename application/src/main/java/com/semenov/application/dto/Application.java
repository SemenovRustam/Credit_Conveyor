package com.semenov.application.dto;

import com.semenov.application.model.ApplicationHistory;
import com.semenov.application.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Application {

    private Long id;

    private Client client;

    private Credit credit;

    private Status status;

    private LocalDate creationDate;

    private LoanOfferDTO appliedOffer;

    private LocalDate singDate;

    private Integer sesCode;

    private List<ApplicationHistory> statusHistory;
}
