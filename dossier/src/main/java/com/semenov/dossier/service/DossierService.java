package com.semenov.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semenov.dossier.client.DealClient;
import com.semenov.dossier.dto.EmailMessageDTO;
import com.semenov.dossier.entity.Application;
import com.semenov.dossier.model.Theme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierService {

    private final DocumentService documentService;
    private final MailService mailService;
    private final ObjectMapper objectMapper;
    private final DealClient dealClient;

    @KafkaListener(topics = "finish-registration", groupId = "deal")
    public void finishRegistrationMessage(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Theme theme = emailMessageDTO.getTheme();

        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        Application application = dealClient.getApplicationById(emailMessageDTO.getApplicationId());
        String text = String.format(
                "Dear %s, finish registration your application id-%s, please.", application.getClient().getFirstName(), application.getId()
        );
        String address = emailMessageDTO.getAddress();
        log.info("Dossier service try a send message for {}", address);

        mailService.sendMessage(address, text);

        log.info("Message for {} successfully delivered", address);

    }

    @KafkaListener(topics = "create-documents", groupId = "deal")
    public void sendDocumentRequest(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Theme theme = emailMessageDTO.getTheme();

        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        Application application = dealClient.getApplicationById(emailMessageDTO.getApplicationId());
        String address = emailMessageDTO.getAddress();

        String messageText = String.format(
                "Dear %s, can prepare and send documents for you?", application.getClient().getFirstName()
        );

        log.info("Dossier service try a send message for {}", emailMessageDTO.getAddress());
        mailService.sendMessage(emailMessageDTO.getAddress(), messageText);
        log.info("Message for {} successfully delivered", address);
    }

    @KafkaListener(topics = "send-documents", groupId = "deal")
    public void sendDocument(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Theme theme = emailMessageDTO.getTheme();
        String address = emailMessageDTO.getAddress();

        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        Application application = dealClient.getApplicationById(emailMessageDTO.getApplicationId());
        List<File> files = documentService.createFiles(application);

        log.info("Dossier service try a send message for {}", address);
        files.forEach(file -> mailService.sendDocument(address, file));
        log.info("Message for {} successfully delivered", address);
    }

    @KafkaListener(topics = "send-ses", groupId = "deal")
    public void getSesCode(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Application application = dealClient.getApplicationById(emailMessageDTO.getApplicationId());
        Theme theme = emailMessageDTO.getTheme();

        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        String sesCode = application.getSesCode().toString() + "\n\n\nTo confirm the application, follow the link and enter the code " +
                "\n http://localhost:8085/swagger-ui/#/gateway-controller/signDocumentsUsingPOST";
        String address = emailMessageDTO.getAddress();

        mailService.sendSes(address, sesCode);

        log.info("Message for {} successfully delivered", address);

    }

    @KafkaListener(topics = "credit-issued", groupId = "deal")
    public void signDocument(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Theme theme = emailMessageDTO.getTheme();
        String address = emailMessageDTO.getAddress();
        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        String text = "Congratulations! Loan successfully approved!";

        mailService.sendMessage(address, text);
        log.info("Message for {} successfully delivered", address);
    }

    private EmailMessageDTO getEmailMessageDTO(String data) {
        EmailMessageDTO emailMessageDTO = null;
        try {
            emailMessageDTO = objectMapper.readValue(data, EmailMessageDTO.class);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
        }
        return emailMessageDTO;
    }
}
