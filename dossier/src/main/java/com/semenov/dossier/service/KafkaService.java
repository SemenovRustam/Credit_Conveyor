package com.semenov.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semenov.dossier.client.DealClient;
import com.semenov.dossier.dto.EmailMessageDTO;
import com.semenov.dossier.entity.Application;
import com.semenov.dossier.entity.Client;
import com.semenov.dossier.entity.Credit;
import com.semenov.dossier.model.Theme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {

    private final DossierService dossierService;
    private final ObjectMapper objectMapper;
    private final DealClient dealClient;
    private String filename;



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

        dossierService.sendMessage(address, text);

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
        dossierService.sendMessage(emailMessageDTO.getAddress(), messageText);
        log.info("Message for {} successfully delivered", address);
    }

    @KafkaListener(topics = "send-documents", groupId = "deal")
    public void sendDocument(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Theme theme = emailMessageDTO.getTheme();
        String address = emailMessageDTO.getAddress();

        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        Application application = dealClient.getApplicationById(emailMessageDTO.getApplicationId());
        List<File> files = createFiles(application);

        log.info("Dossier service try a send message for {}", address);
        files.forEach(file -> dossierService.sendDocument(address, file));
        log.info("Message for {} successfully delivered", address);
    }

    @KafkaListener(topics = "send-ses", groupId = "deal")
    public void getSesCode(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Application application = dealClient.getApplicationById(emailMessageDTO.getApplicationId());
        Theme theme = emailMessageDTO.getTheme();

        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        String sesCode = application.getSesCode().toString();
        String address = emailMessageDTO.getAddress();

        dossierService.sendSes(address, sesCode);

        log.info("Message for {} successfully delivered", address);

    }

    @KafkaListener(topics = "credit-issued", groupId = "deal")
    public void signDocument(String data) {
        EmailMessageDTO emailMessageDTO = getEmailMessageDTO(data);
        Theme theme = emailMessageDTO.getTheme();
        String address = emailMessageDTO.getAddress();
        log.info("CONSUMER RECEIVED THE MESSAGE WITH TOPIC {}", theme);

        String text = "Congratulations! Loan successfully approved!";

        dossierService.sendMessage(address, text);
        log.info("Message for {} successfully delivered", address);
    }


    private List<File> createFiles(Application application) {
        File creditFile = null;
        File applicationFile = null;
        File clientFile = null;
        try {
            Credit credit = application.getCredit();
            Client client = application.getClient();
             creditFile = getCreditFile(credit);
             applicationFile = getApplicationFile(application);
             clientFile = getClientFile(client);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return List.of(creditFile, applicationFile, clientFile);
    }

    private File getCreditFile(Credit credit) {
        HashMap<String, String> data = new HashMap<>();
        data.put("creditId ", credit.getId().toString());
        data.put("creditAmount", credit.getAmount().toString());
        data.put("creditTerm", credit.getTerm().toString());
        data.put("monthlyPayment", credit.getMonthlyPayment().toString());
        data.put("rate ", credit.getRate().toString());
        data.put("psk ", credit.getPsk().toString());
        data.put("paymentSchedule ", credit.getPaymentSchedule().toString());
        data.put("additional services ", credit.getAdditionalServices().toString());
        data.put("credit status ", credit.getCreditStatus().toString());

        List<String> creditData = formatData(data);

        Path path = null;
        try {
            Path creditFile = Files.createTempFile("Credit", ".txt");
            path = Files.write(creditFile, creditData);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return path.toFile();
    }

    private File getApplicationFile(Application application) {
        HashMap<String, String> data = new HashMap<>();
        data.put("application Id", application.getId().toString());
        data.put("Client ", application.getClient().toString());
        data.put("Status ", application.getStatus().toString());
        data.put("creation date ", application.getCreationDate().toString());
        data.put("apply offer ", application.getAppliedOffer().toString());
        data.put("application history", application.getStatusHistory().toString());

        List<String> creditData = formatData(data);

        Path path = null;
        try {
            Path appFile = Files.createTempFile("Application", ".txt");
            path = Files.write(appFile, creditData);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return path.toFile();
    }

    private List<String> formatData(HashMap<String, String> data) {
        return data.entrySet()
                .stream()
                .map(e -> e.getKey() + " : " + e.getValue())
                .collect(Collectors.toList());
    }

    private File getClientFile(Client client) {
        HashMap<String, String> data = new HashMap<>();
        String clientFullName = String.format("%s %s %s", client.getFirstName(), client.getLastName(), client.getMiddleName());
        data.put("Client name", clientFullName);
        data.put("birth day", client.getBirthDate().toString());
        data.put("email", client.getEmail());
        data.put("gender", client.getGender().toString());
        data.put("marital status", client.getMaritalStatus().toString());
        data.put("dependent amount", client.getDependentAmount().toString());
        data.put("passport", client.getPassport().toString());
        data.put("employment", client.getEmployment().toString());
        data.put("account", client.getAccount());


        List<String> clientData = formatData(data);

        Path clientPath = null;
        try {
            Path clientFile = Files.createTempFile("Client", ".txt");
            clientPath = Files.write(clientFile, clientData);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return clientPath.toFile();
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
