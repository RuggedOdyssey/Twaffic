package net.ruggedodyssey.backend.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import net.ruggedodyssey.backend.domain.ScoreWord;

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
@Api(name = "scoreword", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.ruggedodyssey.net", ownerName = "backend.ruggedodyssey.net", packagePath=""))
public class ScoreWordEndpoint {

    private static final Logger log = Logger.getLogger(ScoreWordEndpoint.class.getName());

    /**
     * Register a device to the backend
     *
     * @param word search term
     * @param score score that influences if the word will cause a notification
     */

    @ApiMethod(name = "add")
    public void addWord(@Named("word") String word, @Named("score") Integer score) {
        ScoreWord record = findRecord(word);
        if(record == null) {
            record = new ScoreWord();
        }
        record.setScore(score);
        ofy().save().entity(record).now();
    }

    /**
     * Unregister a device from the backend
     *
     * @param word The Google Cloud Messaging registration Id to remove
     */
    @ApiMethod(name = "delete")
    public void deleteWord(@Named("word") String word) {
        ScoreWord record = findRecord(word);
        if(record == null) {
            log.info("Word " + word + " not in list, skipping delete");
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
    @ApiMethod(name = "listWords")
    public CollectionResponse<ScoreWord> listWords(@Named("count") int count) {
        List<ScoreWord> records = ofy().load().type(ScoreWord.class).limit(count).list();
        return CollectionResponse.<ScoreWord>builder().setItems(records).build();
    }

    private ScoreWord findRecord(String word) {
        return ofy().load().type(ScoreWord.class).filter("word", word).first().now();
    }

}