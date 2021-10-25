package com.example.exchangerategifservice.services.interfaces;

public interface ExchangeService {

    int getKeyForTag(String charCode);

    void refresh();

}
