package com.semenov.dossier.service;

import com.semenov.dossier.entity.Application;
import com.semenov.dossier.entity.Client;
import com.semenov.dossier.entity.Credit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentService {

    public List<File> createFiles(Application application) {
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
}
