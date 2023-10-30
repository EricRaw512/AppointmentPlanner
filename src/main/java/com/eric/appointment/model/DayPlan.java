package com.eric.appointment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class DayPlan implements Serializable{
    
    private TimePeriod workingPeriod;
    private List<TimePeriod> breaks;

    public DayPlan() {
        this.breaks = new ArrayList<>();
    }

    public List<TimePeriod> timePeriodWithBreakExcluded() {
        List<TimePeriod> timePeriodWithBreakExcluded = new ArrayList<>();
        timePeriodWithBreakExcluded.add(getWorkingPeriod());
        List<TimePeriod> timePeriodBreaks = getBreaks();
        for (TimePeriod curBreak : timePeriodBreaks) {
            if (curBreak.getStart().isBefore(workingPeriod.getStart())) {
                curBreak.setStart(workingPeriod.getStart());
            }
            if (curBreak.getEnd().isAfter(workingPeriod.getEnd())) {
                curBreak.setEnd(workingPeriod.getEnd());
            }
            for (TimePeriod curTime : timePeriodWithBreakExcluded) {
                if (curBreak.getStart().equals(curTime.getStart()) && curBreak.getEnd().isBefore(curTime.getEnd()) && curBreak.getEnd().isAfter(curTime.getStart())) {
                    curTime.setStart(curBreak.getEnd());
                }
                if (curBreak.getEnd().equals(curTime.getEnd()) && curBreak.getStart().isBefore(curTime.getEnd()) && curBreak.getStart().isAfter(curTime.getStart())) {
                    curTime.setEnd(curBreak.getStart());
                }
                if (curBreak.getStart().isAfter(curTime.getStart()) && curBreak.getEnd().isBefore(curTime.getEnd())) {
                    timePeriodWithBreakExcluded.add(new TimePeriod(curTime.getStart(), curBreak.getStart()));
                    curTime.setStart(curBreak.getEnd());
                }
            }
        }

        Collections.sort(timePeriodWithBreakExcluded);
        return timePeriodWithBreakExcluded;
    }
    
    public void removeBreak(TimePeriod timePeriod) {
        breaks.remove(timePeriod);
    }

    public void addBreak(TimePeriod timePeriod) {
        breaks.add(timePeriod);
    }
}
