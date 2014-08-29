package net.ruggedodyssey.backend.service;

import net.ruggedodyssey.backend.Constants;

import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Wrapper class to access the Twitter service through the twitter4j libraries.
 */
public class TwitterAPI {

    static Status tweet;

    public TwitterAPI() {

    }

    public static int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }


    public static String getTestTweet() {
        String tweettext = "";
        try {
            String user = "Capetownfreeway";
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(Constants.oauth_consumerKey)
                    .setOAuthConsumerSecret(Constants.oauth_consumerSecret)
                    .setOAuthAccessToken(Constants.oauth_accessToken)
                    .setOAuthAccessTokenSecret(Constants.oauth_accessTokenSecret)
                    .setIncludeEntitiesEnabled(true);
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            List<Status> statuses = twitter.getUserTimeline(user);

            Status status = statuses.get(randomWithRange(0, statuses.size()));
            tweet = status;
            tweettext = status.getText();

        } catch (TwitterException te) {
            te.printStackTrace();
            tweettext = te.getMessage();
        }
        return tweettext;
    }

    public static long getTweetID(){
        return tweet.getId();
    }

    public static String getTweetURL(){
        String url = "";
        if (tweet != null && tweet.getMediaEntities().length > 0){
            for (MediaEntity ue : tweet.getMediaEntities()) {
                url += ue.getURL() == null ? "" : ue.getMediaURL();
//                url += ue.getDisplayURL() == null ? "#" : ue.getDisplayURL();
//                url += ue.getExpandedURL() == null ? "#" : ue.getExpandedURL();
            }
        }
        return url;
    }

    public static String getTweetDate() {
        return tweet.getCreatedAt().toString();
    }
}
