package com.example.exchangerategifservice.services.implementations;

import com.example.exchangerategifservice.client.GifClient;
import com.example.exchangerategifservice.services.interfaces.GifService;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GifServiceImp implements GifService {

    private final GifClient gifClient;
    @Value("${giphy.api.key}")
    private String apiKey;

    @Autowired
    public GifServiceImp(GifClient gifClient) {
        this.gifClient = gifClient;
    }

    @Override
    public byte[] getGif(String tag) {
        String gifUrl = JsonPath.read(gifClient.getRandomGif(this.apiKey, tag), "$.data.image_original_url");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(gifUrl, byte[].class);
        return response.getBody();
    }
}
