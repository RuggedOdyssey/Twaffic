package net.ruggedodyssey.backend.spi;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TriggerConfigEndpointTest {
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
    TriggerConfigEndpoint triggerConfigEndpoint;

    @Before
    public void setUp() {
        helper.setUp();
        triggerConfigEndpoint = new TriggerConfigEndpoint();
    }

    @After
    public void tearDown() {
//        ofy().clear(); //TODO this makes the tests fail
        helper.tearDown();
    }

    @Test
    public void testAdd() throws Exception {

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