package net.ruggedodyssey.backend.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import net.ruggedodyssey.backend.domain.Profile;
import net.ruggedodyssey.backend.domain.TimeRoute;
import net.ruggedodyssey.backend.form.ProfileForm;
import net.ruggedodyssey.backend.form.TimeRouteConfigForm;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static net.ruggedodyssey.backend.service.OfyService.factory;
import static net.ruggedodyssey.backend.service.OfyService.ofy;



/**
 * An endpoint to manage triggers for a user
 */
@Api(name = "trigger", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.ruggedodyssey.net", ownerName = "backend.ruggedodyssey.net", packagePath=""))
public class TriggerConfigEndpoint {

    private static final Logger log = Logger.getLogger(TriggerConfigEndpoint.class.getName());

    /*
     * Get the display name from the user's email. For example, if the email is
     * lemoncake@example.com, then the display name becomes "lemoncake."
     */
    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }
    /**
     * Creates or updates a Profile object associated with the given user
     * object.
     *
     * @param user        A User object injected by the cloud endpoints.
     * @param profileForm A ProfileForm object sent from the client form.
     * @return Profile object just created.
     * @throws UnauthorizedException when the User object is null.
     */

    // Declare this method as a method available externally through Endpoints
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = ApiMethod.HttpMethod.POST)
    // The request that invokes this method should provide data that
    // conforms to the fields defined in ProfileForm
    public Profile saveProfile(final User user, ProfileForm profileForm)
            throws UnauthorizedException {
        // If the user is not logged in, throw an UnauthorizedException
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        // Get the userId and mainEmail
        String mainEmail = user.getEmail();
        String userId = user.getUserId();

        String displayName = profileForm.getDisplayName();
        boolean isMuted = profileForm.isMuted();

        // Get the Profile from the datastore if it exists
        // otherwise create a new one
        Profile profile = ofy().load().key(Key.create(Profile.class, userId))
                .now();

        if (profile == null) {
            // Populate the displayName and teeShirtSize with default values
            // if not sent in the request
            if (displayName == null) {
                displayName = extractDefaultDisplayNameFromEmail(user
                        .getEmail());
            }
            // Now create a new Profile entity
            profile = new Profile(userId, displayName, mainEmail);
            profile.setMute(isMuted);
        } else {
            // The Profile entity already exists
            // Update the Profile entity
            profile.update(displayName);
            profile.setMute(isMuted);
        }
        ofy().save().entity(profile).now();

        // Return the profile
        return profile;
    }

    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException when the User object is null.
     */
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = ApiMethod.HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        String userId = user.getUserId();
        Key key = Key.create(Profile.class, userId);

        Profile profile = (Profile) ofy().load().key(key).now();
        return profile;
    }

    /**
     * Gets the Profile entity for the current user
     * or creates it if it doesn't exist
     *
     * @param user
     * @return user's Profile
     */
    private static Profile getProfileFromUser(User user) {
        // First fetch the user's Profile from the datastore.
        Profile profile = ofy().load().key(
                Key.create(Profile.class, user.getUserId())).now();
        if (profile == null) {
            // Create a new Profile if it doesn't exist.
            // Use default displayName and teeShirtSize
            String email = user.getEmail();
            profile = new Profile(user.getUserId(),
                    extractDefaultDisplayNameFromEmail(email), email);
        }
        return profile;
    }

    /**
     * Add a trigger to the backend
     *
     */
    @ApiMethod(name = "addTimeRoute", httpMethod = ApiMethod.HttpMethod.POST)
    public TimeRoute addTimeRoute(final User user, TimeRouteConfigForm configForm)
            throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        Key<Profile> profileKey = Key.create(Profile.class, user.getUserId());
        final Key<TimeRoute> key = factory().allocateId(profileKey, TimeRoute.class);
        TimeRoute record = new TimeRoute(key.getId(), user.getUserId(), configForm);
        ofy().save().entity(record).now();
        return record;
    }

    /**
     * delete a trigger
     * @param webSafeRouteKey
     */
    @ApiMethod(name = "deleteTimeRoute",
            path = "timeRoute/{webSafeRouteKey}/delete", httpMethod = ApiMethod.HttpMethod.DELETE)
    public void deleteTimeRoute(final User user, @Named("webSafeRouteKey") String webSafeRouteKey)
            throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        Key<TimeRoute> timeRouteKey = Key.create(webSafeRouteKey);
        ofy().delete().key(timeRouteKey).now();
//        List<TimeRoute> records = findRecord(user, routeName);
//        for (TimeRoute record : records) {
//            ofy().delete().entity(record).now();
//        }
    }

    /**
     * Get a trigger with a specific routeName for a user
     *
     * @param webSafeRouteKey The key to the timeroute
     * @return a list of time route configs with  spcific name for a user
     */
    @ApiMethod(name = "getTimeRoute",
            path = "timeRoute/{webSafeRouteKey}",
            httpMethod = ApiMethod.HttpMethod.POST)
    public TimeRoute getTimeRoute(final User user, @Named("webSafeRouteKey") String webSafeRouteKey) throws NotFoundException {
        Key<TimeRoute> timeRouteKey = Key.create(webSafeRouteKey);
        TimeRoute timeRoute = ofy().load().key(timeRouteKey).now();
        if (timeRoute == null) {
            throw new NotFoundException("No time route found with key: " + webSafeRouteKey);
        }
        return timeRoute;
    }

    /**
     * Return a collection of triggers for a user
     *
     * @return a list of time route configs for a user
     */
    @ApiMethod(name = "listTimeRoutes", path = "listTimeRoutes",
            httpMethod = ApiMethod.HttpMethod.POST)
    public CollectionResponse<TimeRoute> listTimeRoutes(final User user)
            throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        // Get the key for the User's Profile.order("name")
        Key userKey = Key.create(Profile.class, user.getUserId());
        Query query = ofy().load().type(TimeRoute.class).ancestor(userKey);
        List<TimeRoute> records = query.list();
        return CollectionResponse.<TimeRoute>builder().setItems(records).build();
    }

    /**
     * Return a collection of triggers for a user
     *
     * @param count The number of devices to list
     * @return a list of time route configs for a user
     */
    @ApiMethod(name = "listAllTimeRoutes", path = "listAllTimeRoutes")
    public CollectionResponse<TimeRoute> listAllTimeRoutes( @Named("count") int count) {
        List<TimeRoute> records = ofy().load().type(TimeRoute.class).limit(count).list();
        return CollectionResponse.<TimeRoute>builder().setItems(records).build();
    }

}