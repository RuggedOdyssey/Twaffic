package net.ruggedodyssey.backend.domain;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import net.ruggedodyssey.backend.form.TimeRouteConfigForm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by maia on 2014/09/03.
 */
public class TimeRouteTest {
    private static final long ID = 123456L;
    private static final String EMAIL = "example@gmail.com";
    private static final String USER_ID = "123456789";
    private static final String ROUTE_NAME = "test route";

    private TimeRoute timeRoute;
    private TimeRouteConfigForm form;
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
    private Date startTime;
    private Date endTime;

    @Before
    public void setUp() throws Exception{
        helper.setUp();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        startTime = dateFormat.parse("14:00:00");
        endTime = dateFormat.parse("16:00:00");
        form = new TimeRouteConfigForm(ROUTE_NAME, startTime, endTime, true, true, true, true, true, false, false);
    }

    @After
    public void tearDown() {
        helper.tearDown();

    }
    @Test(expected = NullPointerException.class)
    public void testMustHaveRouteName() {
        TimeRouteConfigForm nullForm = new TimeRouteConfigForm(null, startTime, endTime, true, true, true, true, true, false, false);
        new TimeRoute(ID, USER_ID, nullForm);
    }
    @Test
    public void testSetFieldsFromForm() {
//        TimeRouteConfigForm form = new TimeRouteConfigForm(ROUTE_NAME, startTime, endTime);
    }
}
