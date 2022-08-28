package com.example.traveler.model;

public class Country {
    
    private int id;
    private String countryCode;
    private String currencyCode;
    private String countryName;
    
    public Country() {
    }

    public Country(int id, String countryCode, String currencyCode, String countryName) {
        this.id = id;
        this.countryCode = countryCode;
        this.currencyCode = currencyCode;
        this.countryName = countryName;
    }

    public int getId() {
        return this.id;
    }

    public void setCountryId(int countryId) {
        this.id = countryId;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
