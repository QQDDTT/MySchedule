package com.local.MySchedule.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.local.MySchedule.entity.Schedule;

@Mapper
public interface ScheduleMapper {

    public List<Schedule> selectSchedulesByDate (LocalDate scheduleDate);

    public List<Schedule> selectSchedulesByTypeId (int typeId);

    public List<Schedule> selectSchedulesByTypeIdAndDate (int typeId, LocalDate scheduleDate);

    public Schedule selectScheduleByTime (LocalDate scheduleDate, LocalTime startTime, LocalTime endTime);

    public boolean createSchedule (Schedule schedule);

    public boolean updateSchedule (LocalDate scheduleDate, LocalTime startTime, LocalTime endTime, Schedule schedule);

    public boolean deleteSchedule (LocalDate scheduleDate, LocalTime startTime, LocalTime endTime);

}
