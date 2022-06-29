package com.semenov.deal.controller;

import com.semenov.deal.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MsgController {

    private final MessageService messageService;

    @PostMapping("/deal/document/{applicationId}/send")
    public ResponseEntity<Void> sendDocumentsRequest(@PathVariable Long applicationId) {
        messageService.send(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deal/document/{applicationId}/sign")
    public ResponseEntity<Void> signDocumentsRequest(@PathVariable Long applicationId) {
        messageService.signDocumentRequest(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deal/document/{applicationId}/code")
    public ResponseEntity<Void> signDocuments(@PathVariable Long applicationId) {
        messageService.signDocument(applicationId);
        return ResponseEntity.ok().build();
    }
}
