package com.semenov.deal.model;

import com.semenov.deal.entity.Application;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class ApplicationHistory {

    private Status status;

    private LocalDate date;
}
