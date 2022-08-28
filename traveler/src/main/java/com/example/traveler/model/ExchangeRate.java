package com.example.traveler.model;

public class ExchangeRate {
    
    private int rateId;
    private String currencyCodeNumerator;
    private String currencyCodeDenumerator;
    private String rateDate; // TODO: Consider replacing with java.util.Date type 
    private Double rateValue;

    public ExchangeRate() {
    }

    public ExchangeRate(String currencyCodeNumerator, String currencyCodeDenumerator,
                        String rateDate, Double rateValue) {
        
        this.currencyCodeNumerator = currencyCodeNumerator;
        this.currencyCodeDenumerator = currencyCodeDenumerator;
        this.rateDate = rateDate;
        this.rateValue = rateValue;
    }

    public ExchangeRate(int rateId, String currencyCodeNumerator, String currencyCodeDenumerator, 
                        String rateDate, Double rateValue) {
        
        this.rateId = rateId;
        this.currencyCodeNumerator = currencyCodeNumerator;
        this.currencyCodeDenumerator = currencyCodeDenumerator;
        this.rateDate = rateDate;
        this.rateValue = rateValue;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public String getCurrencyCodeNumerator() {
        return currencyCodeNumerator;
    }

    public void setCurrencyCodeNumerator(String currencyCodeNumerator) {
        this.currencyCodeNumerator = currencyCodeNumerator;
    }

    public String getCurrencyCodeDenumerator() {
        return currencyCodeDenumerator;
    }

    public void setCurrencyCodeDenumerator(String currencyCodeDenumerator) {
        this.currencyCodeDenumerator = currencyCodeDenumerator;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public Double getRateValue() {
        return rateValue;
    }

    public void setRateValue(Double rateValue) {
        this.rateValue = rateValue;
    }
    
}
