package net.ruggedodyssey.backend.domain;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import net.ruggedodyssey.backend.form.TimeRouteConfigForm;

import java.util.Date;

/**
 * The Objectify object model for user route configuration
 * Created by maia on 2014/08/29.
 */
@Entity
public class TimeRoute {

    /**
     * Implicit key
     */
    @Id
    Long id;

    /**
     * Holds Profile key as the parent.
     */
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> profileKey;

    /**
     * The userId of the owner of this trigger.
     */
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String userId;

    /**
     * The name the user gives to this TimeRoute
     */
    @Index
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

    /**
     * Just making the default constructor private.
     */
    private TimeRoute() {}
    public TimeRoute(final long id, String userId, TimeRouteConfigForm configForm) {
        Preconditions.checkNotNull(configForm.getRouteName(), "The name is required");
        this.id = id;
        this.profileKey = Key.create(Profile.class, userId);
        this.userId = userId;
        setFieldsFromForm(configForm);
    }

    public void setFieldsFromForm(TimeRouteConfigForm configForm) {
        this.routeName = configForm.getRouteName();
        this.monday = configForm.getMonday();
        this.tuesday = configForm.getTuesday();
        this.wednesday = configForm.getWednesday();
        this.thursday = configForm.getThursday();
        this.friday = configForm.getFriday();
        this.saturday = configForm.getSaturday();
        this.sunday = configForm.getSunday();
        this.starTime = configForm.getStarTime();
        this.endTime = configForm.getEndTime();
        this.searchString = configForm.getSearchString();
        //TODO need to do some date and time conversion stuff

//        Date startDate = configForm.getStarTime();
//        this.starTime = startDate == null ? null : new Date(startDate.getTime());
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

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Profile> getProfileKey() {
        return profileKey;
    }

    // Get a String version of the key
    public String getWebsafeKey() {
        return Key.create(profileKey, TimeRoute.class, id).getString();
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getUserId() {
        return userId;
    }
}
