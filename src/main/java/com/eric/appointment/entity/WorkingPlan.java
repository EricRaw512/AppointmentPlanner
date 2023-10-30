package com.eric.appointment.entity;

import java.time.LocalTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.eric.appointment.entity.user.Provider;
import com.eric.appointment.model.DayPlan;
import com.eric.appointment.model.TimePeriod;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "monday")
    private DayPlan monday;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tuesday")
    private DayPlan tuesday;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "wednesday")
    private DayPlan wednesday;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "thursday")
    private DayPlan thursday;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "friday")
    private DayPlan friday;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "saturday")
    private DayPlan saturday;

    @JdbcTypeCode(SqlTypes.JSON)
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
        TimePeriod defaultWorkingPeroid = new TimePeriod(defaultStartHour, defaultEndHour);
        DayPlan defaultDayPlan = new DayPlan();
        defaultDayPlan.setWorkingPeriod(defaultWorkingPeroid);
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
