package com.semenov.dossier.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailService mailService;


    @Test
    public void sendSes() {
        String sesCode = "1234";
        String receiver = "mail@mail.ru";

        mailService.sendSes(receiver, sesCode);

        verify(javaMailSender, times(1)).send(
                argThat((SimpleMailMessage smm) -> Objects.requireNonNull(smm.getTo())[0].equals(receiver))
        );
    }

    @Test
    public void sendSimpleEmail() {
        String receiver = "mail@mail.ru";
        String expectedText = "some text";

        mailService.sendMessage(receiver, expectedText);

        verify(javaMailSender, times(1)).send(
                argThat((SimpleMailMessage message) -> Objects.requireNonNull(message.getTo())[0].equals(receiver) &&
                        Objects.equals(message.getText(), expectedText))
        );
    }


    @Test
    public void sendDocument() {
        String receiver = "mail@mail.ru";
        MimeMessage message = new MimeMessage((Session) null);
        File file = new File("some.txt");

        when(javaMailSender.createMimeMessage()).thenReturn(message);
        mailService.sendDocument(receiver, file);

        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(
                argThat((MimeMessage mm) -> {
                    try {
                        return mm.getAllRecipients()[0].toString().equals(receiver);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
        );
    }
}