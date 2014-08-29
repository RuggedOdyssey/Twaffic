package net.ruggedodyssey.twaffic.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import net.ruggedodyssey.twaffic.R;
import net.ruggedodyssey.twaffic.data.TwafficUpdateContract.TweetEntry;

/**
 * Created by daneel on 2014/08/12.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class TweetFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TWEET_LOADER = 0;
    private SimpleCursorAdapter mTweetAdapter;
    View rootView;

    private static final String[] TWEET_COLUMNS = {
        TweetEntry.TABLE_NAME + "." + TweetEntry._ID,
        TweetEntry.COLUMN_TWEET_Date,
        TweetEntry.COLUMN_TWEET_Text,
        TweetEntry.COLUMN_TWEET_Url,
        TweetEntry.COLUMN_TWEET_Keywords
    };

    public static final int COL_TWEET_ID = 0;
    public static final int COL_TWEET_DATE_= 1;
    public static final int COL_TWEET_TEXT_= 2;
    public static final int COL_TWEET_URL_= 3;
    public static final int COL_TWEET_KEYWORDS_= 4;

    public TweetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTweetAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item_tweet,
                null,
                // the column names to use to fill the textviews
                new String[]{TweetEntry.COLUMN_TWEET_Date,
                        TweetEntry.COLUMN_TWEET_Text,
                        TweetEntry.COLUMN_TWEET_Url,
                        TweetEntry.COLUMN_TWEET_Keywords
                },
                // the textviews to fill with the data pulled from the columns above
                new int[]{R.id.list_item_tweet_date,
                        R.id.list_item_tweet_text,
                        R.id.list_item_tweet_keywords,
                        R.id.list_item_tweet_url
                },
                0
        );

//        mTweetAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//                switch (columnIndex) {
//                    case COL_TWEET_DATE_:
//                    case COL_TWEET_TEXT_:
//                    case COL_TWEET_URL_:
//                }
//                return false;
//            }
//        });

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_tweet);
        listView.setAdapter(mTweetAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Cursor cursor = mTweetAdapter.getCursor();
//                if (cursor != null && cursor.moveToPosition(position)){
//                    Bundle bundle = new Bundle();
//                    bundle.putString("tweet", cursor.getString(COL_TWEET_TEXT_));
//                    bundle.putString("tweetUrl", cursor.getString(COL_TWEET_URL_));
//                    Intent intent = new Intent(getActivity(), DetailActivity.class)
//                            .putExtra(Intent.EXTRA_TEXT, bundle);
//                }
//            }
//        });

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TWEET_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        Uri tweetUri = TweetEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),
                tweetUri,
                TWEET_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTweetAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mTweetAdapter.swapCursor(null);
    }
}
