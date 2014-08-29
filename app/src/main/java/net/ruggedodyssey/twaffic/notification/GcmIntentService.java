package net.ruggedodyssey.twaffic.notification;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import net.ruggedodyssey.twaffic.data.TwafficUpdateContract.TweetEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by daneel on 2014/08/12.
 */
public class GcmIntentService extends IntentService {

    private final Context mContext;
    public static final String DATE_FORMAT = "EEE, d MMM HH:mm";

    public GcmIntentService() {
        super("GcmIntentService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                //showToast(extras.getString("message"));
                //Log.d("sender", extras.getString("message"));
                sendMessage(extras.getString("id"), extras.getString("message"), extras.getString("url"), extras.getString("date"));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getFormattedDateString(Date date){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromString(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }
    private void sendMessage(String id, final String message, String url, String date) {
//        Date tweetDate  = getDateFromString(date);
//        date = getFormattedDateString(tweetDate);

        Log.d("sender", "Broadcasting message 1");
        long tweetId = Long.valueOf(id);

        addTweet(tweetId, message, date, url, null);

        Intent intent = new Intent("GCM_message_received");
        // You can also include some extra data.
        intent.putExtra("message", message);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        intent.putExtra("date", date);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private long addTweet(long ID, String text, String date, String url, String keywords) {

        // First, check if the location with this city name exists in the db
        Cursor cursor = mContext.getContentResolver().query(
                TweetEntry.CONTENT_URI,
                new String[]{TweetEntry._ID},
                TweetEntry.COLUMN_TWEET_Id+ " = ?",
                new String[]{String.valueOf(ID)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int tweetIndex = cursor.getColumnIndex(TweetEntry._ID);
            return cursor.getLong(tweetIndex);
        } else {
            ContentValues tweetValues = new ContentValues();
            tweetValues.put(TweetEntry.COLUMN_TWEET_Id, ID);
            tweetValues.put(TweetEntry.COLUMN_TWEET_Date, date);
            tweetValues.put(TweetEntry.COLUMN_TWEET_Text, text);
            tweetValues.put(TweetEntry.COLUMN_TWEET_Url, url);
            tweetValues.put(TweetEntry.COLUMN_TWEET_Keywords, keywords);

            Uri TweetInsertUri = mContext.getContentResolver()
                    .insert(TweetEntry.CONTENT_URI, tweetValues);

            return ContentUris.parseId(TweetInsertUri);
        }
    }
}