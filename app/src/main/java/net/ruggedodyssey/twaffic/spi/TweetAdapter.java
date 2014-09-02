package net.ruggedodyssey.twaffic.spi;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ruggedodyssey.twaffic.R;
import net.ruggedodyssey.twaffic.ui.TweetFragment;
import net.ruggedodyssey.twaffic.ui.Utility;

/**
 * Created by daneel on 2014/09/01.
 */

public class TweetAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_FEATURED = 0;
    private static final int VIEW_TYPE_OTHER = 1;

    Context context;


    public TweetAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_FEATURED: {
                layoutId = R.layout.list_item_tweet_first;
                break;
            }
            case VIEW_TYPE_OTHER: {
                layoutId = R.layout.list_item_tweet;
                break;
            }
        }
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Read weather icon ID from cursor
        long tweetId = cursor.getInt(TweetFragment.COL_TWEET_ID);

        // Read date from cursor
        String dateString = cursor.getString(TweetFragment.COL_TWEET_DATE_);

        TextView dateView = (TextView) view.findViewById(R.id.list_item_tweet_date);
        dateView.setText(Utility.getDisplayDateFromString(dateString));

        String tweetString = cursor.getString(TweetFragment.COL_TWEET_TEXT_);
        TextView tweetTextView = (TextView) view.findViewById(R.id.list_item_tweet_text);
        tweetTextView.setText(tweetString.split("http")[0]);

        String tweetUrl = cursor.getString(TweetFragment.COL_TWEET_URL_);
        int viewType = getItemViewType(cursor.getPosition());
        if (viewType == VIEW_TYPE_FEATURED) {
            ImageView tweetImage = (ImageView) view.findViewById(R.id.list_item_tweet_image);
            Picasso.with(context).load(tweetUrl).into(tweetImage);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_FEATURED : VIEW_TYPE_OTHER;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
