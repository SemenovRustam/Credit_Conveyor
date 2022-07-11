package com.semenov.dossier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Трудоустройство")
public class EmailMessageDTO {

    @Schema(description = "Адрес", example = "ivanov@yandex.ru")
    private String address;

    @Schema(description = "Название темы", example = "FINISH_REGISTRATION")
    private Theme theme;

    @Schema(description = "Номер заявки", example = "1")
    private Long applicationId;
}
