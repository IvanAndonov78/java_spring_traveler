package com.example.traveler.controller;
import com.example.traveler.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod; 

import javax.servlet.http.HttpSession;


@Controller
public class AboutController {

    private static LoginService loginService;
    
    // http://localhost:8000/about
    @RequestMapping(path = "/about", method = RequestMethod.GET) // WORKS :)
    public String about(HttpSession httpSession) {
                
        System.out.println("Hit endpoint ABOUT");
        // C:\intelli_ws\traveler\src\main\resources\templates\about_vw.html
        return "about_vw";
    }

}

