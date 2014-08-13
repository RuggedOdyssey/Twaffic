package net.ruggedodyssey.twaffic;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        return rootView;
    }

    public void setText(String text){
        Log.d("mainfragment", "setting text to: " + text);
        TextView textView = (TextView) rootView.findViewById(R.id.textview);
        textView.setText(text);
    }


}
