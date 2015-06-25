package net.ruggedodyssey.backend.service;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import net.ruggedodyssey.backend.domain.Profile;
import net.ruggedodyssey.backend.domain.RegistrationRecord;
import net.ruggedodyssey.backend.domain.ScoreWord;
import net.ruggedodyssey.backend.domain.TimeRoute;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
        ObjectifyService.register(RegistrationRecord.class);
        ObjectifyService.register(TimeRoute.class);
        ObjectifyService.register(ScoreWord.class);
        ObjectifyService.register(Profile.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
