package net.ruggedodyssey.backend.service;

import net.ruggedodyssey.backend.Constants;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Wrapper class to access the Twitter service through the twitter4j libraries.
 */
public class TwitterAPI {

    public TwitterAPI() {

    }

    public static String getTestTweet() {
        String tweet = "";
        try {
            String user = "Capetownfreeway";
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(Constants.oauth_consumerKey)
                    .setOAuthConsumerSecret(Constants.oauth_consumerSecret)
                    .setOAuthAccessToken(Constants.oauth_accessToken)
                    .setOAuthAccessTokenSecret(Constants.oauth_accessTokenSecret);
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            List<Status> statuses = twitter.getUserTimeline(user);

            for (Status status : statuses) {
                tweet = status.getText();
            }

        } catch (TwitterException te) {
            te.printStackTrace();
            tweet = te.getMessage();

        }
        return tweet;
    }

}
