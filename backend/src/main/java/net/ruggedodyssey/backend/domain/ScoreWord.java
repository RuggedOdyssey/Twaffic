package net.ruggedodyssey.backend.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * List of scorewords, good and bad, used to figure out if a notification must be sent
 * Created by maia on 2014/08/29.
 */
@Entity
public class ScoreWord {

    /**
     * The word. We only want the word to appear once in the list so we can use it as a key.
     */
    @Id
    String word;

    /** The score to weight the algorithm. Positive makes it more likely that
     *  the word trigger a notification. Negative makes it less likely that the word triggers a
     *  notification.
     */
    Integer score;


    /**
     * Constructor only for tests
     * @param word
     * @param score
     */
    public ScoreWord(String word, int score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
