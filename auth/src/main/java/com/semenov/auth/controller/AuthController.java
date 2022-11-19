package com.semenov.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/hello")
    public ResponseEntity<String> helloSecurityWorld() {
        String hello = "Hello Security world!";
        return ResponseEntity.ok(hello);
    }

    @GetMapping("/world")
    public ResponseEntity<String> helloWorld() {
        String hello = "Hello world!";
        return ResponseEntity.ok().body(hello);
    }
}
