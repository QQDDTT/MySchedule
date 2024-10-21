package com.local.MySchedule.config;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalTime;

@Component
public class DateFormatUtil {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // 格式化 LocalTime
    public String formatTime(LocalTime time) {
        return (time != null) ? time.format(timeFormatter) : LocalTime.now().format(timeFormatter);
    }

    // 格式化 LocalDate
    public String formatDate(LocalDate date) {
        return date != null ? date.format(dateFormatter) : LocalDate.now().format(dateFormatter);
    }
}