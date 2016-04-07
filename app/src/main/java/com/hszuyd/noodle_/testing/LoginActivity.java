package com.hszuyd.noodle_.testing;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
	final BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();

	public void button_Start_App_OnClick(View view) {
		EditText text = (EditText) findViewById(R.id.editText);
		String name = text.getText().toString().trim(); // Remove trailing spaces

//		if (checkName(name)) {                          // TODO Remove this when not testing.
		// Start MainActivity and give it "name" through an intent
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		intent.putExtra("NAME_PLAYER", name);
		startActivity(intent);
//		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		checkBluetooth();
	}

	/**
	 * Show different dialogs based on Bluetooth adapter availability, show none if there's a bluetooth adapter and it's enabled
	 */
	private void checkBluetooth() {
		if (BTAdapter == null) {
			new AlertDialog.Builder(this)
					.setTitle("Warning")
					.setMessage("Bluetooth adapter not found. \n" +
							"\n" +
							"Please run this app on a different device instead.")
					.setPositiveButton(
							"Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									System.exit(0);
								}
							})
					.setIcon(R.drawable.ic_warning)
					.show();
		} else if (!BTAdapter.isEnabled()) {
			new AlertDialog.Builder(this)
					.setTitle("Warning")
					.setMessage("Bluetooth is currently disabled. \n" +
							"\n" +
							"Please click 'Enable Bluetooth' to continue using this app")
					.setPositiveButton(
							"Enable Bluetooth",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									BTAdapter.enable();
								}
							}
					)
					.setNegativeButton("Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									System.exit(0);
								}
							})
					.setIcon(R.drawable.ic_warning)
					.show();
		}
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
