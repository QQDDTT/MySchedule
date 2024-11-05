package com.local.MySchedule.controller;

import java.time.LocalDate;
import java.util.LinkedList;
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
import com.local.MySchedule.entity.Schedule;
import com.local.MySchedule.service.ScheduleService;

@Controller
public class ListController {
    private static Logger LOGGER = LoggerFactory.getLogger(ListController.class);
    
    @Autowired
    private ScheduleService scheduleService;
    
    /**
     * 处理 GET 请求，显示当天的计划列表。
     * @param model 用于将数据传递给视图
     * @return 返回 list 页面
     */
    @GetMapping("/list")
    public String getList(Model model) {
        LOGGER.info("get list");
        LocalDate scheduleDate = LocalDate.now();
        Map<LocalDate, List<Schedule>> schedulesGroup = new TreeMap<>();
        model.addAttribute("ScheduleDate", scheduleDate);
        try {
            List<Schedule> schedules = scheduleService.getScheduleByDate(scheduleDate);
            schedulesGroup.put(scheduleDate, schedules);
        } catch (ScheduleException e) {
            model.addAttribute("Error", "Select schedule failed !");
            LOGGER.error("Select schedule failed !");
        }
        model.addAttribute("SchedulesGroup", schedulesGroup);
        return "/list";
    }
    
    /**
     * 处理 POST 请求，根据指定的日期范围筛选计划列表。
     * @param startDateStr 开始日期
     * @param endDateStr 结束日期
     * @param model 用于将数据传递给视图
     * @return 返回 list 页面
     */
    @PostMapping("/list")
    public String postList(@RequestParam("start_date") String startDateStr, 
                            @RequestParam("end_date") String endDateStr, 
                            Model model) {
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            LOGGER.info("Post list : start - {} end - {}", startDate, endDate);

            if (startDate.isAfter(endDate)) {
                model.addAttribute("Error", "start_date is after end_date!");
            } else {
                Map<LocalDate, List<Schedule>> schedulesMap = new TreeMap<>();
                LocalDate date = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
                while (!date.isAfter(endDate)) {
                    try {
                        schedulesMap.put(date, scheduleService.getScheduleByDate(date));
                    } catch (ScheduleException e) {
                        LOGGER.warn("Error fetching schedules for date: {}. Error: {}", date, e.getMessage());
                    }
                    date = date.plusDays(1);
                }

                LOGGER.debug("SchedulesGroup size: {}", schedulesMap.size());
                model.addAttribute("StartDate", startDate);
                model.addAttribute("EndDate", endDate);
                model.addAttribute("SchedulesGroup", schedulesMap);
            }
        } catch (Exception e) {
            model.addAttribute("Error", "Post list failed!");
            model.addAttribute("StartDate", LocalDate.now());
            model.addAttribute("EndDate", LocalDate.now());
            LOGGER.error("Post list failed!", e);
        }
        
        return "/list";
    }

}
