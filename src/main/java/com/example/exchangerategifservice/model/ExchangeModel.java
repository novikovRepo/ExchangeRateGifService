package com.example.exchangerategifservice.model;

import java.util.Map;

public class ExchangeModel {

    private Integer timestamp;
    private String base;
    private Map<String, Double> rates;

    public ExchangeModel() {
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
