package com.consultrix.consultrixserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.consultrix")
public class ConsultrixServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultrixServerApplication.class, args);
    }

}
