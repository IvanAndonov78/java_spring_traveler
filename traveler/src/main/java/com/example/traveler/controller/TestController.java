package com.example.traveler.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.ResponseBody; 


@Controller
public class TestController {

    // http://localhost:8000/test1
    @RequestMapping(
            value = "/test1", // value = "/test1" is allias of path = "/test1"
            //method=RequestMethod.GET,
            method = { RequestMethod.POST,RequestMethod.GET }
            //,produces = "text/xml; charset=UTF-8"

            //, method = { RequestMethod.POST,RequestMethod.GET },
            //,headers = {"name=Ivan", "id=1"},
            //produces={"application/json","application/xml"},
            //consumes="text/html"
    )
    @ResponseBody
    public String methodTest1() {
        System.out.println("Hit endpoint TEST1");
        return "<h1> YEP </h1>";
    }

}