package com.example.exchangerategifservice.services.implementations;

import com.example.exchangerategifservice.client.FeignExchangeClient;
import com.example.exchangerategifservice.model.ExchangeModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.example.exchangerategifservice")
public class ExchangeServiceImpTest {

    @Value("${openexchangerates.base}")
    private String base;
    @Autowired
    private ExchangeServiceImp exchangeService;
    @MockBean
    private FeignExchangeClient exchangeClient;

    private ExchangeModel previous;
    private ExchangeModel current;

    @Before
    public void initialize() {

        int time = 1635033599;
        this.previous = new ExchangeModel();
        this.previous.setTimestamp(time);
        this.previous.setBase("baseCode");
        Map<String, Double> previousMap = new HashMap<>();
        previousMap.put("conCode", 15.0);
        previousMap.put("decCode", 70.0);
        previousMap.put("incCode", 65.0);
        previousMap.put(this.base, 75.100);
        previousMap.put("baseCode", 70.0);
        this.previous.setRates(previousMap);

        time = 1635119999;
        this.current = new ExchangeModel();
        this.current.setTimestamp(time);
        this.current.setBase("baseCode");
        Map<String, Double> currentMap = new HashMap<>();
        currentMap.put("conCode", 15.0);
        currentMap.put("decCode", 65.0);
        currentMap.put("incCode", 70.0);
        currentMap.put(this.base, 75.100);
        currentMap.put("baseCode", 70.0);
        this.current.setRates(currentMap);

    }

    @Test
    public void whenUnchanged() {
        Mockito.when(exchangeClient.getLatest(anyString()))
                .thenReturn(this.current);
        Mockito.when(exchangeClient.getPrevious(anyString(), anyString()))
                .thenReturn(this.previous);
        int result = exchangeService.getKeyForTag("conCode");
        assertEquals(0, result);
    }

    @Test
    public void whenDecreases() {
        Mockito.when(exchangeClient.getLatest(anyString()))
                .thenReturn(this.current);
        Mockito.when(exchangeClient.getPrevious(anyString(), anyString()))
                .thenReturn(this.previous);
        int result = exchangeService.getKeyForTag("decCode");
        assertEquals(-1, result);
    }

    @Test
    public void whenIncreases() {
        Mockito.when(exchangeClient.getLatest(anyString()))
                .thenReturn(this.current);
        Mockito.when(exchangeClient.getPrevious(anyString(), anyString()))
                .thenReturn(this.previous);
        int result = exchangeService.getKeyForTag("incCode");
        assertEquals(1, result);
    }

    @Test
    public void whenInvalidCode() {
        Mockito.when(exchangeClient.getLatest(anyString()))
                .thenReturn(this.current);
        Mockito.when(exchangeClient.getPrevious(anyString(), anyString()))
                .thenReturn(this.previous);
        int result = exchangeService.getKeyForTag("invalid");
        assertEquals(-101, result);
    }

    @Test
    public void whenNullCode() {
        Mockito.when(exchangeClient.getLatest(anyString()))
                .thenReturn(this.current);
        Mockito.when(exchangeClient.getPrevious(anyString(), anyString()))
                .thenReturn(this.previous);
        int result = exchangeService.getKeyForTag(null);
        assertEquals(-101, result);
    }
}
