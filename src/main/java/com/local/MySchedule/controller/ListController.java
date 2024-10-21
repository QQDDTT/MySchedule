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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.entity.Schedule;
import com.local.MySchedule.service.ScheduleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ListController {
    private static Logger LOGGER = LoggerFactory.getLogger(ListController.class);
    
    @Autowired
    private ScheduleService scheduleService;
    
    /**
     * 处理 GET 请求，获取当天的计划列表并显示。
     * @param request 客户端请求对象
     * @param response 服务端响应对象
     * @param model 用于将数据传递给视图
     * @return 返回 list 页面
     */
    @GetMapping("/list")
    public String getList(HttpServletRequest request, HttpServletResponse response, Model model) {
        LOGGER.info("get list");
        LocalDate scheduleDate = LocalDate.now();
        Map<LocalDate, List<Schedule>> schedulesGroup = new TreeMap<>();
        model.addAttribute("ScheduleDate", scheduleDate);
        try {
            List<Schedule> schedules = scheduleService.getSchedules(scheduleDate);
            schedulesGroup.put(scheduleDate, schedules);
        } catch (ScheduleException e) {
            model.addAttribute("Error", "Select schedule failed !");
            LOGGER.error("Select schedule failed !");
        }
        model.addAttribute("SchedulesGroup", schedulesGroup);
        return "/list";
    }
    
    /**
     * 处理 POST 请求，根据指定条件（年、月、日）获取计划列表。
     * @param request 客户端请求对象
     * @param response 服务端响应对象
     * @param cond 路径变量，用于指示按年、月或日筛选计划
     * @param model 用于将数据传递给视图
     * @return 返回 list 页面
     */
    @PostMapping("/list/{Cond}")
    public String postList(HttpServletRequest request, HttpServletResponse response, @PathVariable("Cond") String cond, Model model) {
        LOGGER.info("post list {}", cond);
        Map<LocalDate, List<Schedule>> schedulesGroup = new TreeMap<>();
        switch (cond) {
            case "year":
                try {
                    int year = ((LocalDate) request.getAttribute("scheduleDate")).getYear();
                    List<Schedule> schedules = scheduleService.getSchedules(year);
                    schedulesGroup = schedules.stream().collect(Collectors.groupingBy(Schedule::getScheduleDate));
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
                    schedulesGroup = schedules.stream().collect(Collectors.groupingBy(Schedule::getScheduleDate));
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
                    schedulesGroup = schedules.stream().collect(Collectors.groupingBy(Schedule::getScheduleDate));
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
}
