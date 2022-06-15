package com.semenov.application.dto;

import com.semenov.application.Application;
import com.semenov.application.model.Employment;
import com.semenov.application.model.Gender;
import com.semenov.application.model.MaritalStatus;
import com.semenov.application.model.Passport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Client {

    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthDate;

    private String email;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private Passport passport;

    private Employment employment;

    private String account;

    private Application application;
}
