package com.local.MySchedule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.local.MySchedule.entity.ScheduleType;

@Mapper
public interface TypeMapper {

    public List<ScheduleType> selectScheduleTypes ();

    public ScheduleType selectScheduleTypeById (int typeId);

    public boolean createScheduleType (ScheduleType scheduleType);

    public boolean updateScheduleType (ScheduleType scheduleType);

    public boolean deleteScheduleType (int typeId);
}
