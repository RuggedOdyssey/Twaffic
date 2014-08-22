package net.ruggedodyssey.backend.servlet;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet for checking the twitter feeds and figuring out if notifications have to be sent.
 * Created by maia on 2014/08/13.
 */
@SuppressWarnings("serial")
public class CheckFeedsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO we have to query the db to figure out who's feed to check with which strings
        // then if a notification must be sent put it in memcache and send it.
//        // Query for conferences with less than 5 seats left
//        Iterable<Conference> iterable = ofy().load().type(Conference.class)
//                .filter("seatsAvailable <", 5)
//                .filter("seatsAvailable >", 0);
//
//        // TODO
//        // Iterate over the conferences with less than 5 seats less
//        // and get the name of each one
//        List<String> conferenceNames = new ArrayList<>(0);
//        for (Conference conference : iterable) {
//            conferenceNames.add(conference.getName());
//        }
//        if (conferenceNames.size() > 0) {
//
//            // Build a String that announces the nearly sold-out conferences
//            StringBuilder announcementStringBuilder = new StringBuilder(
//                    "Last chance to attend! The following conferences are nearly sold out: ");
//            Joiner joiner = Joiner.on(", ").skipNulls();
//            announcementStringBuilder.append(joiner.join(conferenceNames));
//
//            // TODO
//            // Get the Memcache Service
            MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
//
//
//            // TODO
//            // Put the announcement String in memcache,
//            // keyed by Constants.MEMCACHE_ANNOUNCEMENTS_KEY
//            memcacheService.put(Constants.MEMCACHE_ANNOUNCEMENTS_KEY, announcementStringBuilder.toString());
//
//        }

        // Set the response status to 204 which means
        // the request was successful but there's no data to send back
        // Browser stays on the same page if the get came from the browser
        response.setStatus(204);
    }
}

