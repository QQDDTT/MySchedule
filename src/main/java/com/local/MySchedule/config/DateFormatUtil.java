package com.local.MySchedule.config;

import org.springframework.stereotype.Component;

import java.time.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalTime;

@Component
public class DateFormatUtil {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final double WIDTH_NUM = 0.75;

    // 格式化 LocalTime
    public String formatTime(LocalTime time) {
        return (time != null) ? time.format(timeFormatter) : LocalTime.now().format(timeFormatter);
    }

    // 格式化 LocalDate
    public String formatDate(LocalDate date) {
        return date != null ? date.format(dateFormatter) : LocalDate.now().format(dateFormatter);
    }

    public int calculateLeftPosition(LocalTime startTime) {
        int left =  startTime.getHour() * 60 + startTime.getMinute(); // 每小时60px
        return (int) (left * WIDTH_NUM);
    }
    
    public int calculateWidth(LocalTime startTime, LocalTime endTime) {
        long durationInMinutes = Duration.between(startTime, endTime).toMinutes();
        return (int) (durationInMinutes * WIDTH_NUM); // 每分钟1px
    }
}