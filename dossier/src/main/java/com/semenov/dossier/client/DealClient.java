package com.semenov.dossier.client;


import com.semenov.dossier.entity.Application;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "deal-client", url = "http://deal:8090/api")
public interface DealClient {

    @GetMapping("/deal/admin/application/{applicationId}")
    Application getApplicationById(@PathVariable Long applicationId);
}
