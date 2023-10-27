package com.eric.appointment.model;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimePeriod implements Comparable<TimePeriod>{
    
    LocalTime start;
    LocalTime end;
    
    @Override
    public int compareTo(TimePeriod o) {
        return this.start.compareTo(o.getStart());
    }
}
