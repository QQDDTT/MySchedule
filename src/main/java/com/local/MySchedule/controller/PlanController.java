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
import org.springframework.web.bind.annotation.RequestParam;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.entity.Schedule;
import com.local.MySchedule.entity.ScheduleType;
import com.local.MySchedule.service.ScheduleService;
import com.local.MySchedule.service.TypeService;

@Controller
public class PlanController {
    private static Logger LOGGER = LoggerFactory.getLogger(PlanController.class);
    
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TypeService typeService;

    /**
     * 处理 GET 请求，获取计划页面的信息。
     * @param scheduleDate 计划日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param model 用于向视图传递数据
     * @return 返回计划页面的模板名
     */
    @GetMapping("/plan")
    public String getPlan(@RequestParam(value = "scheduleDate", required = false) LocalDate scheduleDate,
                          @RequestParam(value = "startTime", required = false) LocalTime startTime,
                          @RequestParam(value = "endTime", required = false) LocalTime endTime,
                          Model model) {
        LOGGER.info("进入计划页面");
        Schedule schedule = new Schedule();
        
        if (startTime == null || endTime == null) {
            model.addAttribute("Action", "create");
        } else {
            model.addAttribute("Action", "update");
            try {
                schedule = scheduleService.getScheduleByTime(scheduleDate, startTime, endTime);
            } catch (ScheduleException e) {
                LOGGER.error("获取计划失败: {}", e);
            }
        }

        model.addAttribute("Schedule", schedule);

        try {
            List<ScheduleType> scheduleTypes = typeService.getType();
            model.addAttribute("ScheduleTypes", scheduleTypes);
        } catch (ScheduleException e) {
            LOGGER.error("获取计划类型失败！");
        }
        
        return "/plan";
    }

    /**
     * 处理 POST 请求，创建或更新计划。
     * @param action 动作类型（创建/更新/删除）
     * @param scheduleDate 计划日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param oldStartTime 原始开始时间（仅更新时使用）
     * @param oldEndTime 原始结束时间（仅更新时使用）
     * @param title 计划标题
     * @param typeId 类型ID
     * @param description 计划描述
     * @param model 用于向视图传递数据
     * @return 返回对应的页面路径
     */
    @PostMapping("/plan")
    public String postPlanCreate(@RequestParam("action") String action,
                                 @RequestParam("scheduleDate") LocalDate scheduleDate,
                                 @RequestParam("startTime") LocalTime startTime,
                                 @RequestParam("endTime") LocalTime endTime,
                                 @RequestParam(value = "oldStartTime", required = false) LocalTime oldStartTime,
                                 @RequestParam(value = "oldEndTime", required = false) LocalTime oldEndTime,
                                 @RequestParam("title") String title,
                                 @RequestParam("typeId") int typeId,
                                 @RequestParam("description") String description,
                                 Model model) {
        LOGGER.info("刷新计划页面 {}", action);
        Schedule schedule = new Schedule();
        
        switch (action) {
            case "create":
                try {
                    scheduleService.createSchedule(scheduleDate, startTime, endTime, title, typeId, description);
                    return "/home";
                } catch (ScheduleException e) {
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                    try {
                        schedule.setScheduleType(typeService.getTypeById(typeId));
                    } catch (ScheduleException se) {
                        LOGGER.error("获取计划类型失败:", se);
                    }
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    model.addAttribute("Action", "create");
                    model.addAttribute("Schedule", schedule);
                    model.addAttribute("Error", "创建计划失败！");
                    LOGGER.error("创建计划失败！", e);
                    return "/plan";
                }

            case "update":
                try {

                    scheduleService.updateSchedule(scheduleDate, oldStartTime, oldEndTime, startTime, endTime, title, typeId, description);
                    return "/home";
                } catch (Exception e) {
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setStartTime(oldStartTime);
                    schedule.setEndTime(oldEndTime);
                    try {
                        schedule.setScheduleType(typeService.getTypeById(typeId));
                    } catch (ScheduleException se) {
                        LOGGER.error("获取计划类型失败:", se);
                    }
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    model.addAttribute("Action", "update");
                    model.addAttribute("Schedule", schedule);
                    model.addAttribute("Error", "更新计划失败！");
                    LOGGER.error("更新计划失败！");
                    return "/plan";
                }

            case "delete":
                try {

                    scheduleService.deleteSchedule(scheduleDate, startTime, endTime);
                    return "/home";
                } catch (ScheduleException e) {
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                    try {
                        schedule.setScheduleType(typeService.getTypeById(typeId));
                    } catch (ScheduleException se) {
                        LOGGER.error("获取计划类型失败:", se);
                    }
                    schedule.setTitle(title);
                    schedule.setDescription(description);
                    model.addAttribute("Action", "delete");
                    model.addAttribute("Schedule", schedule);
                    model.addAttribute("Error", "删除计划失败！");
                    LOGGER.error("删除计划失败！");
                    return "/plan";
                }

            default:
                LOGGER.error("无效的动作: {}", action);
                model.addAttribute("Error", "无效的动作：" + action);
                return "/plan";
        }
    }
}
