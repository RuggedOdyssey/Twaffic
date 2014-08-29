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
     * Implicit key
     */
    @Id
    Long id;

    /**
     * The word.
     */
    String word;

    /** The score to weight the algorithm. Positive makes it more likely that
     *  the word trigger a notification. Negative makes it less likely that the word triggers a
     *  notification.
     */
    Integer score;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
