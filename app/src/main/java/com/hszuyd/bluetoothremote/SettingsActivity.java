package com.hszuyd.bluetoothremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
	General g = new General(SettingsActivity.this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
		Context context = getBaseContext();
		SharedPreferences asdf = context.getSharedPreferences("pref_leesplankje", MODE_PRIVATE);
		final String asdfString = asdf.getString("pref_leesplankje", "");
		g.showToast(asdfString);
	}

	public static class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);

			findPreference("pref_leesplankje").setOnPreferenceChangeListener(this);
		}

		@Override
		public boolean onPreferenceChange(Preference pref, Object newValue) {
			findPreference("pref_leesplankje").setSummary((String) newValue);

			SharedPreferences current = getActivity().getSharedPreferences("pref_leesplankje", MODE_PRIVATE);
			current.edit().putString("pref_leesplankje", newValue.toString())
					.apply();
			return true;
		}
	}
}


