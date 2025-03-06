package com.local.MySchedule.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.entity.ScheduleType;
import com.local.MySchedule.service.TypeService;

import org.springframework.ui.Model;


@Controller
public class TypeController {

    private static Logger LOGGER = LoggerFactory.getLogger(TypeController.class);

    @Autowired
    private TypeService typeService;
    
    @GetMapping("/type")
    public String getTypes(Model model) {
        LOGGER.info("Get type");
        try {
            List<ScheduleType> scheduleTypes = typeService.getType();
            model.addAttribute("ScheduleTypes", scheduleTypes);
        } catch (ScheduleException e) {
            LOGGER.error("Get types failed ! {}", e.getMessage());
            model.addAttribute("Error", "Get types failed !");
        }
        return "type";
    }
    
    @PostMapping("/type")
    public String createType(@RequestParam("action") String action,
                            @RequestParam("typeId") int typeId,
                            @RequestParam("typeName") String typrName,
                            @RequestParam("typeDescription") String typeDescription,
                            @RequestParam("bgColor") String bgColor,
                            Model model) {
        LOGGER.info("Post types action : {}",action);
        try {
            switch (action) {
                case "create":
                    typeService.createScheduleType(typrName, typeDescription, bgColor);
                    break;
                case "update":
                    typeService.updateScheduleType(typeId, typrName, typeDescription, bgColor);
                    break;
                case "dalete":
                    typeService.deleteScheduleType(typeId);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Post types failed ! {}", e.getMessage());
            model.addAttribute("Error", "Post types [" + action + "] failed !");
        }
        try {
            List<ScheduleType> scheduleTypes = typeService.getType();
            model.addAttribute("ScheduleTypes", scheduleTypes);
        } catch (ScheduleException e) {
            LOGGER.error("Get types failed ! {}", e.getMessage());
            model.addAttribute("Error", "Get types failed !");
        }
        return "type";
    }
}
