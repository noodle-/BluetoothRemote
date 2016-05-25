package com.hszuyd.bluetoothremote;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
	General g = new General(LoginActivity.this);            // Load the General class

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		g.checkBluetooth();
	}

	public void buttonStartAppOnClick(View view) {
		EditText text = (EditText) findViewById(R.id.editText);
		TextInputLayout mInputName = (TextInputLayout) findViewById(R.id.nameWrapper);
		if (text != null) {                                 // Make sure we've found the view
			String name = text.getText().toString().trim(); // Remove trailing spaces
			if (checkName(name)) {
				//mInputName.setErrorEnabled(false);                                    // Set the textView to non error layout in case the users input was wrong before TODO Fix this, as it currently does not work and makes the app crash.
				Intent intent = new Intent(getBaseContext(), MainActivity.class);       // Create intent for going from here to KickPanel
				intent.putExtra("NAME_PLAYER", name);                                   // Add player name to the intent as Extra
				ActivityOptions transitionActivityOptions =                             // Set animation options
						ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, text, "ToMain");
				startActivity(intent, transitionActivityOptions.toBundle());            // Start new activity with player name and animation
			} else {
				mInputName.setErrorEnabled(true);
				mInputName.setError("Your name may only contain letters, numbers and spaces");   // Show a hint to indicate that it was false input}
			}
		}
	}

	/**
	 * Check whether the user has entered a name. Show a dialog if he has not.
	 */
	private boolean checkName(String name) {
		final String VALID_NAME = "[a-zA-Z0-9 ]+";          // Regex string to make the user choose a name with at least one character, only including numbers and letters
		Pattern pattern = Pattern.compile(VALID_NAME);
		Matcher matcher;
		matcher = pattern.matcher(name);                    // Compare regex string to name
		return matcher.matches();                           // Return whether the name matches the regex pattern
	}
}
