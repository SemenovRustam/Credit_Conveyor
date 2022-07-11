package com.semenov.dossier.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum Theme {
    FINISH_REGISTRATION,
    CREATE_DOCUMENTS,
    SEND_DOCUMENTS,
    SEND_SES,
    CREDIT_ISSUED,
    APPLICATION_DENIED;
}
