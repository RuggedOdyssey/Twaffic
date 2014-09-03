package net.ruggedodyssey.backend.form;

import java.util.Date;

/**
 * POJO form to record TimeRoute Config
 * Created by maia on 2014/08/30.
 */
public class TimeRouteConfigForm {
    /**
     * The name the user gives to this TimeRoute
     */
    String routeName;

    /**
     * Flags to indicate if this TimeRoute applies to the day of week.
     */
    Boolean monday;
    Boolean tuesday;
    Boolean wednesday;
    Boolean thursday;
    Boolean friday;
    Boolean saturday;
    Boolean sunday;

    /**
     * Start time
     */
    Date starTime;

    /**
     * End time
     */
    Date endTime;

    /**
     * Search string. It contains words, AND, OR and matching matching ()
     */
    String searchString;

    public TimeRouteConfigForm(String routeName,
                               Date startTime,
                               Date endTime,
                               boolean monday,
                               boolean tuesday,
                               boolean wednesday,
                               boolean thursday,
                               boolean friday,
                               boolean saturday,
                               boolean sunday) {
        this.routeName = routeName;
        this.starTime = startTime == null ? null : new Date(startTime.getTime());
        this.endTime = endTime == null ? null : new Date(endTime.getTime());
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public String getRouteName() {
        return routeName;
    }

    public Boolean getMonday() {
        return monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public Boolean getFriday() {
        return friday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public Date getStarTime() {
        return starTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getSearchString() {
        return searchString;
    }
}
