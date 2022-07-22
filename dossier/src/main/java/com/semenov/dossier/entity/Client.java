package com.semenov.dossier.entity;


import com.semenov.dossier.model.Employment;
import com.semenov.dossier.model.Gender;
import com.semenov.dossier.model.MaritalStatus;
import com.semenov.dossier.model.Passport;
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
