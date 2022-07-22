package com.semenov.dossier;

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
import com.semenov.dossier.service.DocumentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

    @Spy
    private DocumentService documentService;

    @Test
    public void createFiles() throws IOException {
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

        File file  = Files.createTempFile("1", ".txt").toFile();
        File file1 = Files.createTempFile("2", ".txt").toFile();
        File file2 = Files.createTempFile("3", ".txt").toFile();

        List<File> expectedListFiles = List.of(file1, file, file2);

        List<File> actualListFiles = documentService.createFiles(application);

        assertEquals(expectedListFiles.size(), actualListFiles.size());
    }
}