/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ruggedodyssey.twaffic.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ruggedodyssey.twaffic.R;
import net.ruggedodyssey.twaffic.data.TwafficUpdateContract.TweetEntry;


public class DetailActivity extends Activity {

    public static final String TWEET_KEY = "tweet_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        public static final String TWEET_KEY = "tweet_id";
        private long mTweetId;
        private static final int DETAIL_LOADER = 0;

        private static final String[] TWEET_COLUMNS = {
                TweetEntry.TABLE_NAME + "." + TweetEntry._ID,
                TweetEntry.COLUMN_TWEET_Date,
                TweetEntry.COLUMN_TWEET_Text,
                TweetEntry.COLUMN_TWEET_Url,
                TweetEntry.COLUMN_TWEET_Keywords
        };

        public DetailFragment() {
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putString(TWEET_KEY, String.valueOf(mTweetId));
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onResume() {
            super.onResume();
            if (mTweetId != 0) {
                getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            if (savedInstanceState != null) {
                mTweetId = Long.valueOf(savedInstanceState.getString(TWEET_KEY));
            }
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            Intent intent = getActivity().getIntent();
            if (intent == null || !intent.hasExtra(TWEET_KEY)) {
                return null;
            }
            long mTweetId = Long.valueOf(intent.getStringExtra(TWEET_KEY));

            // Sort order:  Ascending, by date.
            String sortOrder = TweetEntry.COLUMN_TWEET_Date+ " DESC";

            Uri TweetUri = TweetEntry.buildTweetUriWithId(mTweetId);

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    TweetUri,
                    TWEET_COLUMNS,
                    null,
                    null,
                    sortOrder
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
            if (!data.moveToFirst()) { return; }

            String dateString = data.getString(data.getColumnIndex(TweetEntry.COLUMN_TWEET_Date));
            ((TextView) getView().findViewById(R.id.detail_date))
                    .setText(dateString);

            String tweetString = data.getString(data.getColumnIndex(TweetEntry.COLUMN_TWEET_Text));
            TextView tweetTextView = (TextView)getView().findViewById(R.id.detail_text);
            tweetTextView.setText(tweetString);
            Linkify.addLinks(tweetTextView, Linkify.ALL);

            String tweetUrl = data.getString(data.getColumnIndex(TweetEntry.COLUMN_TWEET_Url));
            ImageView tweetImage = (ImageView) getView().findViewById(R.id.detail_tweet_Image);
            Picasso.with(getActivity()).load(tweetUrl).into(tweetImage);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {

        }
    }
}
