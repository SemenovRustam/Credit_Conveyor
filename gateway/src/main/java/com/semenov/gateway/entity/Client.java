package com.semenov.gateway.entity;

import com.semenov.gateway.model.Employment;
import com.semenov.gateway.model.Gender;
import com.semenov.gateway.model.MaritalStatus;
import com.semenov.gateway.model.Passport;
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
