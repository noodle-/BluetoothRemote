package com.hszuyd.bluetoothremote;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	}

	public static class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);

			findPreference("pref_leesplankje").setOnPreferenceChangeListener(this);
		}
		@Override
		public boolean onPreferenceChange(Preference pref, Object newValue) {
			findPreference("pref_leesplankje").setSummary((String) newValue);
			return true;
		}
	}
}


