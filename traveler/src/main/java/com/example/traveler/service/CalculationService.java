package com.example.traveler.service;

import com.example.traveler.model.Calculation;
import com.example.traveler.model.Country;
import com.example.traveler.model.ExchangeRate;
import org.springframework.stereotype.Service;
import java.lang.Math;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CalculationService {
    
    private static CountryService countryService;
    private static ExchangeRateService exchangeRateService;
    
    public static int getTravelsAround(Calculation calculation) {
        List<Country> countries = countryService.getCountries();
        int numOfCountries = countries.size();
        Double selectedBudgetPerCountry = calculation.getBudgetPerCountry();
        Double totalCost = selectedBudgetPerCountry * numOfCountries;
        Double selectedTotalBudget = calculation.getTotalBudget();
        int res = Integer.valueOf((int) Math.ceil(selectedTotalBudget / totalCost)); 
        return res;
    }
    
    public static Double exchange(Double amountForConverting, Double rate) {
        Double a = amountForConverting * rate;
        return (double)Math.round(a * 100) / 100;
    }
    
    public static List<Country> getProcessedCountries(Calculation calc) {
        String calcCurrencyCode = calc.getCurrency();
        List<Country> countries = countryService.getCountries();
        List<Country> processedCountries =
                countryService.getCountriesWithoutStartingCountry(countries, calcCurrencyCode);
        return processedCountries;
    }
    
    public static Map<String, Double> getAllBudgets(Calculation calc) {
        Map<String, Double> res = new HashMap<String, Double>();
        List<Country> processedCountries = getProcessedCountries(calc);
        for (Country processedCountry : processedCountries) {
            String currencyCode = processedCountry.getCurrencyCode();
            String processedCurrencyCode = processedCountry.getCurrencyCode();
            ExchangeRate exRateObj = exchangeRateService.getExchangeRateObjByCurrencyCode(processedCurrencyCode);
            Double amount = calc.getBudgetPerCountry();
            Double exchRate = exRateObj.getRateValue();
            Double processedBudget = exchange(amount, exchRate);
            res.put(currencyCode, processedBudget);
        }
        return res;
    }
    
    
    public static Double getLeftover(Calculation calc) {
        
        Double leftover = calc.getTotalBudget(); // init with all tatal amount value and decrease this after ..
        String basisCurrencyCode = calc.getCurrency();
        
        ExchangeRate basisExchRateObj = exchangeRateService.getExchangeRateObjByCurrencyCode(basisCurrencyCode);
        Double basisExchRate = basisExchRateObj.getRateValue();
        
        Map<String, Double> allBudgets = getAllBudgets(calc);

        for (Map.Entry<String, Double> stringDoubleEntry : allBudgets.entrySet()) {
            String currencyCode = stringDoubleEntry.getKey();
            Double countryBudgetRaw = stringDoubleEntry.getValue(); // in the respective currency
            Double countryBudget = exchange(countryBudgetRaw, basisExchRate); // converted to basis currency
            if ((leftover - countryBudget) > 0) {
                leftover -= countryBudget;
            }
        }
        
        return leftover;
    }
    
    
}
