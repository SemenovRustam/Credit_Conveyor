package com.semenov.gateway.service;

import com.semenov.gateway.client.DealClient;
import com.semenov.gateway.entity.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final DealClient dealClient;

    public Application getApplicationById(Long id) {
        Application application = dealClient.getApplicationById(id);
        log.info("APPLICATION WITH ID {} FIND SUCCESSFULLY", id);
        return application;
    }

    public List<Application> getAllApplication() {
        List<Application> listApplications = dealClient.getAllApplication();
        log.info("CREATE LIST OF APPLICATIONS");
        return listApplications;
    }
}
