package com.rustam_semenov.credit_conveyor.ms_conveyor.ValidationData;

import com.rustam_semenov.credit_conveyor.ms_conveyor.exception_handling.ValidateException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Component
public class ValidateLoanApplication {

    public void validateName(String name) {
        if (name.length() < 2) {
            throw new ValidateException("Name length cannot be less than 2 characters");
        }

        if (name.length() > 30) {
            throw new ValidateException("Name length cannot be more than 30 characters");
        }

        if (!name.matches("[a-zA-Z]{2,30}")) {
            throw new ValidateException("The name can consist only of latin letters");
        }
    }

    public void validateAmount(BigDecimal amount) {
        if (amount.intValue() < 10000) {
            throw new ValidateException("Amount cannot be less than 10000");
        }
    }

    public void validateTerm(Integer term) {
        if (term < 6) {
            throw new ValidateException("Term cannot be less than 6");
        }
    }

    public void validateAge(LocalDate localDate) {
        if (Period.between( localDate, LocalDate.now()).getYears() < 18) {
            throw new ValidateException("Age must be over 18");
        }
    }

    public void validateEmail(String email) {
        if (!email.matches("[\\w\\.]{2,50}@[\\w\\.]{2,20}")) {
            throw new ValidateException("Not valid email");
        }
    }

    public void validatePassportSeries(String series) {
        if (!series.matches("[\\d]{4}")) {
            throw new ValidateException("Not valid passport series");
        }
    }

    public void validatePassportNumber(String number) {
        if (!number.matches("[\\d]{6}")) {
            throw new ValidateException("Not valid passport number");
        }
    }
}
