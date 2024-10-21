package com.local.MySchedule.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.local.MySchedule.service.TypeService;

import org.springframework.ui.Model;


@Controller
public class TypeController {

    private static Logger LOGGER = LoggerFactory.getLogger(TypeController.class);

    @Autowired
    private TypeService typeService;
    
    @GetMapping("/types")
    public String getTypes(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "types";
    }
    
    @PostMapping("/type")
    public String createType(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "types";
    }


    
}
