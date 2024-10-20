package com.local.MySchedule.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.entity.Schedule;
import com.local.MySchedule.entity.ScheduleType;
import com.local.MySchedule.service.ScheduleService;

import org.springframework.ui.Model;


@Controller
public class ScheduleController {

    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping("/index")
    public String index() {
        return "/index";
    }
    

    @RequestMapping("/home")
    public String home(HttpServletRequest request, HttpServletResponse response){
        LOGGER.info("home");
        return "/home";
    }

    @GetMapping("/plan")
    public String getPlan(HttpServletRequest request, HttpServletResponse response, Model model) {
        LOGGER.info("get plan");
        Schedule schedule = new Schedule();
        try {
            LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");
            LocalDateTime endTime = (LocalDateTime) request.getAttribute("endTime");
            if (startTime == null || endTime == null) {
                model.addAttribute("Action", "create");
            } else {
                LocalDate date = LocalDate.of(startTime.getYear(), startTime.getMonthValue(), startTime.getDayOfMonth());
                schedule.setDate(date);
                schedule.setStartTime(startTime);
                schedule.setEndTime(endTime);
                model.addAttribute("Action", "update");
                schedule = scheduleService.getScheduleByTime(startTime, endTime);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        model.addAttribute("Schedule", schedule);

        try {
            List<ScheduleType> ScheduleTypes = scheduleService.getType();
            model.addAttribute("ScheduleTypes", ScheduleTypes);
        } catch (ScheduleException e) {
            LOGGER.error("get plan types error !");
        }
        
        return "/plan";
    }

    @PostMapping("/plan")
    public String postPlanCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
        String action = (String) request.getAttribute("action");
        if (action == null || "".equals(action)) {
            action = "create";
        }
        LOGGER.info("post plan {}", action);
        Schedule schedule = new Schedule();
        switch (action) {
            case "create" :
                try {
                    LocalDate date = (LocalDate) request.getAttribute("date");
                    LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");
                    LocalDateTime endTime = (LocalDateTime) request.getAttribute("endTime");
                    String title = (String) request.getAttribute("title");
                    int TypeId = (int) request.getAttribute("typeId");
                    String description = (String) request.getAttribute("description");
                    schedule.setDate(date);
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                    schedule.setType(scheduleService.getTypeById(TypeId));
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    scheduleService.createSchedule(date, startTime, endTime, title, TypeId, description);
                    return "/home";
                } catch (Exception e) {
                    model.addAttribute("Action", "create");
                    model.addAttribute("Schedule", schedule);
                    model.addAttribute("Error", "Create schedule failed !");
                    LOGGER.error("Create schedule failed !");
                    return "/plan";
                }

            case "update" :
                try {
                    LocalDate date = (LocalDate) request.getAttribute("date");
                    LocalDateTime oldStartTime = (LocalDateTime) request.getAttribute("oldStartTime");
                    LocalDateTime oldEndTime = (LocalDateTime) request.getAttribute("oldEndTime");
                    LocalDateTime startTime = (LocalDateTime)request.getAttribute("startTime");
                    LocalDateTime endTime = (LocalDateTime) request.getAttribute("endTime");
                    String title = (String) request.getAttribute("title");
                    int typeId = (int) request.getAttribute("typeId");
                    String description = (String) request.getAttribute("description");
                    schedule.setDate(date);
                    schedule.setStartTime(oldStartTime);
                    schedule.setEndTime(oldEndTime);
                    schedule.setType(scheduleService.getTypeById(typeId));
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    scheduleService.updateSchedule(date, oldStartTime, oldEndTime, startTime, endTime, title, typeId, description);
                    return "/home";
                } catch (Exception e) {
                    model.addAttribute("Action", "update");
                    model.addAttribute("Schedule", schedule);
                    model.addAttribute("Error", "Update schedule failed !");
                    LOGGER.error("update schedule failed !");
                    return "/plan";
                }

            case "delete" :
                try {
                    LocalDate date = (LocalDate) request.getAttribute("date");
                    LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");
                    LocalDateTime endTime = (LocalDateTime) request.getAttribute("endTime");
                    String title = (String) request.getAttribute("title");
                    int TypeId = (int) request.getAttribute("typeId");
                    String description = (String) request.getAttribute("description");
                    schedule.setDate(date);
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                    schedule.setType(scheduleService.getTypeById(TypeId));
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    scheduleService.deleteSchedule(startTime, endTime);
                    return "/home";
                } catch (ScheduleException e) {
                    model.addAttribute("Action", "update");
                    model.addAttribute("Schedule", schedule);
                    model.addAttribute("Error", "Delete schedule failed !");
                    LOGGER.error("delete schedule failed !");
                    return "/plan";
                }

            default :
                LOGGER.error("action {} error !", action);
                model.addAttribute("Error", "action : " + action + " error !");
                return "/plan";
        }
        
    }
    
    @GetMapping("/list")
    public String getList(HttpServletRequest request, HttpServletResponse response, Model model) {
        LOGGER.info("get list");
        LocalDate date = LocalDate.now();
        Map<LocalDate, List<Schedule>> schedulesGroup = new TreeMap<>();
        model.addAttribute("Date", date);
        try {
            List<Schedule> schedules = scheduleService.getSchedules(date);
            schedulesGroup.put(date, schedules);
        } catch (ScheduleException e) {
            model.addAttribute("Error", "Select schedule failed !");
            LOGGER.error("Select schedule failed !");
        }
        model.addAttribute("SchedulesGroup", schedulesGroup);
        return "/list";
    }
    
    @PostMapping("/list/{Cond}")
    public String postList(HttpServletRequest request, HttpServletResponse response, @PathVariable("Cond") String cond, Model model) {
        LOGGER.info("post list {}", cond);
        Map<LocalDate, List<Schedule>> schedulesGroup = new TreeMap<>();
        switch (cond) {
            case "year":
                try {
                    int year = ((LocalDate) request.getAttribute("Date")).getYear();
                    List<Schedule> schedules = scheduleService.getSchedules(year);
                    schedulesGroup = schedules.stream().collect(Collectors.groupingBy(Schedule::getDate));
                    model.addAttribute("SchedulesGroup", schedulesGroup);
                } catch (Exception e) {
                    model.addAttribute("Error","Post list failed !");
                    LOGGER.error("Post list failed ! [{}]", cond);
                }
                break;
            case "month":
                try {
                    int year = (int)request.getAttribute("year");
                    int month = (int)request.getAttribute("month");
                    List<Schedule> schedules = scheduleService.getSchedules(year, month);
                    schedulesGroup = schedules.stream().collect(Collectors.groupingBy(Schedule::getDate));
                    model.addAttribute("SchedulesGroup", schedulesGroup);
                } catch (Exception e) {
                    model.addAttribute("Error","Post list failed !");
                    LOGGER.error("post list failed ! [{}]", cond);
                }
                break;
            case "day":
                try {
                    int year = (int)request.getAttribute("year");
                    int month = (int)request.getAttribute("month");
                    int day = (int)request.getAttribute("day");
                    LocalDate localDate = LocalDate.of(year, month, day);
                    List<Schedule> schedules = scheduleService.getSchedules(localDate);
                    schedulesGroup = schedules.stream().collect(Collectors.groupingBy(Schedule::getDate));
                    model.addAttribute("SchedulesGroup", schedulesGroup);
                } catch (Exception e) {
                    model.addAttribute("Error","Post list failed !");
                    LOGGER.error("post list failed ! [{}]", cond);
                }
                break;
            default:
                model.addAttribute("Error","Post list failed !");
                LOGGER.error("post list failed ! [{}]", cond);
        }
        return "/list";
    }
    
    @GetMapping("/view")
    public String getView(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/list";
    }
    
    @PostMapping("/view")
    public String postView(HttpServletRequest request, HttpServletResponse response) {
        return "/view";
    }
    
    @GetMapping("/types")
    public String getTypes(@RequestParam String param) {
        return "types";
    }
    
    @RequestMapping("/create_type")
    public void createType(@RequestParam String param) {
        
    }

    @RequestMapping("/update_type")
    public void updateType(@RequestParam String param) {
        
    }
    
}
