package com.semenov.dossier.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DossierService {

    private final JavaMailSender javaMailSender;
    private List<String> list = new ArrayList<>();
    private int docCount;
    private int sesCodeCount;
    private List<String> sesCodeList = new ArrayList<>();


    @Value("${mail.sender}")
    private String senderEmail;

    public void sendSes(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject("Ses code");
        message.setText(sesCodeList.get(sesCodeCount - 1));
        javaMailSender.send(message);
        log.info("message send");
    }


    public void sendMessage(String receiver, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject("Оформление кредита");
        message.setText(text);
        javaMailSender.send(message);
        log.info("message send");
    }

    public void sendDocument(String receiver) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setFrom(new InternetAddress(senderEmail, "senderText"));

            message.setSubject("Оформление кредита");
            Multipart multipart = new MimeMultipart();
            MimeBodyPart fileBodyPart = new MimeBodyPart();

            DataSource fileDataSource = new FileDataSource("dossier/src/main/resources/documents/" + list.get(docCount - 1));
            fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
            fileBodyPart.setFileName(list.get(docCount - 1) + ".txt");
            multipart.addBodyPart(fileBodyPart);

            message.setContent(multipart);
            javaMailSender.send(message);
            log.info("message send");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(topics = {"finish-registration"
            , "create-documents"
            , "send-documents"
            , "credit-issued"
            , "application-denied"}
            , groupId = "deal")
    private void listener(String data) {
        String theme = null;
        if (data.contains("FINISH_REGISTRATION")) {
            theme = "FINISH_REGISTRATION";
            docCount++;
        }
        if (data.contains("CREATE_DOCUMENTS")) {
            theme = "CREATE_DOCUMENTS";
            docCount++;
        }
        if (data.contains("SEND_DOCUMENTS")) {
            theme = "SEND_DOCUMENTS";
            docCount++;
        }
        if (data.contains("SEND_SES")) {
            theme = "SEND_SES";
            docCount++;
        }
        if (data.contains("CREDIT_ISSUED")) {
            theme = "CREDIT_ISSUED";
            docCount++;
        }
        if (data.contains("APPLICATION_DENIED")) {
            theme = "APPLICATION_DENIED";
            docCount++;
        }
        System.out.println(theme);
        list.add(theme);
    }


    @KafkaListener(topics = "send-ses", groupId = "deal")
    private void getSesCode(String data) {
        System.out.println("Ses code = " + data);
        sesCodeList.add(data);
        sesCodeCount++;
    }
}
