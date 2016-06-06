package com.hszuyd.bluetoothremote;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the actual Preferences fragment. This is the recommended way because this is a lot more dynamic. The preferences fragment can be called from anywhere.
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

		// Show the current value of the pref_leesplankje preference
//		General g = new General(SettingsActivity.this);
//		Context context = getBaseContext();
//		SharedPreferences asdf = context.getSharedPreferences("pref_leesplankje", MODE_PRIVATE);
//		String asdfString = asdf.getString("pref_leesplankje", "");
//		g.showToast(asdfString);
	}

	public static class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);                          // Load the preferences layout
			findPreference("pref_leesplankje").setOnPreferenceChangeListener(this); // Initialize the change listener
			findPreference("pref_display_name").setOnPreferenceChangeListener(this); // Initialize the change listener
		}

		@Override
		public boolean onPreferenceChange(Preference pref, Object newValue) {
			findPreference("pref_leesplankje").setSummary((String) newValue);       // Set the summary to the new value
			findPreference("pref_display_name").setSummary((String) newValue);       // Set the summary to the new value

			// Store the new value.
			SharedPreferences current = getActivity().getSharedPreferences("pref_leesplankje", MODE_PRIVATE);   // TODO not sure about using "pref_leesplankje" right here
			current.edit().putString("pref_leesplankje", newValue.toString())
					.putString("pref_display_name", newValue.toString())
					.apply();
			return true;
		}
	}
}


