package com.local.MySchedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Schedule {

    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private ScheduleType type;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime upDateTime;

}
