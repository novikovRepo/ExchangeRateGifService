package com.example.exchangerategifservice.services.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GifService {

    byte[] getGif(String tag);
}