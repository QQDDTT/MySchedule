package com.local.MySchedule.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.entity.Schedule;
import com.local.MySchedule.entity.ScheduleType;
import com.local.MySchedule.service.ScheduleService;
import com.local.MySchedule.service.TypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PlanController {
    private static Logger LOGGER = LoggerFactory.getLogger(PlanController.class);
    
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TypeService typeService;

    /**
     * 处理 GET 请求，用于获取计划页面的信息。
     * @param request 客户端请求对象，包含请求参数
     * @param response 服务端响应对象
     * @param model 用于向视图传递数据
     * @return 返回计划页面的模板名
     */
   @GetMapping("/plan")
    public String getPlan(HttpServletRequest request, HttpServletResponse response, Model model) {
        LOGGER.info("get plan");
        Schedule schedule = new Schedule();
        try {
            LocalDate scheduleDate = (LocalDate) request.getAttribute("scheduleDate");
            LocalTime startTime = (LocalTime) request.getAttribute("startTime");
            LocalTime endTime = (LocalTime) request.getAttribute("endTime");
            if (startTime == null || endTime == null) {
                model.addAttribute("Action", "create");
            } else {
                schedule.setScheduleDate(scheduleDate);
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
            List<ScheduleType> ScheduleTypes = typeService.getType();
            model.addAttribute("ScheduleTypes", ScheduleTypes);
        } catch (ScheduleException e) {
            LOGGER.error("get plan types error !");
        }
        
        return "/plan";
    }

    /**
     * 处理 POST 请求，用于创建或更新计划。
     * @param request 客户端请求对象，包含请求参数
     * @param response 服务端响应对象
     * @param model 用于向视图传递数据
     * @return 返回对应的页面路径
     */
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
                    LocalDate scheduleDate = (LocalDate) request.getAttribute("scheduleDate");
                    LocalTime startTime = (LocalTime) request.getAttribute("startTime");
                    LocalTime endTime = (LocalTime) request.getAttribute("endTime");
                    String title = (String) request.getAttribute("title");
                    int TypeId = (int) request.getAttribute("typeId");
                    String description = (String) request.getAttribute("description");
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                    schedule.setType(typeService.getTypeById(TypeId));
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    scheduleService.createSchedule(scheduleDate, startTime, endTime, title, TypeId, description);
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
                    LocalDate scheduleDate = (LocalDate) request.getAttribute("scheduleDate");
                    LocalTime oldStartTime = (LocalTime) request.getAttribute("oldStartTime");
                    LocalTime oldEndTime = (LocalTime) request.getAttribute("oldEndTime");
                    LocalTime startTime = (LocalTime)request.getAttribute("startTime");
                    LocalTime endTime = (LocalTime) request.getAttribute("endTime");
                    String title = (String) request.getAttribute("title");
                    int typeId = (int) request.getAttribute("typeId");
                    String description = (String) request.getAttribute("description");
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setStartTime(oldStartTime);
                    schedule.setEndTime(oldEndTime);
                    schedule.setType(typeService.getTypeById(typeId));
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    scheduleService.updateSchedule(scheduleDate, oldStartTime, oldEndTime, startTime, endTime, title, typeId, description);
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
                    LocalDate scheduleDate = (LocalDate) request.getAttribute("scheduleDate");
                    LocalTime startTime = (LocalTime) request.getAttribute("startTime");
                    LocalTime endTime = (LocalTime) request.getAttribute("endTime");
                    String title = (String) request.getAttribute("title");
                    int TypeId = (int) request.getAttribute("typeId");
                    String description = (String) request.getAttribute("description");
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                    schedule.setType(typeService.getTypeById(TypeId));
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
}
