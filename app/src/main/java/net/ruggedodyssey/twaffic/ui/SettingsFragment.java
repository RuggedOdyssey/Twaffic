package net.ruggedodyssey.twaffic.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.ruggedodyssey.twaffic.R;

public class SettingsFragment extends PreferenceActivity {

    public static final String KEY_PREF_GPLUS = "PREF_GPLUS";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsActivityFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.preference_screen, false);
    }

    public static class SettingsActivityFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_screen);

            Preference startPreference = findPreference("PREF_GPLUS");
            Intent startIntent = new Intent(getActivity().getApplication(), GPlusSignIn.class);
            startIntent.setAction("android.intent.action.VIEW");
            startPreference.setIntent(startIntent);
        }

        @Override
        public void onResume(){
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public  void onPause(){
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(KEY_PREF_GPLUS)) {
                Preference serverIPPref = findPreference(key);
                serverIPPref.setSummary(sharedPreferences.getString(key, ""));
            }
        }
    }
}
