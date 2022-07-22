package com.semenov.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semenov.dossier.client.DealClient;
import com.semenov.dossier.dto.EmailMessageDTO;
import com.semenov.dossier.dto.LoanOfferDTO;
import com.semenov.dossier.entity.Application;
import com.semenov.dossier.entity.Client;
import com.semenov.dossier.entity.Credit;
import com.semenov.dossier.model.AdditionalServices;
import com.semenov.dossier.model.CreditStatus;
import com.semenov.dossier.model.Employment;
import com.semenov.dossier.model.Gender;
import com.semenov.dossier.model.MaritalStatus;
import com.semenov.dossier.model.Passport;
import com.semenov.dossier.model.Status;
import com.semenov.dossier.model.Theme;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DossierServiceTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private MailService mailService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private DossierService dossierService;


    @Test
    public void finishRegistrationMessage() throws JsonProcessingException {
        String text = "Dear ivan, finish registration your application id-1, please.";
        String mail = "email";

        Client client = Client.builder()
                .id(1L)
                .firstName("ivan")
                .middleName("ivanic")
                .lastName("ivanov")
                .email("zasds@sdaf.ru")
                .application(new Application())
                .passport(new Passport())
                .birthDate(LocalDate.of(2000, 2, 20))
                .account("Sdfs")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(new Employment())
                .build();

        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .creditStatus(CreditStatus.CALCULATED)
                .additionalServices(new AdditionalServices())
                .id(1L)
                .build();

        Application application = Application.builder()
                .credit(credit)
                .id(1L)
                .client(client)
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .build();

        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .applicationId(1L)
                .address("email")
                .theme(Theme.SEND_DOCUMENTS)
                .build();

        when(objectMapper.readValue(text, EmailMessageDTO.class)).thenReturn(emailMessageDTO);
        when((dealClient.getApplicationById(emailMessageDTO.getApplicationId()))).thenReturn(application);

        dossierService.finishRegistrationMessage(text);

        verify(mailService, times(1)).sendMessage(
                argThat(email -> email.equals(mail)),
                argThat(data -> data.equals(text))
        );
    }

    @Test
    public void sendDocumentRequest() throws JsonProcessingException {
        String text = "Dear ivan, can prepare and send documents for you?";

        Client client = Client.builder()
                .id(1L)
                .firstName("ivan")
                .middleName("ivanic")
                .lastName("ivanov")
                .email("email")
                .application(new Application())
                .passport(new Passport())
                .birthDate(LocalDate.of(2000, 2, 20))
                .account("Sdfs")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(new Employment())
                .build();

        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .creditStatus(CreditStatus.CALCULATED)
                .additionalServices(new AdditionalServices())
                .id(1L)
                .build();

        Application application = Application.builder()
                .credit(credit)
                .id(1L)
                .client(client)
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .build();


        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .applicationId(1L)
                .address("email")
                .theme(Theme.SEND_DOCUMENTS)
                .build();

        when(objectMapper.readValue(text, EmailMessageDTO.class)).thenReturn(emailMessageDTO);
        when((dealClient.getApplicationById(emailMessageDTO.getApplicationId()))).thenReturn(application);


        dossierService.sendDocumentRequest(text);

        verify(mailService, times(1)).sendMessage(
                argThat(receiver -> receiver.equals(application.getClient().getEmail())),
                argThat(data -> data.equals(text))
        );
    }

    @Test
    public void sendDocument() throws IOException {
        String text = "Dear ivan, can prepare and send documents for you?";
        File expectedFile = Files.createTempFile("file", ".txt").toFile();

        Client client = Client.builder()
                .id(1L)
                .firstName("ivan")
                .middleName("ivanic")
                .lastName("ivanov")
                .email("email")
                .application(new Application())
                .passport(new Passport())
                .birthDate(LocalDate.of(2000, 2, 20))
                .account("Sdfs")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(new Employment())
                .build();

        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .creditStatus(CreditStatus.CALCULATED)
                .additionalServices(new AdditionalServices())
                .id(1L)
                .build();

        Application application = Application.builder()
                .credit(credit)
                .id(1L)
                .client(client)
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .build();


        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .applicationId(1L)
                .address("email")
                .theme(Theme.SEND_DOCUMENTS)
                .build();

        when(objectMapper.readValue(text, EmailMessageDTO.class)).thenReturn(emailMessageDTO);
        when((dealClient.getApplicationById(emailMessageDTO.getApplicationId()))).thenReturn(application);
        when(documentService.createFiles(application)).thenReturn(List.of(expectedFile));

        dossierService.sendDocument(text);

        verify(mailService, times(1)).sendDocument(
                argThat(receiver -> receiver.equals(application.getClient().getEmail())),
                argThat(file -> file.equals(expectedFile))
        );
    }

    @Test
    public void getSesCode() throws JsonProcessingException {
        String data = "data";
        String ses = "1001" + "\n\n\nTo confirm the application, follow the link and enter the code "
                + "\n http://localhost:8085/swagger-ui/#/gateway-controller/signDocumentsUsingPOST";

        Client client = Client.builder()
                .id(1L)
                .firstName("ivan")
                .middleName("ivanic")
                .lastName("ivanov")
                .email("email")
                .application(new Application())
                .passport(new Passport())
                .birthDate(LocalDate.of(2000, 2, 20))
                .account("Sdfs")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(new Employment())
                .build();

        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .creditStatus(CreditStatus.CALCULATED)
                .additionalServices(new AdditionalServices())
                .id(1L)
                .build();

        Application application = Application.builder()
                .credit(credit)
                .id(1L)
                .client(client)
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .sesCode(1001)
                .build();


        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .applicationId(1L)
                .address("email")
                .theme(Theme.SEND_DOCUMENTS)
                .build();

        when(objectMapper.readValue(data, EmailMessageDTO.class)).thenReturn(emailMessageDTO);
        when((dealClient.getApplicationById(emailMessageDTO.getApplicationId()))).thenReturn(application);
        dossierService.getSesCode(data);

        verify(mailService, times(1)).sendSes(
                argThat(receiver -> receiver.equals(client.getEmail())),
                argThat(sesCode -> sesCode.equals(ses))
        );
    }

    @Test
    public void signDocument() throws JsonProcessingException {
        String text = "Congratulations! Loan successfully approved!";
        String mail = "email";

        Client client = Client.builder()
                .id(1L)
                .firstName("ivan")
                .middleName("ivanic")
                .lastName("ivanov")
                .email("zasds@sdaf.ru")
                .application(new Application())
                .passport(new Passport())
                .birthDate(LocalDate.of(2000, 2, 20))
                .account("Sdfs")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(new Employment())
                .build();

        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .creditStatus(CreditStatus.CALCULATED)
                .additionalServices(new AdditionalServices())
                .id(1L)
                .build();

        Application application = Application.builder()
                .credit(credit)
                .id(1L)
                .client(client)
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .build();

        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .applicationId(1L)
                .address("email")
                .theme(Theme.SEND_DOCUMENTS)
                .build();

        when(objectMapper.readValue(text, EmailMessageDTO.class)).thenReturn(emailMessageDTO);
        dossierService.signDocument(text);

        verify(mailService, times(1)).sendMessage(
                argThat(email -> email.equals(mail)),
                argThat(data -> data.equals(text))
        );
    }
}