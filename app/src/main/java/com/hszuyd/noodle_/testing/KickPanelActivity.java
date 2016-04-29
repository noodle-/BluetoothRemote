package com.hszuyd.noodle_.testing; // TODO change package name?

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import app.akexorcist.bluetoothspp.library.BluetoothSPP;
import app.akexorcist.bluetoothspp.library.BluetoothState;

public class KickPanelActivity extends AppCompatActivity {
	private static final String TAG = "KickPanelActivity";
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	private BluetoothSPP bt = new BluetoothSPP(KickPanelActivity.this);
	private General g = new General(KickPanelActivity.this);
	private String name;
	private String amountOfRounds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kickpanel);

		// Gets the Intent from Loginscreen/MainActivity
		Intent intentName = getIntent();
		Bundle nameBundle = intentName.getExtras();
		name = (String) nameBundle.get("NAME_PLAYER");

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);                // Load toolbar (with title, icon etc)
		setSupportActionBar(mToolbar);                                          // Cast toolbar as actionbar
		getSupportActionBar().setDisplayShowHomeEnabled(true);                  // Show home/back button
		mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);    // Set back button icon
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {      // Initialize the onclick listener to navigate back
			@Override
			public void onClick(View v) {
				supportFinishAfterTransition();                                 // Finish with animation
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (bt.getServiceState() != BluetoothState.STATE_NONE) {
			bt.stopService();
		}
		supportFinishAfterTransition();     // Use the transition while going back as well
	}

	public void buttonClickKickpanelA(View view) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText edittext = new EditText(getBaseContext());
		alert.setMessage("Enter the amount of rounds that you want to play");
		alert.setTitle("How many rounds?");

		alert.setView(edittext);        // Set focus to the edit text and open the keyboard

		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//What ever you want to do with the value
				//Editable YouEditTextValue = edittext.getText();
				amountOfRounds = edittext.getText().toString();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Close the dialog
			}
		});

		alert.show();
	}

	public void buttonClickKickpanelB(View view) {
		if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
			//bt.send("2", false);
			bt.send(amountOfRounds, false);
			g.showToast("Sent some hardcoded stuff");
		} else {
			g.showToast("Can't send anything because we're not connected");
		}
	}

	public void buttonClickKickpanelC(View view) {
		Log.i(TAG, "SetupService()");
		bt.setupService();
		startBluetoothService();
		g.showToast("Bluetooth service started and listening");
	}

	public void buttonStartDeviceList(View v) {
		Intent intent = new Intent(this, DeviceListActivity.class);             // Create intent for going from here to KickPanel
		ActivityOptions transitionActivityOptions =                             // Set animation options
				ActivityOptions.makeSceneTransitionAnimation(KickPanelActivity.this, v, "ToDeviceList");
		startActivityForResult(intent, REQUEST_DEVICE_ADDRESS, transitionActivityOptions.toBundle());            // Start new activity with player name and animation
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DEVICE_ADDRESS) {        // Check which request we're responding to. When doing more requests a switch case is probably a nicer way of doing this.
			if (resultCode == RESULT_OK) {                  // Make sure the request was successful
				if (data.hasExtra("EXTRA_DEVICE_ADDRESS")) {
					Bundle bundleResult = data.getExtras(); // Store the Intent data(=device address) that we've received from the DeviceListActivity in a bundle. The bundle consists of "EXTRA_DEVICE_ADDRESS, MAC_ADDRESS"
					String device = bundleResult.getString("EXTRA_DEVICE_ADDRESS");

					bt.setupService();
					startBluetoothService();
					Log.i(TAG, "Connecting to " + device);
					bt.connect(device);
				} else {
					Log.i(TAG, "Failed to get MAC address from ?");
				}
			}
		}
	}

	private void startBluetoothService() {
		bt.startService(BluetoothState.DEVICE_OTHER);   // OR   //bt.startService(BluetoothState.DEVICE_ANDROID);

		bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
			public void onDataReceived(byte[] data, String message) {
				Log.i(TAG, "OnDataReceivedListener -> "
						//+ "\ndata " + data
						+ message);
				g.showToast(message);

				// When the name is requested, send it
				if (message.equals("Please provide a username\n")) {
					bt.send(name, false);
				} else if (message.equals("Please provide the number of rounds to play (under 20)")) {
					//bt.send(numberofrounds, false);
					g.showToast("temp");
				}
			}
		});

		bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
			public void onDeviceConnected(String name, String address) {
				Log.i(TAG, "BluetoothConnectionListener -> onDeviceConnected"
						+ "\nname: " + name
						+ "\taddress " + address);
			}

			public void onDeviceDisconnected() {
				Log.i(TAG, "BluetoothConnectionListener -> onDeviceDisconnected");
			}

			public void onDeviceConnectionFailed() {
				Log.i(TAG, "BluetoothConnectionListener -> onDeviceConnectionFailed");
			}
		});

		bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
			public void onServiceStateChanged(int state) {
				TextView textView = (TextView) findViewById(R.id.text_kickpanel_welcome);
				assert textView != null;
				if (state == BluetoothState.STATE_CONNECTED) {
					Log.i(TAG, "BluetoothStateListener -> STATE_CONNECTED");
					textView.setText(getString(R.string.kickpanel_bt_state_connected));
					//TODO ENABLE TEH BUTTONS
//					buttonSelectRounds = (Button) findViewById(R.id.button_choose_rounds);
//					buttonSendRounds.setVisibility(View.GONE);
				} else if (state == BluetoothState.STATE_CONNECTING) {
					Log.i(TAG, "BluetoothStateListener -> STATE_CONNECTING");
					textView.setText(getString(R.string.kickpanel_bt_state_connecting));
					// TODO Show a pretty activity indicator
				} else if (state == BluetoothState.STATE_LISTEN) {
					Log.i(TAG, "BluetoothStateListener -> STATE_LISTEN");
					textView.setText(getString(R.string.kickpanel_bt_state_listening));
					// TODO Show connect to button
				} else if (state == BluetoothState.STATE_NONE) {
					Log.i(TAG, "BluetoothStateListener -> STATE_NONE");
					textView.setText(getString(R.string.kickpanel_bt_state_none));
					// TODO Show connect to button
				}
			}
		});
	}
}