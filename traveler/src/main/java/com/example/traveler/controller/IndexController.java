package com.example.traveler.controller;
import com.example.traveler.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    
    private static LoginService loginService;
    
    // http://localhost:8000
    @RequestMapping(path = "/")
    public String displayIndexPage() {
                
        System.out.println("End Point hit INDEX");
        // C:\intelli_ws\traveler\src\main\resources\templates\index_vw.html
        return "index_vw";
    }

}
