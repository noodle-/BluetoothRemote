package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
		/*BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
		if (BTAdapter == null) {
			new AlertDialog.Builder(this)
					.setTitle("Not compatible")
					.setMessage("Your phone does not support Bluetooth")
					.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.exit(0);
						}
					})
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();}*/
	}

	public void button_Start_App_OnClick(View view) {
		EditText text = (EditText) findViewById(R.id.editText);
		String name = text.getText().toString().trim();

		//This is commented for test purposes only.
		/*if(Objects.equals(name, "")) {
			AlertDialog.Builder nameAlert = new AlertDialog.Builder(this);
			nameAlert.setMessage("Please insert your name.");
			nameAlert.setCancelable(true);
			nameAlert.setPositiveButton(
					"Okay",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog nameAlert1 = nameAlert.create();
			nameAlert1.show();
		}
		else{*/
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		intent.putExtra("NAME_PLAYER", name);
		startActivity(intent);
		//}
	}
}
