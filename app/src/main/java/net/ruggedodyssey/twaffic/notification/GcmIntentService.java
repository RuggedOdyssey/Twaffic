package net.ruggedodyssey.twaffic.notification;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import net.ruggedodyssey.twaffic.data.TwafficUpdateContract.TweetEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by daneel on 2014/08/12.
 */
public class GcmIntentService extends IntentService {

    private final Context mContext;
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

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

                sendMessage(extras.getString("id"), extras.getString("message"), extras.getString("url"), extras.getString("date"));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public static String getDbDateFromString(String date){
        try {
            Locale dateLocale = Locale.getDefault();
            SimpleDateFormat inFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", dateLocale);
            Date returnDate = inFormat.parse(date);
            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", dateLocale);
            return outFormat.format(returnDate);
        }
        catch (ParseException e) {
                Log.d("receiver", "Error converting date");
        }
        return "";
    }

    private void sendMessage(String id, final String message, String url, String date) {
        String formattedDate = getDbDateFromString(date);

        Log.d("sender", "Broadcasting message 1");
        long tweetId = Long.valueOf(id);

        addTweet(tweetId, message, formattedDate, url, null);

        Intent intent = new Intent("GCM_message_received");
        // You can also include some extra data.
        intent.putExtra("message", message);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        intent.putExtra("date", formattedDate);
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