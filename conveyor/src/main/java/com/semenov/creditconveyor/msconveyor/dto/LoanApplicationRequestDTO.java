package com.semenov.creditconveyor.msconveyor.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@PropertySource("classpath:application.properties")
@Schema(description = "Заявка на получение кредита", name = "Заявка")
public class LoanApplicationRequestDTO {

    @NotNull
    @DecimalMin(value = "10000", message = "Amount cannot be less than 10000")
    @Schema(description = "Запрашиваемая сумма", example = "100000")
    private final BigDecimal amount;

    @Min(value = 6, message = "Term cannot be less than 6")
    @NotNull
    @Schema(description = "Срок займа в месяцах", example = "6")
    private final Integer term;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{2,}", message = "Name length cannot be less than 2 characters. The name can consist only of latin letters.")
    @Schema(description = "Имя заемщика", example = "Ivan")
    private final String firstName;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{2,}", message = "Last name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Фамилия заемщика", example = "Ivanov")
    private final String lastName;

    @Pattern(regexp = "[a-zA-Z]{2,}", message = "<Middle name cannot be less than 2 characters. The name can consist only of latin letters")
    @Schema(description = "Отчество заемщика", example = "Petrovich")
    private final String middleName;

    @Email(message = "Not valid email")
    @Schema(description = "Электронная почта", example = "ivanov@gmail.com")
    private final String email;

    @NotNull
    @Past(message = "Date cannot be future")
    @Schema(description = "Дата рождения", example = "2000-01-10")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private final LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "[\\d]{4}", message = "Not valid passport series")
    @Schema(description = "Серия паспорта", example = "1234")
    private final String passportSeries;

    @NotNull
    @Pattern(regexp = "[\\d]{6}", message = "Not valid passport number")
    @Schema(description = "Номер паспорта", example = "123456")
    private final String passportNumber;
}
