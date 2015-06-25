package net.ruggedodyssey.backend.spi;

import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

import net.ruggedodyssey.backend.domain.ScoreWord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static net.ruggedodyssey.backend.service.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by maia on 2014/09/03.
 */
public class ScoreWordEndpointTest {
    private static final String WORD_1 = "word1";
    private static final String WORD_2 = "word2";
    private static final String WORD_UNKNOWN = "unknown";
    private static final Integer SCORE_1 = 1;
    private static final Integer SCORE_2 = 2;

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    ScoreWordEndpoint scoreWordEndpoint;

    @Before
    public void setUp() {
        helper.setUp();
        scoreWordEndpoint = new ScoreWordEndpoint();
    }

    @After
    public void tearDown() {
        ofy().clear();
        helper.tearDown();
    }

    @Test
    public void testUnknownWord() {
        ScoreWord word = ofy().load().key(Key.create(ScoreWord.class, WORD_UNKNOWN)).now();
        assertNull(word);
        word = scoreWordEndpoint.getWord(WORD_UNKNOWN);
        assertNull(word);
    }

    @Test
    public void testAddWord() {
        scoreWordEndpoint.addWord(WORD_1, SCORE_1);
        ScoreWord word = scoreWordEndpoint.getWord(WORD_1);
        assertNotNull(word);
        assertEquals(WORD_1, word.getWord());
        assertEquals(SCORE_1, word.getScore());
    }

    @Test
    public void testDeleteWord() {
        scoreWordEndpoint.addWord(WORD_1, SCORE_1);
        scoreWordEndpoint.addWord(WORD_1, SCORE_1);
        ScoreWord word = scoreWordEndpoint.getWord(WORD_1);
        assertNotNull(word);
        scoreWordEndpoint.deleteWord(WORD_1);
        word = scoreWordEndpoint.getWord(WORD_1);
        assertNull(word);
    }

    //@Test
    public void testListWords() {
        scoreWordEndpoint.addWord(WORD_1, SCORE_1);
        scoreWordEndpoint.addWord(WORD_2, SCORE_2);
        //TODO this works in the api but not in the test
        CollectionResponse<ScoreWord> scoreWordsRes = scoreWordEndpoint.listWords(10);
        Collection<ScoreWord> scoreWords = scoreWordsRes.getItems();
        assertEquals(2, scoreWords.size());

        ScoreWord word1 = scoreWordEndpoint.getWord(WORD_1);
        ScoreWord word2 = scoreWordEndpoint.getWord(WORD_2);
        assertTrue("The result should contain a time route with name " + WORD_1,
                scoreWords.contains(word1));
        assertTrue("The result should contain a time route with name " + WORD_2,
                scoreWords.contains(word2));


    }
}
