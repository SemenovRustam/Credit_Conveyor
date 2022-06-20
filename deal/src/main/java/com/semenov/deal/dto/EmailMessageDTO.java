package com.semenov.deal.dto;


import com.semenov.deal.model.Theme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
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
