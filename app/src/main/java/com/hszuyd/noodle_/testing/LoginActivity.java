package com.hszuyd.noodle_.testing;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
	General g = new General(LoginActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		g.checkBluetooth();
	}

	public void buttonStartAppOnClick(View view) {
		EditText text = (EditText) findViewById(R.id.editText);
		String name = text.getText().toString().trim(); // Remove trailing spaces

//		if (checkName(name)) {                          // TODO Remove this when not testing.
		// Start MainActivity and give it "name" through an intent
		Intent intent = new Intent(getBaseContext(), MainActivity.class);       // Create intent for going from here to KickPanel
		intent.putExtra("NAME_PLAYER", name);                                   // Add player name to the intent as Extra
		ActivityOptions transitionActivityOptions =                             // Set animation options
				ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, text, "ToMain");
		startActivity(intent, transitionActivityOptions.toBundle());            // Start new activity with player name and animation
//		}
	}

	/**
	 * Check whether the user has entered a name. Show a dialog if he has not.
	 */
	private boolean checkName(String name) {
		if (Objects.equals(name, "")) {
			new AlertDialog.Builder(this)
					.setTitle("Invalid name")
					.setMessage("Please insert your name.")
					.setCancelable(true)
					.setPositiveButton(
							"Okay",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							})
					.setIcon(R.drawable.ic_warning)
					.show();
			return false;
		} else {
			return true;    // User has entered a valid name, so we're sending his name to the next activity
		}
	}
}
