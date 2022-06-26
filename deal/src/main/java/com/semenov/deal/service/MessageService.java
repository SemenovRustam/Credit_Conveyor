package com.semenov.deal.service;

import com.semenov.deal.dto.EmailMessageDTO;
import com.semenov.deal.entity.Application;
import com.semenov.deal.entity.Credit;
import com.semenov.deal.exceptionhandling.DealAppException;
import com.semenov.deal.model.ApplicationHistory;
import com.semenov.deal.model.CreditStatus;
import com.semenov.deal.model.Status;
import com.semenov.deal.model.Theme;
import com.semenov.deal.repository.ApplicationRepository;
import com.semenov.deal.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;

    public void finishRegistration(Long applicationId) {
        Application application = getApplication(applicationId);
        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(application.getClient().getEmail())
                .applicationId(applicationId)
                .theme(Theme.FINISH_REGISTRATION)
                .build());
        log.debug("FINISH REGISTRATION MESSAGE SEND FOR CLIENT");
    }

    public void send(Long applicationId) {
        log.debug("TRY GET APPLICATION BY ID {} ", applicationId);
        Application application = getApplication(applicationId);
        log.debug("UPDATE APPLICATION STATUS");
        application.setStatus(Status.PREPARE_DOCUMENTS);
        updateStatusHistory(application, Status.PREPARE_DOCUMENTS);
        log.debug("UPDATE APPLICATION IN DB");
        applicationRepository.save(application);

        log.debug("SEND MESSAGE FOR DOSSIER");
        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(application.getClient().getEmail())
                .applicationId(applicationId)
                .theme(Theme.CREATE_DOCUMENTS).build()
        );
    }

    public void signDocumentRequest(Long applicationId) {
        log.info("TRY GET APPLICATION BY ID {} ", applicationId);
        Application application = getApplication(applicationId);
        Integer sesCode = createSesCode();
        kafkaTemplate.send(getTopic(Theme.SEND_SES), sesCode.toString());

        log.debug("SET SESCODE {} FOR APPLICATION {} ", sesCode, application);
        application.setSesCode(sesCode);
        application.setStatus(Status.DOCUMENT_SIGNED);
        log.debug("UPDATE APPLICATION IN DB");
        applicationRepository.save(application);

        log.debug("SEND MESSAGE FOR DOSSIER");
        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(application.getClient().getEmail())
                .applicationId(applicationId)
                .theme(Theme.SEND_DOCUMENTS)
                .build());
    }

    public void signDocument(Long applicationId) {
        log.debug("TRY GET APPLICATION BY ID {} ", applicationId);
        Application application = getApplication(applicationId);
        application.setStatus(Status.DOCUMENT_SIGNED);
        application.setSingDate(LocalDate.now());
        updateStatusHistory(application, Status.DOCUMENT_SIGNED);
        log.debug("UPDATE APPLICATION IN DB");
        applicationRepository.save(application);

        issueCredit(application);

        sendMessageForConsumer(EmailMessageDTO.builder()
                .address(application.getClient().getEmail())
                .applicationId(applicationId)
                .theme(Theme.CREDIT_ISSUED)
                .build()
        );
    }

    private void issueCredit(Application application) {
        if (application.getCredit() == null) {
            log.info("\"credit  is not exists\"");
            throw new DealAppException("credit  is not exists");
        }
        Long creditId = application.getCredit().getId();
        Credit credit = creditRepository.findById(creditId).get();

        application.setStatus(Status.CREDIT_ISSUED);
        updateStatusHistory(application, Status.CREDIT_ISSUED);
        credit.setCreditStatus(CreditStatus.ISSUED);
        creditRepository.save(credit);
        log.info("UPDATE CREDIT {} IN DATABASE");
    }

    private void updateStatusHistory(Application application, Status status) {
        application.getStatusHistory().add(ApplicationHistory.builder()
                .date(LocalDate.now())
                .status(status)
                .build()
        );
    }

    private Integer createSesCode() {
        return ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private Application getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(() -> new DealAppException("application by id " + applicationId + " not found"));
    }

    private void sendMessageForConsumer(EmailMessageDTO messageDTO) {
        String topic = getTopic(messageDTO.getTheme());
        kafkaTemplate.send(topic, messageDTO.toString());
    }

    private String getTopic(Theme theme) {
        log.info("START DEFINITION TOPIC");
        String topic;

        switch (theme) {
            case FINISH_REGISTRATION:
                topic = "finish-registration";
                break;
            case CREATE_DOCUMENTS:
                topic = "create-documents";
                break;
            case SEND_DOCUMENTS:
                topic = "send-documents";
                break;
            case SEND_SES:
                topic = "send-ses";
                break;
            case CREDIT_ISSUED:
                topic = "credit-issued";
                break;
            case APPLICATION_DENIED:
                topic = "application-denied";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + theme);
        }
        log.info("TOPIC IS DEFINITION {}", topic);
        return topic;
    }
}
