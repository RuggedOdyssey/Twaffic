package net.ruggedodyssey.backend.domain;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by maia on 2014/09/03.
 */
public class ScoreWordTest {

    private static final String WORD = "blah";

    private static final int SCORE_1 = 1;
    private static final Integer SCORE_2 = 2;

    private ScoreWord scoreWord;
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() {
        helper.setUp();
        scoreWord = new ScoreWord(WORD, SCORE_1);
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testGetter() {
        assertEquals(WORD, scoreWord.getWord());
        assertTrue(scoreWord.getScore().equals(SCORE_1));
    }


    @Test
    public  void testSetScore() {
        scoreWord.setScore(SCORE_2);
        assertTrue(scoreWord.getScore().equals(SCORE_2));
    }
}
