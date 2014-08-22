package net.ruggedodyssey.twaffic.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.ruggedodyssey.twaffic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by daneel on 2014/08/12.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class TweetFragment extends Fragment {

    View rootView;
    public TweetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] data = {
                "Update 2",
                "Update 3"
        };
        List<String> tweetList = new ArrayList<String>(Arrays.asList(data));

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_tweet, // The name of the layout ID.
                        R.id.list_item_tweet_textview, // The ID of the textview to populate.
                        tweetList);

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_tweet);
        listView.setAdapter(forecastAdapter);

        return rootView;
    }


}
