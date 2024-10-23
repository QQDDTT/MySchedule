package com.local.MySchedule.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.entity.ScheduleType;
import com.local.MySchedule.service.ScheduleService;
import com.local.MySchedule.service.TypeService;

@Controller
public class ViewController {

    private static Logger LOGGER = LoggerFactory.getLogger(ViewController.class);
    
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TypeService typeService;

    @GetMapping("/view")
    public String getView(Model model) {
        LOGGER.info("Get view");
        try {
            List<ScheduleType> scheduleTypes = typeService.getType();
            Map<ScheduleType, Long> group = new TreeMap<>();
            for (ScheduleType type : scheduleTypes) {
                Long time = scheduleService.getDurationByType(type.getTypeId()).getSeconds();
                group.put(type, time);
            }
            model.addAttribute("Group", group);
        } catch (ScheduleException e) {
            LOGGER.error("Get view failed ! {}", e);
            model.addAttribute("Error", "Get view failed !");
        }
        return "/list";
    }
    
    @PostMapping("/view")
    public String postView(@RequestParam("start_date") LocalDate startDate,
                        @RequestParam("end_date") LocalDate endDate,
                        Model model) {
        LOGGER.info("Post view");
        try {
            List<ScheduleType> scheduleTypes = typeService.getType();
            Map<ScheduleType, Long> group = new TreeMap<>();
            for (ScheduleType type : scheduleTypes) {
                Long time = 0l;
                LocalDate date = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
                while (!date.isAfter(endDate)) {
                    try {
                        time = Long.sum(time, scheduleService.geDurationByType(type.getTypeId(), startDate).getSeconds());
                        
                    } catch (ScheduleException e) {
                        LOGGER.warn("Error fetching schedules for date: {}. Error: {}", startDate, e.getMessage());
                    }
                    date = date.plusDays(1);
                }
                group.put(type, time);
            }
            model.addAttribute("Group", group);
            model.addAttribute("StartDate", startDate);
            model.addAttribute("EndDate", endDate);
        } catch (Exception e) {
            LOGGER.error("Post view failed ! {}", e);
            model.addAttribute("Error", "Post view failed !");
        }
        return "/view";
    }
}
