package net.ruggedodyssey.twaffic.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.ruggedodyssey.twaffic.R;
import net.ruggedodyssey.twaffic.spi.EndpointsAsyncTask;

/**
 * Created by daneel on 2014/08/12.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    View rootView;
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //TextView tv = (TextView)rootView.findViewById(R.id.textview);
        setText("Tweet to show here");
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EndpointsAsyncTask().execute(new Pair<Context, String>(getActivity(), "dummy"));
            }
        });

        return rootView;
    }

    public void setText(String text){
        Log.d("mainfragment", "setting text to: " + text);
        TextView textView = (TextView) rootView.findViewById(R.id.textview);
        textView.setText(text);
    }


}
