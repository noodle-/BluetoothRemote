package com.hszuyd.noodle_.testing; // TODO change package name?

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import app.akexorcist.bluetoothspp.library.BluetoothSPP;
import app.akexorcist.bluetoothspp.library.BluetoothState;

public class KickPanelActivity extends AppCompatActivity {
	private static final String TAG = "KickPanelActivity";
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	private BluetoothSPP bt = new BluetoothSPP(KickPanelActivity.this);
	private General g = new General(KickPanelActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kickpanel);

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
		if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
			bt.send("Henk", false);
//			bt.send("Hoiiiii!", true);
//			bt.send(new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x21}, false); // "Hello world!" in hex
			g.showToast("Sent some hardcoded stuff");
		} else {
			g.showToast("Can't send anything because we're not connected");
		}
	}

	public void buttonClickKickpanelB(View view) {
		if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
			bt.send("2", false);
			g.showToast("Sent some hardcoded stuff");
		} else {
			g.showToast("Can't send anything because we're not connected");
		}
	}

	public void buttonClickKickpanelC(View view) {
		Log.e(TAG, "SetupService()");
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
					Log.e(TAG, "Connecting to " + device);
					bt.connect(device);
				} else {
					Log.e(TAG, "Failed to get MAC address from ?");
				}
			}
		}
	}

	private void startBluetoothService() {
		// TODO Dialog choose between android and other
		///bt.startService(BluetoothState.DEVICE_ANDROID);
		bt.startService(BluetoothState.DEVICE_OTHER);

		bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
			public void onDataReceived(byte[] data, String message) {
				Log.e(TAG, "OnDataReceivedListener ->"
						+ "\ndata " + data
						+ "\nmessage " + message);
				g.showToast(message);
			}
		});

		bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
			public void onDeviceConnected(String name, String address) {
				Log.e(TAG, "BluetoothConnectionListener -> onDeviceConnected"
						+ "\nname: " + name
						+ "\taddress " + address);
			}

			public void onDeviceDisconnected() {
				Log.e(TAG, "BluetoothConnectionListener -> onDeviceDisconnected");
			}

			public void onDeviceConnectionFailed() {
				Log.e(TAG, "BluetoothConnectionListener -> onDeviceConnectionFailed");
			}
		});

		bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
			public void onServiceStateChanged(int state) {
				TextView textView = (TextView) findViewById(R.id.text_kickpanel_welcome);
				assert textView != null;
				if (state == BluetoothState.STATE_CONNECTED) {
					Log.e(TAG, "BluetoothStateListener -> STATE_CONNECTED");
					textView.setText(getString(R.string.kickpanel_bt_state_connected));
				} else if (state == BluetoothState.STATE_CONNECTING) {
					Log.e(TAG, "BluetoothStateListener -> STATE_CONNECTING");
					textView.setText(getString(R.string.kickpanel_bt_state_connecting));
				} else if (state == BluetoothState.STATE_LISTEN) {
					Log.e(TAG, "BluetoothStateListener -> STATE_LISTEN");
					textView.setText(getString(R.string.kickpanel_bt_state_listening));
				} else if (state == BluetoothState.STATE_NONE) {
					Log.e(TAG, "BluetoothStateListener -> STATE_NONE");
					textView.setText(getString(R.string.kickpanel_bt_state_none));
				}
			}
		});
	}
}