package net.ruggedodyssey.backend.form;

import com.google.appengine.repackaged.com.google.api.client.util.DateTime;

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
    DateTime starTime;

    /**
     * End time
     */
    DateTime endTime;

    /**
     * Search string. It contains words, AND, OR and matching matching ()
     */
    String searchString;

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

    public DateTime getStarTime() {
        return starTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public String getSearchString() {
        return searchString;
    }
}
