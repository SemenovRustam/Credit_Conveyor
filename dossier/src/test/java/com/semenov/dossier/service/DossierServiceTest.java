package com.semenov.dossier.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DossierServiceTest {

    @Mock
    private DossierService dossierService;

    @Test
    public void sendSes() {
        String expectedSes = "1923";
        SimpleMailMessage mailMessage = mock(SimpleMailMessage.class);
        when(mailMessage.getText()).thenReturn("1923");

        dossierService.sendSes(any());
        assertEquals(expectedSes, mailMessage.getText());
        verify(dossierService, times(1)).sendSes(any());
    }

    @Test
    public void sendSimpleEmail() {
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("testSender");

        SimpleMailMessage actualMessage = mock(SimpleMailMessage.class);
        when(actualMessage.getFrom()).thenReturn("testSender");

        dossierService.sendMessage(any(), any());
        assertEquals(expectedMessage.getFrom(), actualMessage.getFrom());
        verify(dossierService, times(1)).sendMessage(any(), any());
    }

    @Test
    public void sendDocument() throws IOException, MessagingException {
        String expectedString = "some text";
        MimeMessage mailMessage = mock(MimeMessage.class);
        when(mailMessage.getContent()).thenReturn(expectedString);

        dossierService.sendDocument(any());

        assertEquals(expectedString, mailMessage.getContent());
        verify(dossierService, times(1)).sendDocument(any());
    }
}