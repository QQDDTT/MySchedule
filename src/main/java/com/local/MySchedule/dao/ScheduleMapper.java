package com.local.MySchedule.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.local.MySchedule.entity.Schedule;

@Mapper
public interface ScheduleMapper {

    public List<Schedule> selectSchedulesByDate (LocalDate date);

    public List<Schedule> selectSchedulesByType (int typeId);

    public List<Schedule> selectSchedulesByTypeAndDate (int typeId, LocalDate date);

    public Schedule selectScheduleByTime (LocalTime startTime, LocalTime endTime);

    public boolean createSchedule (Schedule schedule);

    public boolean updateSchedule (LocalTime startTime, LocalTime endTime, Schedule schedule);

    public boolean deleteSchedule (LocalTime startTime, LocalTime endTime);

}
