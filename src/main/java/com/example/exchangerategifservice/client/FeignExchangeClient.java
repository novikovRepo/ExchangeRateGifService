package com.example.exchangerategifservice.client;

import com.example.exchangerategifservice.model.ExchangeModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ExchangeClient", url = "${openexchangerates.url.general}")
public interface FeignExchangeClient extends ExchangeClient {
    @Override
    @GetMapping("/latest.json")
    ExchangeModel getLatest(
            @RequestParam("app_id") String appId
    );

    @Override
    @GetMapping("/historical/{date}.json")
    ExchangeModel getPrevious(
            @PathVariable String date,
            @RequestParam("app_id") String appId
    );
}
