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

public class LoginScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		//this is commented for test purposes only.
		// Check whether we've even got a bluetooth adapter, because if we don't have bluetooth, you won't be able to do anything useful with the app anyways.
		BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
		/*if (BTAdapter == null) {
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
		}*/
	}

	public void button_Start_App_OnClick(View view) {
		EditText text = (EditText) findViewById(R.id.editText);
		String name = text.getText().toString().trim();

		//This is commented for test purposes only.
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
		} else { // User has entered a name*/
			Intent intent = new Intent(getBaseContext(), MainActivity.class);
			intent.putExtra("NAME_PLAYER", name);
			startActivity(intent);
		//}
	}
}
