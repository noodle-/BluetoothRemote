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


public class LoginScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();

		// Show different dialogs based on Bluetooth adapter status, show none if there's a bluetooth adapter and it's enabled
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
					.setMessage("Bluetooth is currently disabled. You will have to click 'Enable' to continue using this app")
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

	public void button_Start_App_OnClick(View view) {
		EditText text = (EditText) findViewById(R.id.editText);
		String name = text.getText().toString().trim();

		// TODO Remove this when not testing.
		// Check whether the user has entered a name. Not entering a name could cause problems later on.
		/*if (Objects.equals(name, "")) {
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
		} else { // User has entered a name, so we're sending his name to the next activity */
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		intent.putExtra("NAME_PLAYER", name);
		startActivity(intent);
		//}
	}
}
