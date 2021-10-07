package com.example.exchangerategifservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExchangeRateGifServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRateGifServiceApplication.class, args);
    }

}
