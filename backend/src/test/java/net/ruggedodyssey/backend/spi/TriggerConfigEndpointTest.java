package net.ruggedodyssey.backend.spi;

import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

import net.ruggedodyssey.backend.domain.Profile;
import net.ruggedodyssey.backend.domain.TimeRoute;
import net.ruggedodyssey.backend.form.ProfileForm;
import net.ruggedodyssey.backend.form.TimeRouteConfigForm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static net.ruggedodyssey.backend.service.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TriggerConfigEndpointTest {

    private static final long ID = 123456L;
    private static final String EMAIL = "example@gmail.com";
    private static final String USER_ID = "123456789";
    private static final String DISPLAY_NAME = "example";
    private static final String ROUTE_NAME = "test route";
    private static final String ROUTE_NAME2 = "test route2";
    private static final String SEARCH_STRING = "N1 AND outbound";

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
    TriggerConfigEndpoint triggerConfigEndpoint;
    private Date startTime;
    private Date endTime;

    private TimeRouteConfigForm form;
    private TimeRouteConfigForm form2;
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
        form2 = new TimeRouteConfigForm(ROUTE_NAME2, startTime, endTime, true, true, true, true, true, false, false, SEARCH_STRING);
    }

    @After
    public void tearDown() {
        ofy().clear();
        helper.tearDown();
    }

    @Test
    public void testGetProfileFirstTime() throws Exception {
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertNull(profile);
        profile = triggerConfigEndpoint.getProfile(user);
        assertNull(profile);
    }

    @Test
    public void testSaveProfile() throws Exception {
        // Save the profile for the first time.
        Profile profile = triggerConfigEndpoint.saveProfile(
                user, new ProfileForm(DISPLAY_NAME, false));
        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertFalse(profile.isMuted());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertFalse(profile.isMuted());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }

    @Test
    public void testSaveProfileWithNull() throws Exception {
        // Save the profile for the first time with null values.
        Profile profile = triggerConfigEndpoint.saveProfile(user, new ProfileForm(null, false));
        String displayName = EMAIL.substring(0, EMAIL.indexOf("@"));
        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertFalse(profile.isMuted());
        assertEquals(displayName, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertFalse(profile.isMuted());
        assertEquals(displayName, profile.getDisplayName());
    }

    @Test
    public void testGetProfile() throws Exception {
        triggerConfigEndpoint.saveProfile(user, new ProfileForm(DISPLAY_NAME, true));
        // Fetch the Profile via the API.
        Profile profile = triggerConfigEndpoint.getProfile(user);
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertTrue(profile.isMuted());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        // Save for the first time.
        triggerConfigEndpoint.saveProfile(user, new ProfileForm(DISPLAY_NAME, false));
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertFalse(profile.isMuted());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Then try to update it.
        String newDisplayName = "New Name";
        boolean newMuteState = true;
        triggerConfigEndpoint.saveProfile(user, new ProfileForm(newDisplayName, newMuteState));
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(newMuteState, profile.isMuted());
        assertEquals(newDisplayName, profile.getDisplayName());
    }

    @Test
    public void testUpdateProfileWithNulls() throws Exception {
        triggerConfigEndpoint.saveProfile(user, new ProfileForm(DISPLAY_NAME, false));
        // Update the Profile with null values.
        Profile profile = triggerConfigEndpoint.saveProfile(user, new ProfileForm(null, false));
        // Expected behavior is that the existing properties do not get overwritten

        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertFalse(profile.isMuted());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertFalse(profile.isMuted());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }


    @Test
    public void testAddTimeRoute() throws Exception {
        Profile profile = new Profile(USER_ID, DISPLAY_NAME, EMAIL);
        ofy().save().entity(profile).now();
        TimeRoute r = triggerConfigEndpoint.addTimeRoute(user, form);
        assertNotNull(r);
        assertTrue(r.getMonday());
        assertTrue(r.getTuesday());
        assertTrue(r.getWednesday());
        assertTrue(r.getThursday());
        assertTrue(r.getFriday());
        assertFalse(r.getSaturday());
        assertFalse(r.getSunday());
        assertTrue(ROUTE_NAME.equalsIgnoreCase(r.getRouteName()));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteTimeRoute() throws Exception {
        Profile profile = new Profile(USER_ID, DISPLAY_NAME, EMAIL);
        ofy().save().entity(profile).now();
        TimeRoute r = triggerConfigEndpoint.addTimeRoute(user, form);
        assertNotNull(r);
        triggerConfigEndpoint.deleteTimeRoute(user, r.getWebsafeKey());
        triggerConfigEndpoint.getTimeRoute(user, r.getWebsafeKey());
    }

    @Test
    public void testGetTimeRoute() throws Exception {
        Profile profile = new Profile(USER_ID, DISPLAY_NAME, EMAIL);
        ofy().save().entity(profile).now();
        TimeRoute r = triggerConfigEndpoint.addTimeRoute(user, form);
        TimeRoute res = triggerConfigEndpoint.getTimeRoute(user, r.getWebsafeKey());
        assertNotNull(res);
        assertTrue(r.getRouteName().equals(res.getRouteName()));
    }


    //@Test
    public void testListTimeRoutes() throws Exception {
        Profile profile = new Profile(USER_ID, DISPLAY_NAME, EMAIL);
        ofy().save().entity(profile).now();
        TimeRoute r = triggerConfigEndpoint.addTimeRoute(user, form);
        TimeRoute r2 = triggerConfigEndpoint.addTimeRoute(user, form2);

        CollectionResponse<TimeRoute> timeRoutesRes = triggerConfigEndpoint.listTimeRoutes(user);
        Collection<TimeRoute> timeRoutes = timeRoutesRes.getItems();
        assertEquals(2, timeRoutes.size());
        assertTrue("The result should contain a time route with name " + ROUTE_NAME,
                timeRoutes.contains(r));
        assertTrue("The result should contain a time route with name " + ROUTE_NAME2,
                timeRoutes.contains(r2));
    }

    //@Test
    public void testListAllTimeRoutes() throws Exception {
        Profile profile = new Profile(USER_ID, DISPLAY_NAME, EMAIL);
        ofy().save().entity(profile).now();
        TimeRoute r = triggerConfigEndpoint.addTimeRoute(user, form);
        TimeRoute r2 = triggerConfigEndpoint.addTimeRoute(user, form2);

        CollectionResponse<TimeRoute> timeRoutesRes = triggerConfigEndpoint.listAllTimeRoutes(10);
        Collection<TimeRoute> timeRoutes = timeRoutesRes.getItems();
        assertEquals(2, timeRoutes.size());
        assertTrue("The result should contain a time route with name " + ROUTE_NAME,
                timeRoutes.contains(r));
        assertTrue("The result should contain a time route with name " + ROUTE_NAME2,
                timeRoutes.contains(r2));

    }
}