package com.example.traveler.model;

public class Calculation {
    
    private String startingCountry;
    private Double budgetPerCountry;
    private Double totalBudget;
    private String currency;

    public Calculation() {
    }

    public Calculation(String startingCountry, Double budgetPerCountry, Double totalBudget, String currency) {
        this.startingCountry = startingCountry;
        this.budgetPerCountry = budgetPerCountry;
        this.totalBudget = totalBudget;
        this.currency = currency;
    }

    public String getStartingCountry() {
        return startingCountry;
    }

    public void setStartingCountry(String startingCountry) {
        this.startingCountry = startingCountry;
    }

    public Double getBudgetPerCountry() {
        return budgetPerCountry;
    }

    public void setBudgetPerCountry(Double budgetPerCountry) {
        this.budgetPerCountry = budgetPerCountry;
    }

    public Double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
