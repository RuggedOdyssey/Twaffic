package net.ruggedodyssey.backend;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Created by daneel on 2014/08/11.
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
                    .setOAuthConsumerKey("Ugu0UZX7byRaQXiLBm2dz6Tot")
                    .setOAuthConsumerSecret("WeK0tLFizrI8TNQFRvO0BB8BHO3T3bJLQTQti36lHiMzRpIA4o")
                    .setOAuthAccessToken("13935452-UhuwdRWSfIFqNzkL49P3mQklpbHVDAENlxVmGnEYF")
                    .setOAuthAccessTokenSecret("8XDKIePcyUVyCaZS2kbjZvGYE49Z4frFJKuCOtDNRWENc");
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
