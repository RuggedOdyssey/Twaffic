package net.ruggedodyssey.twaffic.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daneel on 2014/08/22.
 */
public class TwafficUpdateContract {

    public static final String CONTENT_AUTHORITY = "net.ruggedodyssey.twaffic.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TWEETS = "tweets";

    public static final class TweetEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TWEETS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TWEETS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TWEETS;

        // Table name
        public static final String TABLE_NAME = "tweets";

        public static final String COLUMN_TWEET_Id = "_id";
        public static final String COLUMN_TWEET_Date = "date";
        public static final String COLUMN_TWEET_Text = "text";
        public static final String COLUMN_TWEET_Url = "url";
        public static final String COLUMN_TWEET_Keywords = "keywords";

        public static Uri buildTweetUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTweetWithDate(String date){
            return CONTENT_URI.buildUpon().appendPath(date).build();
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_TWEET_Date);
        }

    }
}
