package com.local.MySchedule.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.local.MySchedule.common.ScheduleException;
import com.local.MySchedule.common.ScheduleSorter;
import com.local.MySchedule.dao.ScheduleMapper;
import com.local.MySchedule.dao.TypeMapper;
import com.local.MySchedule.entity.Schedule;

@Service
public class ScheduleService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private ScheduleSorter scheduleSorter;

    /**
     * 根据类型ID获取所有日程的时长总和
     * @param typeId 类型ID
     * @return 时长
     * @throws ScheduleException
     */
    public long getDurationByType (int typeId) throws ScheduleException {
        LOGGER.info("Get duration by type id :{}", typeId);
        long total = 0;
        try {
            List<Schedule> schedules = scheduleMapper.selectSchedulesByTypeId(typeId);

            for (Schedule schedule : schedules) {
                long seconds = Duration.between(schedule.getStartTime(), schedule.getEndTime()).toMinutes();
                total += seconds;
            }
            LOGGER.debug("Get duration seconds :{}", total);
            return total;
        } catch (Exception e) {
            LOGGER.error("Get duration failed : {}", e.getCause());
            return 0;
        }
    }

    /**
     * 根据类型ID和指定日期获取日程的时长总和
     * @param typeId 类型ID
     * @param date 指定的日期
     * @return 指定日期内该类型日程的时长
     */
    public long getDurationByType (int typeId, LocalDate date) {
        long total = 0;
        try {
            List<Schedule> schedules = scheduleMapper.selectSchedulesByTypeIdAndDate(typeId, date);

            for (Schedule schedule : schedules) {
                long seconds = Duration.between(schedule.getStartTime(), schedule.getEndTime()).toMinutes();
                total += seconds;
            }
            return total;
        } catch (Exception e) {
            LOGGER.error("Get duration failed !");
            return 0;
        }
    }

    /**
     * 根据类型ID和日期获取日程的时长总和
     * @param typeId 类型ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 指定日期内该类型日程的时长
     */
    public long getDurationByType (int typeId, LocalDate startDate, LocalDate endDate) throws ScheduleException {
        LOGGER.info("Get duration by type:{} start:{} end:{}", typeId, startDate, endDate);
        if (startDate.isAfter(endDate)) {
            LOGGER.error("StartDate is after than endDate !");
            throw new ScheduleException("StartDate is after than endDate !");
        }
        long total = 0;
        LocalDate date = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
        while (!date.isAfter(endDate)) {
            total += getDurationByType(typeId, date);
            date = date.plusDays(1);
        }
        return total;
    }

    /**
     * 根据指定日期获取该日期的所有日程
     * @param scheduleDate 指定的日期
     * @return 该日期的所有日程列表
     * @throws ScheduleException 如果该日期没有日程，则抛出异常
     */
    public List<Schedule> getScheduleByDate (LocalDate scheduleDate) throws ScheduleException {
        try {
            List<Schedule> schedules = scheduleMapper.selectSchedulesByDate(scheduleDate);
            LOGGER.info("Select schedules by date : {} size : {}", scheduleDate, schedules.size());
            LOGGER.debug("Schedules : {}", schedules.stream().map(Schedule::getScheduleDate).collect(Collectors.toList()));
            return scheduleSorter.sortByStartTime(schedules);
        } catch (Exception e) {
            LOGGER.error("Select schedules in date : {} failed !", scheduleDate);
            LOGGER.error("Exception : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 根据开始日期和结束日期获取日程
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 与指定日期范围对应的日程
     * @throws ScheduleException
     */
    public Map<LocalDate, List<Schedule>> getScheduleByDateRange (LocalDate startDate, LocalDate endDate) throws ScheduleException {
        try {
            Map<LocalDate, List<Schedule>> schedulesMap = new TreeMap<>();
            LocalDate date = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
            while (!date.isAfter(endDate)) {
                schedulesMap.put(date, scheduleMapper.selectSchedulesByDate(date));
                date = date.plusDays(1);
            }
            LOGGER.info("Select schedules by date range : {} - {} size : {}", startDate, endDate, schedulesMap.size());
            LOGGER.debug("Schedules : {}", schedulesMap.keySet());
            return schedulesMap;
        } catch (Exception e) {
            LOGGER.error("Select schedules in date range : {} - {} failed !", startDate, endDate);
            LOGGER.error("Exception : {}", e.getMessage());
            return new TreeMap<>();
        }
    }

    /**
     * 根据开始时间和结束时间获取日程s
     * @param scheduleDate 指定的日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 与指定时间范围对应的日程
     * @throws ScheduleException 如果获取日程失败，则抛出异常
     */
    public Schedule getScheduleByTime (LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) throws ScheduleException {
        try {
            Schedule schedule = scheduleMapper.selectScheduleByTime(scheduleDate, startTime, endTime);
            LOGGER.info("Select schedule by start : {} end : {} : {}", startTime, endTime, scheduleDate);
            LOGGER.debug("Schedule : {}", schedule);
            return schedule;
        } catch (Exception e) {
            LOGGER.error("selectScheduleByTime failed");
            throw new ScheduleException("Get schedule by start : " + startTime.toString() + " end : " + endTime.toString() + " date : " + scheduleDate.toString() + " failed !");
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
        LocalDateTime createTime = LocalDateTime.now();
        if (title.isBlank()) {
            LOGGER.error("Title can not be empty !");
            throw new ScheduleException("Title can not be empty !");
        }
        Schedule schedule = new Schedule();
        schedule.setScheduleDate(scheduleDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setTitle(title);
        schedule.setScheduleType(typeMapper.selectScheduleTypeById(typeId));
        schedule.setDescription(description);
        schedule.setCreateTime(createTime);
        schedule.setUpdateTime(createTime);

        boolean result = scheduleMapper.createSchedule(schedule);

        if (!result) {
            LOGGER.error("Create schedule failed !");
            LOGGER.error(schedule.toString());
            throw new ScheduleException("Create schedule failed !");
        }
    }

    /**
     * 更新现有的日程
     * @param scheduleDate 日程日期
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
        Schedule schedule = getScheduleByTime(scheduleDate, oldStartTime, oldEndTime);
        LocalDateTime updateTime = LocalDateTime.now();
        if (title.isBlank()) {
            throw new ScheduleException("Outline is blank !");
        }
        schedule.setScheduleDate(scheduleDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setTitle(title);
        schedule.setScheduleType(typeMapper.selectScheduleTypeById(typeId));
        schedule.setDescription(description);
        schedule.setUpdateTime(updateTime);

        boolean result = scheduleMapper.updateSchedule(scheduleDate, oldStartTime, oldEndTime, schedule);

        if (!result) {
            LOGGER.error("Update schedule failed !");
            LOGGER.error(schedule.toString());
            throw new ScheduleException("Update schedule failed !");
        }
    }

    /**
     * 删除指定的日程
     * @param scheduleDate 日程日期
     * @param startTime 要删除日程的开始时间
     * @param endTime 要删除日程的结束时间
     * @throws ScheduleException 如果删除日程失败，则抛出异常
     */
    public void deleteSchedule (LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) throws ScheduleException {
        try {
            scheduleMapper.deleteSchedule(scheduleDate, startTime, endTime);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ScheduleException("Delete schedule failed !");
        }
    }
}
