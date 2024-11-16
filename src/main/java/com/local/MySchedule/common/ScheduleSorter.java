package com.local.MySchedule.common;

import java.util.List;
import org.springframework.stereotype.Component;

import com.local.MySchedule.entity.Schedule;

@Component
public class ScheduleSorter {

    public List<Schedule> sortByStartTime (List<Schedule> schedules) throws ScheduleException {
        schedules.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));
        return schedules;
    }
}
