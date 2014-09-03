package net.ruggedodyssey.backend.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.cmd.Query;

import net.ruggedodyssey.backend.domain.TimeRoute;
import net.ruggedodyssey.backend.form.TimeRouteConfigForm;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static net.ruggedodyssey.backend.service.OfyService.ofy;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 *
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 *
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(name = "trigger", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.ruggedodyssey.net", ownerName = "backend.ruggedodyssey.net", packagePath=""))
public class TriggerConfigEndpoint {

    private static final Logger log = Logger.getLogger(TriggerConfigEndpoint.class.getName());

    /**
     * Add a trigger to the backend
     *
     */
    @ApiMethod(name = "add", httpMethod = ApiMethod.HttpMethod.POST)
    public void add(final User user, TimeRouteConfigForm configForm)
            throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        TimeRoute record = findRecord(user, configForm.getRouteName()).get(0);
        if(record == null) {
            record = new TimeRoute();
        }
        record.setFieldsFromForm(user.getUserId(), configForm);
        ofy().save().entity(record).now();
    }

    /**
     * delete a trigger
     * @param routeName
     */
    @ApiMethod(name = "delete", httpMethod = ApiMethod.HttpMethod.POST)
    public void delete(final User user, @Named("routeName") String routeName)
            throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        List<TimeRoute> records = findRecord(user, routeName);
        for (TimeRoute record : records) {
            ofy().delete().entity(record).now();
        }
    }

    /**
     * Return a collection of triggers for a user
     *
     * @param count The number of devices to list
     * @return a list of time route configs for a user
     */
    @ApiMethod(name = "list")
    public CollectionResponse<TimeRoute> listTriggers(final User user, @Named("count") int count)
            throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        List<TimeRoute> records = findRecordsForUser(user);
        return CollectionResponse.<TimeRoute>builder().setItems(records).build();
    }

    /**
     * Return a collection of triggers for a user
     *
     * @param count The number of devices to list
     * @return a list of time route configs for a user
     */
    @ApiMethod(name = "listall", path = "all")
    public CollectionResponse<TimeRoute> listAllTriggers( @Named("count") int count) {
        List<TimeRoute> records = ofy().load().type(TimeRoute.class).limit(count).list();
        return CollectionResponse.<TimeRoute>builder().setItems(records).build();
    }


    /**
     * Find a record for a specific user with a specific routeName
     * @param user
     * @param routeName
     * @return
     */
    private List<TimeRoute> findRecord(final User user,  String routeName) {
        Query<TimeRoute> query = ofy().load().type(TimeRoute.class);
        query = query.filter("userId = ", user.getUserId());
        query = query.filter("routeName = ", routeName);
        return query.list();
    }

    /**
     * Find all records for a user
     * @param user
     * @return
     */
    private List<TimeRoute> findRecordsForUser(final User user) {
        Query<TimeRoute> query = ofy().load().type(TimeRoute.class);
        query = query.filter("userId = ", user.getUserId());
        return query.list();
    }

}