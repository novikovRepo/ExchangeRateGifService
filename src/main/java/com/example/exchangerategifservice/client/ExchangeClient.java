package com.example.exchangerategifservice.client;

import com.example.exchangerategifservice.model.ExchangeModel;

public interface ExchangeClient {
    ExchangeModel getLatest(String appId);

    ExchangeModel getPrevious(String date, String appId);
}
