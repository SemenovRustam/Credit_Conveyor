package com.rustam_semenov.credit_conveyor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CreditConveyorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditConveyorApplication.class, args);
    }

}
