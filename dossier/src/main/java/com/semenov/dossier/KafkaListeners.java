package com.semenov.dossier;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = {"finish-registration"
            , "create-documents"
            , "send-documents"
            , "send-ses"
            , "credit-issued"
            , "application-denied"}
            , groupId = "deal")
    void listener(String data) {
        System.out.println("Listener received: " + data + " :)");
    }

}
