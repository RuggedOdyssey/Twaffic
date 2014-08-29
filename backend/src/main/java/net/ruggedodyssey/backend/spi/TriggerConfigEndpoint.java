package net.ruggedodyssey.backend.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import net.ruggedodyssey.backend.domain.TimeRoute;

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
     * Register a device to the backend
     *
     */
    @ApiMethod(name = "add")
    public void add(@Named("userId") String userId, @Named("regId") String name) {
        if(findRecord(userId, name) != null) {
            log.info("Trigger " + userId + "with name " + name + " already registered, skipping register");
            return;
        }
        TimeRoute record = new TimeRoute();
        ofy().save().entity(record).now();
    }

    /**
     * delete a trigger
     * @param userId
     * @param name
     */
    @ApiMethod(name = "delete")
    public void delete(@Named("userId") String userId, @Named("regId") String name) {
        TimeRoute record = findRecord(userId, name);
        if(record == null) {
            log.info("Trigger " + userId + "with name " + name + " not registered, skipping unregister");
            return;
        }
        ofy().delete().entity(record).now();
    }

    /**
     * Return a collection of registered devices
     *
     * @param count The number of devices to list
     * @return a list of Google Cloud Messaging registration Ids
     */
    @ApiMethod(name = "list")
    public CollectionResponse<TimeRoute> listDevices(@Named("count") int count) {
        List<TimeRoute> records = ofy().load().type(TimeRoute.class).limit(count).list();
        return CollectionResponse.<TimeRoute>builder().setItems(records).build();
    }

//TODO is this right? How do we identify a timeroute from the user point of view? UserId and nameString
    private TimeRoute findRecord(String userId, String nameSTring) {
        return ofy().load().type(TimeRoute.class).filter("userId", userId).first().now();
    }

}