package com.eric.appointment.entity;

import java.time.LocalTime;

import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonStringType;

import com.eric.appointment.entity.user.Provider;
import com.eric.appointment.model.DayPlan;
import com.eric.appointment.model.TimePeriod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "working_plan")
@NoArgsConstructor
public class WorkingPlan {
    
    @Id
    @Column(name = "id_provider")
    private int id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id_provider")
    private Provider provider;

    @Type(JsonStringType.class)
    @Column(name = "monday")
    private DayPlan monday;

    @Type(JsonStringType.class)
    @Column(name = "tuesday")
    private DayPlan tuesday;

    @Type(JsonStringType.class)
    @Column(name = "wednesday")
    private DayPlan wednesday;

    @Type(JsonStringType.class)
    @Column(name = "thursday")
    private DayPlan thursday;

    @Type(JsonStringType.class)
    @Column(name = "friday")
    private DayPlan friday;

    @Type(JsonStringType.class)
    @Column(name = "saturday")
    private DayPlan saturday;

    @Type(JsonStringType.class)
    @Column(name = "sunday")
    private DayPlan sunday;

    public DayPlan getDay(String day) {
        switch (day) {
            case "monday":
                return monday;
            case "tuesday":
                return tuesday;
            case "wednesday":
                return wednesday;
            case "thursday":
                return thursday;
            case "friday":
                return friday;
            case "saturday":
                return saturday;
            case "sunday":
                return sunday;
            default:
                return null;
        }
    }

    public void updateWorkingPlan(WorkingPlan updatePlan) {
        this.getMonday().setWorkingPeriod(updatePlan.getMonday().getWorkingPeriod());
        this.getTuesday().setWorkingPeriod(updatePlan.getTuesday().getWorkingPeriod());
        this.getWednesday().setWorkingPeriod(updatePlan.getWednesday().getWorkingPeriod());
        this.getThursday().setWorkingPeriod(updatePlan.getThursday().getWorkingPeriod());
        this.getFriday().setWorkingPeriod(updatePlan.getFriday().getWorkingPeriod());
        this.getSaturday().setWorkingPeriod(updatePlan.getSaturday().getWorkingPeriod());
        this.getSunday().setWorkingPeriod(updatePlan.getSunday().getWorkingPeriod());
    }

    public static WorkingPlan generateDefaultWorkingPlan() {
        WorkingPlan wp = new WorkingPlan();
        LocalTime defaultStartHour = LocalTime.parse("06:00");
        LocalTime defaultEndHour = LocalTime.parse("18:00");
        TimePeriod defaultWorkingPeriod = new TimePeriod(defaultStartHour, defaultEndHour);
        DayPlan defaultDayPlan = new DayPlan();
        defaultDayPlan.setWorkingPeriod(defaultWorkingPeriod);
        wp.setMonday(defaultDayPlan);
        wp.setTuesday(defaultDayPlan);
        wp.setWednesday(defaultDayPlan);
        wp.setThursday(defaultDayPlan);
        wp.setFriday(defaultDayPlan);
        wp.setSaturday(defaultDayPlan);
        wp.setSunday(defaultDayPlan);
        return wp;
    }
}
