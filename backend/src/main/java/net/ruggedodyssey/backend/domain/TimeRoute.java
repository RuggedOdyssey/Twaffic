package net.ruggedodyssey.backend.domain;

import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

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
     * The user that uses this TimeRoute
     */
    String userId;

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

}
