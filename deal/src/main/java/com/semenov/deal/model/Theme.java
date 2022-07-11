package com.semenov.deal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum Theme {
    FINISH_REGISTRATION,
    CREATE_DOCUMENTS,
    SEND_DOCUMENTS,
    SEND_SES,
    CREDIT_ISSUED,
    APPLICATION_DENIED,
}
