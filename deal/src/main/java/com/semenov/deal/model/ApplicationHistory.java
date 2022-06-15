package com.semenov.deal.model;

import com.semenov.deal.entity.Application;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
public class ApplicationHistory {

    private Status status;

    private LocalDate date;
}
