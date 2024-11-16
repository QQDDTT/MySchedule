package com.local.MySchedule.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScheduleType implements Comparable<ScheduleType> {

    private int typeId;
    private String typeName;
    private String typeDescription;
    private String bgColor;

    @Override
    public int compareTo(ScheduleType other) {
        return this.typeId - other.typeId;
    }
}
