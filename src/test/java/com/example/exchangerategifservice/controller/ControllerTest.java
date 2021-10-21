package com.example.exchangerategifservice.controller;

import com.example.exchangerategifservice.services.implementations.ExchangeServiceImp;
import com.example.exchangerategifservice.services.implementations.GifServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
public class ControllerTest {

    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.zero}")
    private String whatTag;
    @Value("${giphy.error}")
    private String errorTag;

    private String richUrl = "https://media3.giphy.com/media/LRxuHywFl8XosgFPFp/giphy.gif";
    private String brokeUrl = "https://media3.giphy.com/media/LRxuHywFl8XosgFPFp/giphy.gif";
    private String whatUrl = "https://media3.giphy.com/media/LRxuHywFl8XosgFPFp/giphy.gif";
    private String errorUrl = "https://media3.giphy.com/media/LRxuHywFl8XosgFPFp/giphy.gif";

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeServiceImp exchangeService;
    @MockBean
    private GifServiceImp gifService;

    @Test
    public void whenRich() throws Exception {
        ResponseEntity<byte[]> response = new RestTemplate().getForEntity(richUrl, byte[].class);
        Mockito.when(exchangeService.getKeyForTag(anyString())).thenReturn(1);
        Mockito.when(gifService.getGif(this.richTag)).thenReturn(response.getBody());
        mockMvc.perform(get("/api/getgif/code")
                .contentType(MediaType.IMAGE_GIF));
    }

    @Test
    public void whenBroke() throws Exception {
        ResponseEntity<byte[]> response = new RestTemplate().getForEntity(brokeUrl, byte[].class);
        Mockito.when(exchangeService.getKeyForTag(anyString())).thenReturn(-1);
        Mockito.when(gifService.getGif(this.brokeTag)).thenReturn(response.getBody());
        mockMvc.perform(get("/api/getgif/code")
                .contentType(MediaType.IMAGE_GIF));
    }

    @Test
    public void whenZero() throws Exception {
        ResponseEntity<byte[]> response = new RestTemplate().getForEntity(whatUrl, byte[].class);
        Mockito.when(exchangeService.getKeyForTag(anyString())).thenReturn(0);
        Mockito.when(gifService.getGif(this.whatTag)).thenReturn(response.getBody());
        mockMvc.perform(get("/api/getgif/code")
                .contentType(MediaType.IMAGE_GIF));
    }

    @Test
    public void whenError() throws Exception {
        ResponseEntity<byte[]> response = new RestTemplate().getForEntity(errorUrl, byte[].class);
        Mockito.when(exchangeService.getKeyForTag(anyString())).thenReturn(-101);
        Mockito.when(gifService.getGif(this.errorTag)).thenReturn(response.getBody());
        mockMvc.perform(get("/api/getgif/code")
                .contentType(MediaType.IMAGE_GIF));
    }

}
