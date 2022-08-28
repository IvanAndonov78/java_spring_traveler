package com.example.traveler.model;

public class CsrfToken{

    private String csrfToken;
    private int isValidCsrfToken;
    
    public CsrfToken() {
    }

    public CsrfToken(String csrfToken, int isValidCsrfToken) {
        this.csrfToken = csrfToken;
        this.isValidCsrfToken = isValidCsrfToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public int getIsValidCsrfToken() {
        return isValidCsrfToken;
    }

    public void setIsValidCsrfToken(int isValidCsrfToken) {
        this.isValidCsrfToken = isValidCsrfToken;
    }
}
