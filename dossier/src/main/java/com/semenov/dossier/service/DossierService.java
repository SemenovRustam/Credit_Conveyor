package com.semenov.dossier.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semenov.dossier.dto.EmailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DossierService {

    private final JavaMailSender javaMailSender;
    private String filename;
    private final ObjectMapper objectMapper;


    @Value("${mail.sender}")
    private String senderEmail;

    public void sendSes(String receiver, String sesCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject("Ses code");
        message.setText(sesCode);
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

    public void sendDocument(String receiver,File file ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setFrom(new InternetAddress(senderEmail, senderEmail));

            message.setSubject("Оформление кредита");
            Multipart multipart = new MimeMultipart();
            MimeBodyPart fileBodyPart = new MimeBodyPart();

            DataSource fileDataSource = new FileDataSource(file);
            fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
            fileBodyPart.setFileName(filename + ".txt");
            multipart.addBodyPart(fileBodyPart);

            message.setContent(multipart);
            javaMailSender.send(message);
            log.info("message send");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
