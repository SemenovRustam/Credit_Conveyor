package com.semenov.dossier.entity;



import com.semenov.dossier.dto.LoanOfferDTO;
import com.semenov.dossier.model.ApplicationHistory;
import com.semenov.dossier.model.Status;
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
