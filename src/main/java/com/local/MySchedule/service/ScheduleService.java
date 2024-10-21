package com.local.MySchedule.service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.dao.ScheduleMapper;
import com.local.MySchedule.dao.TypeMapper;
import com.local.MySchedule.entity.Schedule;
import com.local.MySchedule.entity.ScheduleType;

@Service
public class ScheduleService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private TypeMapper typeMapper;


    /**
     * 根据类型ID获取所有日程的时长总和
     * @param typeId 类型ID
     * @return 时长
     * @throws ScheduleException
     */
    public Duration getDurationByType (int typeId) throws ScheduleException {
        long total = 0;
        try {
            List<Schedule> schedules = scheduleMapper.selectSchedulesByType(typeId);

            for (Schedule schedule : schedules) {
                @SuppressWarnings("deprecation")
                long seconds = Duration.between(schedule.getStartTime(), schedule.getStartTime()).toSeconds();
                total += seconds;
            }
            return Duration.ofSeconds(total);
        } catch (Exception e) {
            throw new ScheduleException("get duration failed !");
        }
    }

    /**
     * 根据类型ID和指定日期获取日程的时长总和
     * @param typeId 类型ID
     * @param date 指定的日期
     * @return 指定日期内该类型日程的时长
     * @throws ScheduleException 获取时长失败时抛出异常
     */
    public Duration geDurationByType (int typeId, LocalDate date) throws ScheduleException {
        long total = 0;
        try {
            List<Schedule> schedules = scheduleMapper.selectSchedulesByTypeAndDate(typeId, date);

            for (Schedule schedule : schedules) {
                @SuppressWarnings("deprecation")
                long seconds = Duration.between(schedule.getStartTime(), schedule.getStartTime()).toSeconds();
                total += seconds;
            }
            return Duration.ofSeconds(total);
        } catch (Exception e) {
            throw new ScheduleException("get duration failed !");
        }
    }

    /**
     * 根据类型ID和指定的年份与月份获取该月内所有日程的时长总和
     * @param typeId 类型ID
     * @param year 年份
     * @param month 月份
     * @return 该月内所有日程的时长总和
     * @throws ScheduleException 获取时长失败时抛出异常
     */
    public Duration geDurationByType (int typeId, int year, int month) throws ScheduleException {
        long total = 0;

        // 使用 YearMonth 表示某年某月
        YearMonth yearMonth = YearMonth.of(year, month);

        // 获取这个月的天数
        int daysInMonth = yearMonth.lengthOfMonth();

        // 遍历这个月的每一天
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            try {
                long seconds = geDurationByType(typeId, date).toSeconds();
                total += seconds;
            } catch (ScheduleException e) {
                LOGGER.warn(e.getMessage());
            }
        }
        return Duration.ofSeconds(total);
    }

    /**
     * 根据类型ID和指定的年份获取该年内所有日程的时长总和
     * @param typeId 类型ID
     * @param year 年份
     * @return 该年内所有日程的时长总和
     * @throws ScheduleException 获取时长失败时抛出异常
     */
    public Duration geDurationByType (int typeId, int year) throws ScheduleException {
        long total = 0;
        // 遍历每个月
        for (int month = 1; month <= 12; month++) {
            try {
                long seconds = geDurationByType(typeId, year, month).toSeconds();
                total += seconds;
            } catch (ScheduleException e) {
                LOGGER.warn(e.getMessage());
            }
        }
        return Duration.ofSeconds(total);
    }

    /**
     * 根据指定日期获取该日期的所有日程
     * @param date 指定的日期
     * @return 该日期的所有日程列表
     * @throws ScheduleException 如果该日期没有日程，则抛出异常
     */
    public List<Schedule> getSchedules (LocalDate scheduleDate) throws ScheduleException {
        List<Schedule> schedules = scheduleMapper.selectSchedulesByDate(scheduleDate);
        if (schedules.size() == 0) {
            LOGGER.error("Schedules in date : " + scheduleDate.toString() + " is not exists !");
            throw new ScheduleException("Schedules in date : " + scheduleDate.toString() + " is not exists !");
        }
        return schedules;
    }

    /**
     * 根据指定的年份和月份，获取该月份的所有日程
     * @param year 年份
     * @param month 月份
     * @return 该月份的所有日程列表
     * @throws ScheduleException 如果该月份没有任何日程，则抛出异常
     */
    public List<Schedule> getSchedules (int year, int month) throws ScheduleException {

        // 使用 YearMonth 表示某年某月
        YearMonth yearMonth = YearMonth.of(year, month);

        // 获取这个月的天数
        int daysInMonth = yearMonth.lengthOfMonth();

        List<Schedule> schedules = new ArrayList<>();

        // 遍历这个月的每一天
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            try {
                List<Schedule> schedulesInDay = getSchedules(date);
                schedules.addAll(schedulesInDay);
            } catch (ScheduleException e) {
                LOGGER.warn(e.getMessage());
            }
        }
        if (schedules.size() == 0) {
            LOGGER.error("Schedules in month : " + yearMonth.toString() + " is not exists !");
            throw new ScheduleException("Schedules in month : " + yearMonth.toString() + " is not exists !");
        }
        return schedules;
    }

    /**
     * 根据指定的年份，获取该年份的所有日程
     * @param year 年份
     * @return 该年份的所有日程列表
     * @throws ScheduleException 如果该年份没有任何日程，则抛出异常
     */
    public List<Schedule> getSchedules (int year) throws ScheduleException {
        List<Schedule> schedules = new ArrayList<>();

        // 遍历每个月
        for (int month = 1; month <= 12; month++) {
            try {
                List<Schedule> schedulesInYear = getSchedules(year, month);
                schedules.addAll(schedulesInYear);
            } catch (ScheduleException e) {
                LOGGER.warn(e.getMessage());
            }
        }
        if (schedules.size() == 0) {
            LOGGER.error("Schedules in year : " + year + " is not exists !");
            throw new ScheduleException("Schedules in year : " + year + " is not exists !");
        }
        return schedules;
    }

    /**
     * 根据开始时间和结束时间获取日程
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 与指定时间范围对应的日程
     * @throws ScheduleException 如果获取日程失败，则抛出异常
     */
    public Schedule getScheduleByTime (LocalTime startTime, LocalTime endTime) throws ScheduleException {
        try {
            return scheduleMapper.selectScheduleByTime(startTime, endTime);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ScheduleException("Get schedule in start : " + startTime.toString() + " end : " + endTime.toString() + " failed !");
        }
    }

    /**
     * 创建新的日程
     * @param date 日程日期
     * @param startTime 日程的开始时间
     * @param endTime 日程的结束时间
     * @param outline 日程概要
     * @param typeId 日程类型ID
     * @param scheduleDescription 日程描述
     * @throws ScheduleException 如果创建日程失败，则抛出异常
     */
    public void createSchedule (LocalDate scheduleDate, LocalTime startTime, LocalTime endTime, String title, int typeId, String description) throws ScheduleException {
        ScheduleType type = getTypeById(typeId);
        LocalDateTime createTime = LocalDateTime.now();
        if (title.isBlank()) {
            LOGGER.error("Outline is blank !");
            throw new ScheduleException("Outline is blank !");
        }
        Schedule schedule = new Schedule();
        schedule.setScheduleDate(scheduleDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setTitle(title);
        schedule.setType(type);
        schedule.setDescription(description);
        schedule.setCreateTime(createTime);
        schedule.setUpDateTime(createTime);

        boolean result = scheduleMapper.createSchedule(schedule);

        if (!result) {
            LOGGER.error("Create schedule failed !");
            LOGGER.error(schedule.toString());
            throw new ScheduleException("Create schedule failed !");
        }
    }

    /**
     * 更新现有的日程
     * @param date 日程日期
     * @param oldStartTime 旧的开始时间
     * @param oldEndTime 旧的结束时间
     * @param startTime 新的开始时间
     * @param endTime 新的结束时间
     * @param outline 日程概要
     * @param typeId 日程类型ID
     * @param scheduleDescription 日程描述
     * @throws ScheduleException 如果更新日程失败，则抛出异常
     */
    public void updateSchedule (LocalDate scheduleDate, LocalTime oldStartTime, LocalTime oldEndTime, LocalTime startTime, LocalTime endTime, String title, int typeId, String description) throws ScheduleException {
        Schedule schedule = getScheduleByTime(oldStartTime, oldEndTime);
        ScheduleType type = getTypeById(typeId);
        LocalDateTime updateTime = LocalDateTime.now();
        if (title.isBlank()) {
            throw new ScheduleException("Outline is blank !");
        }
        schedule.setScheduleDate(scheduleDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setTitle(title);
        schedule.setType(type);
        schedule.setDescription(description);
        schedule.setUpDateTime(updateTime);

        boolean result = scheduleMapper.updateSchedule(oldStartTime, oldEndTime, schedule);

        if (!result) {
            LOGGER.error("Update schedule failed !");
            LOGGER.error(schedule.toString());
            throw new ScheduleException("Update schedule failed !");
        }
    }

    /**
     * 删除指定的日程
     * @param startTime 要删除日程的开始时间
     * @param endTime 要删除日程的结束时间
     * @throws ScheduleException 如果删除日程失败，则抛出异常
     */
    public void deleteSchedule (LocalTime startTime, LocalTime endTime) throws ScheduleException {
        try {
            scheduleMapper.deleteSchedule(startTime, endTime);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ScheduleException("Delete schedule failed !");
        }
    }

    /**
     * 根据类型ID获取日程类型
     * @param typeId 类型ID
     * @return 日程类型
     * @throws ScheduleException
     */
    private ScheduleType getTypeById (int TypeId) throws ScheduleException{
        try {
            return typeMapper.selectScheduleTypeById(TypeId);
        } catch (Exception e) {
            LOGGER.error("Get type id : \" + TypeId + \" failed !");
            throw new ScheduleException("Get type id : " + TypeId + " failed !");
        }
    }
}
