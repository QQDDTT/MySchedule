package com.local.MySchedule.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.local.MySchedule.entity.Schedule;
import com.local.MySchedule.entity.ScheduleType;

@Mapper
public interface ScheduleMapper {

    public List<ScheduleType> selectScheduleTypes ();

    public ScheduleType selectScheduleTypeById (int typeId);

    public boolean createScheduleType (ScheduleType scheduleType);

    public boolean updateScheduleType (ScheduleType scheduleType);

    public boolean deleteScheduleType (int typeId);

    public List<Schedule> selectSchedulesByDate (LocalDate date);

    public List<Schedule> selectSchedulesByType (int typeId);

    public List<Schedule> selectSchedulesByTypeAndDate (int typeId, LocalDate date);

    public Schedule selectScheduleByTime (LocalDateTime startTime, LocalDateTime endTime);

    public boolean createSchedule (Schedule schedule);

    public boolean updateSchedule (LocalDateTime startTime, LocalDateTime endTime, Schedule schedule);

    public boolean deleteSchedule (LocalDateTime startTime, LocalDateTime endTime);

}
