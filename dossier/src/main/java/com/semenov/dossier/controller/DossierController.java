package com.semenov.dossier.controller;

import com.semenov.dossier.service.DossierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DossierController {

    private final DossierService dossierService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestParam String receiver,  String text) {
        dossierService.sendMessage(receiver, text);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send/doc")
    public ResponseEntity<Void> sendMessageWithDoc(@RequestParam String receiver) {
        dossierService.sendDocument(receiver);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send/ses")
    public ResponseEntity<Void> sendSesCode(@RequestParam String receiver) {
        dossierService.sendSes(receiver);
        return ResponseEntity.ok().build();
    }
}
