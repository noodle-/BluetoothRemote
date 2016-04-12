package com.hszuyd.noodle_.testing; // TODO change package name?

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import app.akexorcist.bluetoothspp.library.BluetoothSPP;
import app.akexorcist.bluetoothspp.library.BluetoothState;

public class KickPanelActivity extends AppCompatActivity {
	private static final String TAG = "KickPanelActivity";
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	BluetoothSPP bt = new BluetoothSPP(KickPanelActivity.this);
	General g = new General(KickPanelActivity.this);
	MenuItem mDynamicMenuIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kickpanel);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		if (fab != null)
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Snackbar.make(view, "This is a snackbar!", Snackbar.LENGTH_LONG)
							.setAction("SHOW TOAST", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									g.showToast("This is a Toast!");
								}
							}).show();
				}
			});
	}

	protected void onResume() {
		super.onResume();
		g.checkBluetooth();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		mDynamicMenuIcon = menu.findItem(R.id.tb_bluetooth);
		mDynamicMenuIcon.setIcon(R.drawable.ic_favorite_white);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();      // get ID of the clicked button so we know where we've clicked and act based on that
		if (id == R.id.tb_bluetooth) {
			if (Math.random() > 0.5) {  // Show a different icon based on random int between 0.0 and 1.0
				mDynamicMenuIcon.setIcon(R.drawable.ic_thumb_up);
			} else {
				mDynamicMenuIcon.setIcon(R.drawable.ic_thumb_down);
			}
		}
		return super.onOptionsItemSelected(item);   // Why?
	}

	public void button_click_kickpanel_A(View view) {
		if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
			bt.send("Hoiiiii!", true);
			bt.send(new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x21}, false); // "Hello world!" in hex
			g.showToast("Sent some hardcoded stuff");
		} else {
			g.showToast("Start Bluetooth service first!");
		}
	}

	public void button_click_kickpanel_B(View view) {
		Log.e(TAG, "SetupService()");
		bt.setupService();
		startBluetoothService();
		g.showToast("Bluetooth service started and listening");
	}

	public void button_start_device_list(View v) {
		Intent intent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(intent, REQUEST_DEVICE_ADDRESS);
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
					g.showToast("Connecting to " + device); //TODO Remove this when we've successfully sent through the address
				} else {
					Log.e(TAG, "Failed to get MAC address from ?");
				}
			}
		}
	}

	private void startBluetoothService() {
		//For connection with android device
		///bt.startService(BluetoothState.DEVICE_ANDROID);
		// TODO Find a way to do this automatically
		//For connection with any micro-controller which communication with bluetooth serial port profile module
		bt.startService(BluetoothState.DEVICE_OTHER);

		bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
			public void onDataReceived(byte[] data, String message) {
				Log.e(TAG, "OnDataReceivedListener ->"
						+ "\ndata " + data
						+ "\nmessage " + message);
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
				if (state == BluetoothState.STATE_CONNECTED) {
					Log.e(TAG, "BluetoothStateListener -> STATE_CONNECTED");
				} else if (state == BluetoothState.STATE_CONNECTING) {
					Log.e(TAG, "BluetoothStateListener -> STATE_CONNECTING");
				} else if (state == BluetoothState.STATE_LISTEN) {
					Log.e(TAG, "BluetoothStateListener -> STATE_LISTEN");
				} else if (state == BluetoothState.STATE_NONE) {
					Log.e(TAG, "BluetoothStateListener -> STATE_NONE");
				}
			}
		});
	}
}