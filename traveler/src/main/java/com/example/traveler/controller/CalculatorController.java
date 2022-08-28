package com.example.traveler.controller;

import com.example.traveler.model.Calculation;
import com.example.traveler.model.Country;
import com.example.traveler.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CalculatorController {
    
    private static CountryService countryService;
    private static CalculationService calculationService;
    private static LoginService loginService;
    private static CsrfTokenService csrfTokenService;
    
//    public Integer getRoleId(HttpServletRequest request) {
//        // TODO: Find a way to reuse this method  if wish to expand this app outside this Servlet context !
//        Integer roleId = null;
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("roleId")) {
//                roleId = Integer.parseInt(cookie.getValue().toString());
//            }
//        }
//        return roleId;
//    }
//
//    public String getAccessToken(HttpServletRequest request) {
//        // TODO: Find a way to reuse this method  if wish to expand this app outside this Servlet context !
//        String accessToken = null;
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("accessToken")){
//                accessToken = cookie.getValue();
//            }
//        }
//        return accessToken;
//    }

    public String readCookieTest(@CookieValue(value = "testUsername", defaultValue = "testVal") String testUsername) {
        return "testUsername: " + testUsername;
    }

    // http://localhost:8000/calculator_form
    @RequestMapping(path = "/calculator_form", method=RequestMethod.GET)
    public String displayCalcForm(
            Model model, 
            @CookieValue(value = "roleId") int roleIdVal,
            @CookieValue(value = "accessToken") String accessTokenVal
    ) {
    //public String displayCalcForm(Model model, HttpServletRequest request) {
        //int roleId = (int)getRoleId(request);
        //String accessToken = getAccessToken(request);
        int roleId = roleIdVal;
        String accessToken = accessTokenVal;
        
        if (!loginService.isLoggedIn(accessToken) || !loginService.hasRole(roleId)) {
            return "auth_error_vw";
        }
        
        Calculation calculation = new Calculation();
        model.addAttribute("calculation", calculation);

        String csrfToken = csrfTokenService.getCsrfTokenData().getCsrfToken();
        model.addAttribute("csrfToken", csrfToken);
        
        List<String> countryOptions = countryService.getCountryOptions();
        model.addAttribute("countryOptions", countryOptions);
                
        List<String> currencyOptions = countryService.getCurrencyOptions();
        model.addAttribute("currencyOptions", currencyOptions);
                
        // C:\intelli_ws\traveler\src\main\resources\templates\secret\calc_vw.html
        return "secret/calc_vw";
    }

    // http://localhost:8000/submit_calc
    @RequestMapping(path = "/submit_calc", method=RequestMethod.POST, produces="text/html")
    public String calculate(
            Model model, 
            //HttpServletRequest request,
            HttpSession httpSession,
            @RequestParam("startingCountry") String startingCountry,
            @RequestParam("budgetPerCountry") Double budgetPerCountry,
            @RequestParam("totalBudget") Double totalBudget,
            @RequestParam("currency") String currency,
            @RequestParam("csrfToken") String csrfToken,
            @CookieValue(value = "roleId") int roleIdVal,
            @CookieValue(value = "accessToken") String accessTokenVal
    ) {

        //int roleId = (int)getRoleId(request);
        //String accessToken = getAccessToken(request);
        int roleId = roleIdVal;
        String accessToken = accessTokenVal;
        
        /* Actually, after successfull login, we do not need this:
        if (!loginService.isLoggedIn(accessToken) || !loginService.hasRole(roleId)) {
            return "auth_error_vw";
        }

        String savedCsrfToken = csrfTokenService.getCsrfTokenData().getCsrfToken();
        if (!savedCsrfToken.equals(csrfToken)) {
            return "auth_error_vw";
        }
         */
        
        Calculation calculation = new Calculation();
        model.addAttribute("calculation", calculation);

        model.addAttribute("csrfToken", csrfToken);

        List<String> countryOptions = countryService.getCountryOptions();
        model.addAttribute("countryOptions", countryOptions);

        List<String> currencyOptions = countryService.getCurrencyOptions();
        model.addAttribute("currencyOptions", currencyOptions);
        
        Calculation calc = new Calculation(startingCountry, budgetPerCountry, totalBudget, currency);
        int travelsAround = calculationService.getTravelsAround(calc);
        
        Double leftover = calculationService.getLeftover(calc);
                
        String input = "" +
        "<div class='input-res-holder'>" +
        "<h4 style='margin:2px;'> 1.) Your Input: </h4>" +
        "Starting Country   : <span class='msg-stat-input'>" + startingCountry + "</span> <br>" +
        "Budget per Country : <span class='msg-stat-input'>" + budgetPerCountry + "</span> <br>" +
        "Total Budget       : <span class='msg-stat-input'>" + totalBudget + "</span> <br>" +
        "Currency           : <span class='msg-stat-input'>" + currency + "</span> <br>" +
        "</div>";
        
        String output = "" +
        "<h4 style='margin:2px;'> 2.) Output: </h4>" +
        "Bulgaria has 5 neighbour countries (TR, GR, MK, SR, RO).<br>" +
        "Traveler can travel around them <span class='msg-stat'>" + travelsAround + "</span> times. <br>" +
        "He will have " + "<span class='msg-stat'>" + leftover + "</span> leftover." + " " + currency + "<br>";
        
        String outputLooped = "";

        Map<String, Double> countryBudgets = calculationService.getAllBudgets(calc);
        for (Map.Entry<String, Double> stringDoubleEntry : countryBudgets.entrySet()) {
            String currencyCode = stringDoubleEntry.getKey();
            Double countryBudgetRaw = stringDoubleEntry.getValue(); 
            Country countryObj = countryService.getCountryObjByCurrencyCode(currencyCode);
            String countryName = countryObj.getCountryName();
            outputLooped += "For " + countryName + "you will need to buy <span class='msg-stat'>"; 
            outputLooped += "" + countryBudgetRaw + "</span>" + " " + currencyCode + ". <br>";
        }
        
        String out = input + "<hr>" + output + outputLooped;
        
        httpSession.setAttribute("out", out);
        // If the text was longer I would not use httpSession to store it :)
        // Maybe better solution would be: HttpServletRequest request => setAttribute("out", out);
        
        // C:\intelli_ws\traveler\src\main\resources\templates\secret\calc_vw.html
        return "secret/calc_vw";
    }

    // http://localhost:8000/clear_output
    @RequestMapping(path = "/clear_output", method=RequestMethod.GET)
    public String clearOutput(
            Model model, 
            //HttpServletRequest request, 
            HttpSession httpSession,
            @CookieValue(value = "roleId") int roleIdVal,
            @CookieValue(value = "accessToken") String accessTokenVal
    ) {

        //int roleId = (int)getRoleId(request);
        //String accessToken = getAccessToken(request);
        int roleId = roleIdVal;
        String accessToken = accessTokenVal;

        if (!loginService.isLoggedIn(accessToken) || !loginService.hasRole(roleId)) {
            return "auth_error_vw";
        }
                        
        Calculation calculation = new Calculation();
        model.addAttribute("calculation", calculation);

        List<String> countryOptions = countryService.getCountryOptions();
        model.addAttribute("countryOptions", countryOptions);

        List<String> currencyOptions = countryService.getCurrencyOptions();
        model.addAttribute("currencyOptions", currencyOptions);
                
        String out = "";
        httpSession.setAttribute("out", out);
        // C:\intelli_ws\traveler\src\main\resources\templates\secret\calc_vw.html
        return "secret/calc_vw";
    }

    // http://localhost:8000/country_options_ext
    @RequestMapping(path = "/country_options_ext", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public List<Map<String,Object>> countryOptionsExt(
            // HttpServletRequest request,
            @CookieValue(value = "roleId") int roleIdVal,
            @CookieValue(value = "accessToken") String accessTokenVal
    ) {
        //int roleId = (int)getRoleId(request);
        //String accessToken = getAccessToken(request);
        int roleId = roleIdVal;
        String accessToken = accessTokenVal;

        if (!loginService.isLoggedIn(accessToken) || !loginService.hasRole(roleId)) {
            List<Map<String,Object>> errAsJson = new ArrayList<Map<String, Object>>();
            Map<String, Object> entry = new HashMap<String,Object>();
            entry.put("msg", "You are logged out!");
            errAsJson.add(entry);
            return errAsJson;
        }

        List<Map<String,Object>> extCountryOptionsAsJson = countryService.getCountryOptionsExt();
        return extCountryOptionsAsJson;
    }
    
}
