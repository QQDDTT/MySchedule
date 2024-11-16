package com.local.MySchedule.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
            Map<Integer, ScheduleType> typesMap = scheduleTypes.stream().collect(Collectors.toMap(ScheduleType::getTypeId, a -> a));
            Map<Integer, Long> typesData = new TreeMap<>();
            for (ScheduleType type : scheduleTypes) {
                Long time = scheduleService.getDurationByType(type.getTypeId());
                typesData.put(type.getTypeId(), time);
            }
            model.addAttribute("TypesMap", typesMap);
            model.addAttribute("TypesData", typesData);
        } catch (ScheduleException e) {
            LOGGER.error("Get view failed ! {}", e.getMessage());
            model.addAttribute("Error", "Get view failed !");
        }
        return "view";
    }
    
    @PostMapping("/view")
    public String postView(@RequestParam("startDate") LocalDate startDate,
                        @RequestParam("endDate") LocalDate endDate,
                        Model model) {
        LOGGER.info("Post view start:{} end:{}", startDate, endDate);
        try {
            List<ScheduleType> scheduleTypes = typeService.getType();
            Map<Integer, ScheduleType> typesMap = scheduleTypes.stream().collect(Collectors.toMap(ScheduleType::getTypeId, a -> a));
            Map<Integer, Long> typesData = new TreeMap<>();
            for (ScheduleType type : scheduleTypes) {
                typesData.put(type.getTypeId(), scheduleService.getDurationByType(type.getTypeId(), startDate, endDate));
            }
            model.addAttribute("TypesMap", typesMap);
            model.addAttribute("TypesData", typesData);
            model.addAttribute("StartDate", startDate);
            model.addAttribute("EndDate", endDate);
        } catch (ScheduleException e) {
            LOGGER.error("Post view failed ! {}", e.getMessage());
            model.addAttribute("Error", "Post view failed !");
        }
        return "view";
    }
}
