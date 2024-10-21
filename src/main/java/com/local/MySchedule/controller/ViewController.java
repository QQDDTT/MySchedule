package com.local.MySchedule.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.local.MySchedule.service.ScheduleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ViewController {

    private static Logger LOGGER = LoggerFactory.getLogger(ViewController.class);
    
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/view")
    public String getView(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/list";
    }
    
    @PostMapping("/view")
    public String postView(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/view";
    }
}
