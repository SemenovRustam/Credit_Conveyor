package com.semenov.deal.service;

import com.semenov.deal.entity.Application;
import com.semenov.deal.exceptionhandling.DealAppException;
import com.semenov.deal.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final ApplicationRepository applicationRepository;

    public Application getApplicationById(Long id) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new DealAppException("Application not found"));
        log.info("APPLICATION WITH ID {} FIND SUCCESSFULLY", id);
        return application;
    }

    public List<Application> getAllApplication() {
        List<Application> listApplications = applicationRepository.findAll();
        log.info("CREATE LIST OF APPLICATIONS");
        return listApplications;
    }
}
