package com.example.traveler.controller;

import com.example.traveler.service.CountryService;
import com.example.traveler.service.ExchangeRateService;
import com.example.traveler.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping; // added
import org.springframework.web.bind.annotation.RequestMethod; // added
import org.springframework.web.bind.annotation.ResponseBody; // added

@Controller
public class MigrationsController {

    UserService userService;
    CountryService countryService;
    ExchangeRateService exchangeRateService;

    // http://localhost:8000/migrate

    @RequestMapping(path = "/migrate")
    @ResponseBody
    public String migrateData() {
        System.out.println("Hit endpoint MIGRATE");
        userService.migrate();
        System.out.println("------------------------");
        userService.printAllUsers();
        System.out.println("------------------------");
        countryService.migrate();
        countryService.printAllCountries();
        System.out.println("------------------------");
        exchangeRateService.migrate();
        exchangeRateService.printAllRates();
        
        String out = "<h1> Your Migrations have been successfully created! </h1>" +
        "<h3>" +
        "<a href='/' style='margin-left:16px;color:#3333cc;font-size:20px;'> " +
                "&lt;&lt; Go to Home " +
        "</a>" +
        "</h3>";
        return out;
    }

}
