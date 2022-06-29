package com.semenov.deal.controller;

import com.semenov.deal.entity.Application;
import com.semenov.deal.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/deal/admin/application/{applicationId}")
    @ApiOperation(value = "Получение заявки по ID", notes = "Получить заявку")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long applicationId) {
        return  ResponseEntity.ok(adminService.getApplicationById(applicationId));
    }

    @GetMapping("/deal/admin/application")
    @ApiOperation(value = "Получение списка всех заявок", notes = "Получить все заявки")
    public ResponseEntity<List<Application>> getAllApplication() {
        return  ResponseEntity.ok(adminService.getAllApplication());
    }
}
