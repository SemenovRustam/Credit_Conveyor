package com.semenov.dossier.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
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
public class DossierServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private DossierService dossierService;

    @Test
    public void sendSes() {
        String receiver = "mail@mail.ru";

        dossierService.sendSes(receiver);

        verify(javaMailSender, times(1)).send(
                argThat((SimpleMailMessage smm) -> Objects.requireNonNull(smm.getTo())[0].equals(receiver))
        );
    }

    @Test
    public void sendSimpleEmail() {
        String receiver = "mail@mail.ru";
        String expectedText = "some text";

        dossierService.sendMessage(receiver, expectedText);

        verify(javaMailSender, times(1)).send(
                argThat((SimpleMailMessage message) -> Objects.requireNonNull(message.getTo())[0].equals(receiver) &&
                        Objects.equals(message.getText(), expectedText))
        );
    }

    @Test
    public void sendDocument() throws MessagingException {
        String receiver = "mail@mail.ru";


        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        dossierService.sendDocument(receiver);

//        verify(javaMailSender, times(1)).createMimeMessage();

        verify(javaMailSender, times(2)).send(
                argThat((MimeMessage message) -> {
                    try {
                        return Objects.equals(message.getFrom()[0].toString(), receiver);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
        );
    }
}