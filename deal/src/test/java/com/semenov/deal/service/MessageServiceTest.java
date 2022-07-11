package com.semenov.deal.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private MessageService messageService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;


    @Test
    public void finishRegistration() {
        messageService.finishRegistration(anyLong());

        verify(messageService, times(1)).finishRegistration(anyLong());
    }

    @Test
    public void send() {
        messageService.sendDocumentsRequest(anyLong());

        verify(messageService, times(1)).sendDocumentsRequest(anyLong());
    }

    @Test
    public void signDocumentRequest() {
        messageService.signDocumentRequest(anyLong());

        verify(messageService, times(1)).signDocumentRequest(anyLong());
    }

    @Test
    public void signDocument() {
        messageService.signDocument(anyLong(), anyInt());

        verify(messageService, times(1)).signDocument(anyLong(), anyInt());
    }
}