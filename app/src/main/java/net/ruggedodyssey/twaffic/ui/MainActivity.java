package net.ruggedodyssey.twaffic.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.ruggedodyssey.twaffic.R;
import net.ruggedodyssey.twaffic.notification.GcmRegistrationAsyncTask;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("GCM_message_received"));


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
        new GcmRegistrationAsyncTask().execute(this);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);

            //MainFragment fragment_obj = (MainFragment)getFragmentManager().findFragmentById(R.id.mainfragment);
            Log.d("receiver", "Got message: more");
            TextView textFragment = (TextView)findViewById(R.id.textview);
            //((TextView) fragment_obj.getView().findViewById(R.id.textview)).setText(message);
            //fragment_obj.setText(message);
            textFragment.setText(message);
            createNotification("Traffic Update", message);

        }
    };


    public void createNotification(String statusText, String contentText) {
//        Intent mainIntent = new Intent(this, FragmentSettings.class);
//        mainIntent.setAction("android.intent.action.VIEW)");
//        mainIntent.putExtra("Action", "Preferences");
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addNextIntent(mainIntent);
//
//        PendingIntent pMainIntent = stackBuilder.getPendingIntent(
//                0,
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );

        try {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.jeep256small);
            // Build notification
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(statusText)
                            //TODO: Refer to http://developer.android.com/guide/topics/ui/notifiers/notifications.html#HandlingNotifications
                    .setContentText(contentText)
                            //.setContentInfo(contentText)
                    .setTicker(statusText)  //This creates ticker text on every notification refresh, so not using it anymore.
//                    .setContentIntent(pMainIntent)
                    .setSmallIcon(R.drawable.jeep24)
                    .setLargeIcon(bm)
                    .setWhen(System.currentTimeMillis())
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Hide the notification after its selected
            //notification.flags |= Notification.FLAG_AUTO_CANCEL;

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
