package net.ruggedodyssey.twaffic.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daneel on 2014/08/22.
 */
public class TwafficUpdateContract {

    public static final String CONTENT_AUTHORITY = "net.ruggedodyssey.twaffic.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TWEETS = "tweets";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getDbDateString(Date date){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }

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

        public static Uri buildTweetUriWithId(long id){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_TWEET_Date);
        }

        public static long getTweetIdFromUri(Uri uri) {
            return Long.valueOf(uri.getPathSegments().get(1));
        }

    }
}
