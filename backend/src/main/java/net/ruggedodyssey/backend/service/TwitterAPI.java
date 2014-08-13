package net.ruggedodyssey.backend.service;

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
        int i = 0;
        try {
            i = 1;
            String user = "Capetownfreeway";
            //System.out.println("|START|");
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("***")
                    .setOAuthConsumerSecret("***")
                    .setOAuthAccessToken("***")
                    .setOAuthAccessTokenSecret("***");
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            i = 2;
            List<Status> statuses = twitter.getUserTimeline(user);

            //System.out.println("Showing @" + user + "'s home timeline.");
            i = 3;
            for (Status status : statuses) {
                tweet = status.getText();
            }
            i = 4;

        } catch (TwitterException te) {
            te.printStackTrace();
            tweet = te.getMessage();
            //System.out.println("Failed to get timeline: " + te.getMessage());
            //System.exit(-1);
        }
        return tweet + "["+ String.valueOf(i)+ "]";
    }

}
