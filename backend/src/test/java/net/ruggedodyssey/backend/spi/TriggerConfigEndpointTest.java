package net.ruggedodyssey.backend.spi;

import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import net.ruggedodyssey.backend.domain.TimeRoute;
import net.ruggedodyssey.backend.form.TimeRouteConfigForm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TriggerConfigEndpointTest {

    private static final long ID = 123456L;
    private static final String EMAIL = "example@gmail.com";
    private static final String USER_ID = "123456789";
    private static final String ROUTE_NAME = "test route";
    private static final String SEARCH_STRING = "N1 AND outbound";

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
    TriggerConfigEndpoint triggerConfigEndpoint;
    private Date startTime;
    private Date endTime;

    private TimeRouteConfigForm form;
    private User user;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        triggerConfigEndpoint = new TriggerConfigEndpoint();
        user = new User(EMAIL, "gmail.com", USER_ID);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        startTime = dateFormat.parse("14:00:00");
        endTime = dateFormat.parse("16:00:00");
        form = new TimeRouteConfigForm(ROUTE_NAME, startTime, endTime, true, true, true, true, true, false, false, SEARCH_STRING);
    }

    @After
    public void tearDown() {
//        ofy().clear(); //TODO this makes the tests fail
        helper.tearDown();
    }

    @Test
    public void testAdd() throws Exception {
        triggerConfigEndpoint.addTimeRoute(user, form);
        List<TimeRoute> triggers = triggerConfigEndpoint.getTimeRoute(user, ROUTE_NAME);
        assertEquals("Add TimeRoute failed", 1, triggers.size());
        TimeRoute r = triggers.get(0);
        assertTrue(r.getMonday());
        assertTrue(r.getTuesday());
        assertTrue(r.getWednesday());
        assertTrue(r.getThursday());
        assertTrue(r.getFriday());
        assertTrue(r.getSaturday());
        assertTrue(r.getSunday());
        assertTrue(ROUTE_NAME.equalsIgnoreCase(r.getRouteName()));
    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testListTriggers() throws Exception {

    }

    @Test
    public void testListAllTriggers() throws Exception {

    }
}