package com.example.exchangerategifservice;

import org.springframework.context.annotation.Bean;
import java.text.SimpleDateFormat;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean("date")
    public SimpleDateFormat simpleDateFormatForDate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Bean("time")
    public SimpleDateFormat simpleDateFormatForTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH");
    }
}
