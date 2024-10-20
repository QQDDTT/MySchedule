package com.local.MySchedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Schedule {

    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private ScheduleType type;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime upDateTime;

}
