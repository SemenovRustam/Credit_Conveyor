package com.semenov.deal.service;

import com.semenov.deal.entity.Application;
import com.semenov.deal.repository.ApplicationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private MessageService messageService;

    @Mock
    private  KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private  ApplicationRepository applicationRepository;


    @Test
    public void finishRegistration() {
        long expectedId = 91L;
        Application application = mock(Application.class);


        messageService.finishRegistration(expectedId);
        kafkaTemplate.send("topic", "data");

        verify(messageService, times(1)).finishRegistration(91L);
        verify(kafkaTemplate, times(1)).send("topic", "data");
    }

    @Test
    public void send() {
        long expectedId = 91L;

        Application application = mock(Application.class);
        when(applicationRepository.save(application)).thenReturn(application);
        applicationRepository.save(application);

        messageService.send(expectedId);
        kafkaTemplate.send("topic", "data");

        verify(messageService, times(1)).send(91L);
        verify(kafkaTemplate, times(1)).send(any(), any());
    }

    @Test
    public void signDocumentRequest() {
        long expectedId = 91L;

        kafkaTemplate.send("topic", "data");
        messageService.signDocumentRequest(expectedId);

        verify(messageService, times(1)).signDocumentRequest(91L);
        verify(kafkaTemplate, times(1)).send(any(), any());
    }

    @Test
    public void signDocument() {
        long expectedId = 91L;

        kafkaTemplate.send("topic", "data");
        messageService.signDocument(expectedId);

        verify(kafkaTemplate, times(1)).send(any(), any());
        verify(messageService, times(1)).signDocument(91L);
    }
}