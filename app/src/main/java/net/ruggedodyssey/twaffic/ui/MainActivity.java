package net.ruggedodyssey.twaffic.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import net.ruggedodyssey.twaffic.R;
import net.ruggedodyssey.twaffic.notification.GCMUtils;
import net.ruggedodyssey.twaffic.notification.GcmRegistrationAsyncTask;
import net.ruggedodyssey.twaffic.spi.EndpointsAsyncTask;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("GCM_message_received"));


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TweetFragment())
                    .commit();
        }

        Context context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }


    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = GCMUtils.getGCMPreferences(context);
        String registrationId = prefs.getString(GCMUtils.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(GCMUtils.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = GCMUtils.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }



    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new GcmRegistrationAsyncTask().execute(this);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            //if (intent.hasExtra("message") && intent.hasExtra("id")) {
                String message;
                message = intent.getStringExtra("message");
                String url = intent.getStringExtra("url");
                long id = Long.valueOf(intent.getStringExtra("id")==null?"0":intent.getStringExtra("id"));
                Log.d("receiver", "Got message: " + message);

                //MainFragment fragment_obj = (MainFragment)getFragmentManager().findFragmentById(R.id.mainfragment);
                Log.d("receiver", "Got message: more");

                createNotification("Traffic Update", message, url, id);
            //}

        }
    };


    public void createNotification(String headline, String contentText, String url, long id) {

        Intent resultIntent = new Intent(this, DetailActivity.class)
                .putExtra(DetailActivity.TWEET_KEY, String.valueOf(id));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_jeep256);
            // Build notification
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(headline)
                            //TODO: Refer to http://developer.android.com/guide/topics/ui/notifiers/notifications.html#HandlingNotifications
                    .setContentText(contentText)
                            //.setContentInfo(contentText)
                    .setTicker(headline)  //This creates ticker text on every notification refresh, so not using it anymore.
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.ic_stat_jeep256)
                    .setLargeIcon(bm)
                    .setStyle(new Notification.BigTextStyle().bigText(contentText))
                    .setWhen(System.currentTimeMillis())
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Hide the notification after its selected
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, notification);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_tweet) {
            new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "dummy"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}
