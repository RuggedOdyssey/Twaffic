package net.ruggedodyssey.backend.domain;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by maia on 2014/09/03.
 */
public class TimeRouteTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
    @Before
    public void setUp() {
        helper.setUp();

    }

    @After
    public void tearDown() {
        helper.tearDown();

    }

    @Test
    public void testSetFieldsFromForm() {

    }
}
