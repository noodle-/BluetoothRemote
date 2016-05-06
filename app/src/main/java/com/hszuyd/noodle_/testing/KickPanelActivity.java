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
	private String mConnectedDeviceName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kickpanel);

		// Hide the ConnectedDeviceName until we're connected
		TextView mTextViewConnectedDeviceName = (TextView) findViewById(R.id.text_kickpanel_connected_device);
		if (mTextViewConnectedDeviceName != null) {
			mTextViewConnectedDeviceName.setVisibility(View.INVISIBLE);
		}

		// Gets the Intent from LoginActivity/MainActivity
		Intent intentName = getIntent();
		Bundle nameBundle = intentName.getExtras();
		name = (String) nameBundle.get("NAME_PLAYER");

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);                // Load toolbar (with title, icon etc)
		setSupportActionBar(mToolbar);                                          // Cast toolbar as actionbar
		getSupportActionBar().setDisplayShowHomeEnabled(true);                  // Show home/back button
		assert mToolbar != null;
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

//	public void buttonClickKickpanelTest(View view) {
//		receivedDialog("bleeep", true);
//	}

	public void buttonClickKickpanelListen(View view) {
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

	private void receivedDialog(String message, boolean input) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);  // Initialize the builder
		alert.setTitle("Message received!");
		alert.setMessage(message);                  // Show the message that we've received
		String mButtonText;                         // Initialize mButtonText
		if (input) {                                // if the Raspberry is asking for input
			final EditText mEditText = new EditText(getBaseContext());  // Initialize editText
			mEditText.setTextColor(0xFF000000);        // Set text color because its otherwise white for some reason
			alert.setView(mEditText);                                   // Set the view to display in the dialog
			alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String input = mEditText.getText().toString();      // Store user input in "input"
					bt.send(input, false);                              // Send whatever the user has entered
				}
			});
			mButtonText = "Cancel"; // Set mButtonText to be used later on
		} else {
			mButtonText = "Okay";   // Set mButtonText to be used later on
		}
		alert.setNegativeButton(mButtonText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});

		alert.show();
	}

	private void startBluetoothService() {
		bt.startService(BluetoothState.DEVICE_OTHER);   // OR   //bt.startService(BluetoothState.DEVICE_ANDROID);

		bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
			public void onDataReceived(byte[] data, String message) {
				Log.i(TAG, "OnDataReceivedListener -> " + message);
				if (message.contains("UserProfile setup")) {
					// ignore it
				} else if (message.contains("HIT!")) {
					// yay
				} else if (message.contains("MISS!")) {
					// aww
				} else if (message.contains("provide a username")) {       // When the name is requested, send it
					bt.send(name, false);
				} else if (message.contains("Please")) {
					receivedDialog(message, BluetoothState.INPUT_REQUIRED);    //show dynamic dialog
				} else {
					receivedDialog(message, BluetoothState.INPUT_NOT_REQUIRED);    //show dynamic dialog
				}
			}
		});

		bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
			public void onDeviceConnected(String name, String address) {
				Log.i(TAG, "BluetoothConnectionListener -> onDeviceConnected"
						+ "\nname: " + name
						+ "\taddress " + address);
				mConnectedDeviceName = name;
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
				TextView mTextViewBTState = (TextView) findViewById(R.id.text_kickpanel_bluetoothstate);
				TextView mTextViewBTDeviceName = (TextView) findViewById(R.id.text_kickpanel_connected_device);
				mTextViewBTDeviceName.setVisibility(View.INVISIBLE);
				if (mTextViewBTState != null) {
					if (state == BluetoothState.STATE_CONNECTED) {
						Log.i(TAG, "BluetoothStateListener -> STATE_CONNECTED");
						mTextViewBTState.setText(getString(R.string.kickpanel_bt_state_connected));
						mTextViewBTDeviceName.setText("Device: " + mConnectedDeviceName);
						mTextViewBTDeviceName.setVisibility(View.VISIBLE);
					} else if (state == BluetoothState.STATE_CONNECTING) {
						Log.i(TAG, "BluetoothStateListener -> STATE_CONNECTING");
						mTextViewBTState.setText(getString(R.string.kickpanel_bt_state_connecting));
					} else if (state == BluetoothState.STATE_LISTEN) {
						Log.i(TAG, "BluetoothStateListener -> STATE_LISTEN");
						mTextViewBTState.setText(getString(R.string.kickpanel_bt_state_listening));
					} else if (state == BluetoothState.STATE_NONE) {
						Log.i(TAG, "BluetoothStateListener -> STATE_NONE");
						mTextViewBTState.setText(getString(R.string.kickpanel_bt_state_none));
					}
				}
			}
		});
	}
}