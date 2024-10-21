package com.local.MySchedule.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
private static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    @RequestMapping("/index")
    public String index() {
        return "/index";
    }
    

    @RequestMapping("/home")
    public String home(HttpServletRequest request, HttpServletResponse response){
        LOGGER.info("home");
        return "/home";
    }
}
