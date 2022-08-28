package com.example.traveler.controller;
import com.example.traveler.model.CsrfToken;
import com.example.traveler.model.User;
import com.example.traveler.service.CsrfTokenService;
import com.example.traveler.service.LoginService;
import com.example.traveler.service.CsrfTokenService;

import com.example.traveler.service.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
public class LoginController {
    
    private static UserService userService;
    private static LoginService loginService;
    private static CsrfTokenService csrfTokenService;
    
    // http://localhost:8000/login
    @RequestMapping(path = "/displ_login_form", method = RequestMethod.GET) 
    public String displayLoginForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        //csrfTokenService.testPrintCsrfToken();
        String csrfToken = csrfTokenService.getCsrfTokenData().getCsrfToken();
        model.addAttribute("csrfToken", csrfToken);
        // C:\intelli_ws\traveler\src\main\resources\templates\login_form.html
        return "login_form";
    }

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }
    
    // http://localhost:8000/login_ajax
    @RequestMapping(path = "/login_ajax", method = RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> jsonResp(@RequestBody Map<String, Object> body, HttpSession httpSession) {
    //public Map<String, Object> jsonResp(@RequestBody User user, CsrfToken csrfToken, HttpSession httpSession)  {
        //String inputtedEmail = user.getEmail();
        //String inputtedPassword = user.getPassword();
        //String inputtedCsrfToken = csrfToken.getCsrfToken();

        String inputtedEmail = body.get("email").toString();
        String inputtedPassword = body.get("password").toString();
        String inputtedCsrfToken = body.get("csrfToken").toString();
        
        String savedCsrfToken = csrfTokenService.getCsrfTokenData().getCsrfToken();
        User savedInDbUser = userService.getUserOnLogin(inputtedEmail, inputtedPassword);

        
        
        if (savedCsrfToken.equals(inputtedCsrfToken)) {
            if (savedInDbUser != null && userService.hasRole(savedInDbUser, 1)) {
                
                int roleId = savedInDbUser.getRoleId();
                String accessToken = savedInDbUser.getAccessToken();

                Map<String, Object> tempState = new HashMap<>();
                tempState.put("roleId", roleId);
                tempState.put("accessToken", accessToken);
                return tempState;

            }
        }
        
        httpSession.invalidate();
        Map<String, Object> tempState = new HashMap<>();
        tempState.put("error", "Something Went Wrong");
        return tempState;

    }

    // http://localhost:8000/logout
    @RequestMapping(path = "/logout", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Map<String, Object> logout() {
        Map<String, Object> jsonResp = new HashMap<>();
        jsonResp.put("msg", "You are logged out!");
        return jsonResp;
    }


}
