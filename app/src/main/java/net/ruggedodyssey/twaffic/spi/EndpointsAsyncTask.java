package net.ruggedodyssey.twaffic.spi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import net.ruggedodyssey.backend.messaging.Messaging;
import net.ruggedodyssey.backend.trigger.Trigger;

import java.io.IOException;

/**
 * Created by maia on 2014/08/03.
 */
public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private Messaging myApiService;
    private Trigger myTriggerService;
    private Context context;

    public EndpointsAsyncTask() {
        Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);
//        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
//                // options for running against local devappserver
//                // - 10.0.2.2 is localhost's IP address in Android emulator
//                // - turn off compression when running against local devappserver
//                .setRootUrl("http://10.0.2.2:8080/_ah/api/")
//                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                    @Override
//                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                        abstractGoogleClientRequest.setDisableGZipContent(true);
//                    }
//                });
        // end options for devappserver

        myApiService = builder.build();
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        context = params[0].first;
        String name = params[0].second;

        try {
            myApiService.sendTestTweet().execute();
            return "Tweet sent";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
