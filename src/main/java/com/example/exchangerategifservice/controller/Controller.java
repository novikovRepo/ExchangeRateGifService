package com.example.exchangerategifservice.controller;

import com.example.exchangerategifservice.services.interfaces.ExchangeService;
import com.example.exchangerategifservice.services.interfaces.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    private final ExchangeService exchangeService;
    private final GifService gifService;
    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.zero}")
    private String whatTag;
    @Value("${giphy.error}")
    private String errorTag;


    @Autowired
    public Controller(
            ExchangeService exchangeService,
            GifService gifService
    ) {
        this.exchangeService = exchangeService;
        this.gifService = gifService;
    }

    @GetMapping(value = "/getgif/{code}", produces = "image/gif")
    public ResponseEntity<byte[]> getGif(@PathVariable String code) {
        int gifKey = -101;
        String gifTag = this.errorTag;
        if (code != null) {
            gifKey = exchangeService.getKeyForTag(code);
        }
        switch (gifKey) {
            case 1:
                gifTag = this.richTag;
                break;
            case -1:
                gifTag = this.brokeTag;
                break;
            case 0:
                gifTag = this.whatTag;
                break;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<>(gifService.getGif(gifTag), headers, HttpStatus.OK);
    }
}
