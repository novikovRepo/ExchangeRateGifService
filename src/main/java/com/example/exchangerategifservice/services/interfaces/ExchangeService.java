package com.example.exchangerategifservice.services.interfaces;

import java.util.List;

public interface ExchangeService {

    int getKeyForTag(String charCode);

    void refresh();

}
