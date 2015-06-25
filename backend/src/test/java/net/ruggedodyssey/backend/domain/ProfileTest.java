package net.ruggedodyssey.backend.domain;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProfileTest {

    private static String EMAIL = "test@example.com";
    private static String DISPLAYNAME = "test";
    private static String DISPLAYNAME2 = "test2";
    private static String USERID = "123456";

    private Profile profile;

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        profile = new Profile(USERID, DISPLAYNAME, EMAIL);
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testGetters() throws Exception {
        assertTrue(EMAIL.equals(profile.getMainEmail()));
        assertTrue(USERID.equals(profile.getUserId()));
        assertTrue(DISPLAYNAME.equals(profile.getDisplayName()));
    }


    @Test
    public void testUpdate() throws Exception {
        profile.update(DISPLAYNAME2);
        assertTrue(DISPLAYNAME2.equals(profile.getDisplayName()));
        assertFalse(profile.muted);
        profile.setMute(true);
        assertTrue(profile.muted);
    }
}