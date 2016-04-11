package com.hszuyd.noodle_.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetoothspp.library.BluetoothSPP;
import app.akexorcist.bluetoothspp.library.BluetoothState;


public class TribotActivity extends AppCompatActivity {
	private static final String TAG = "TriBotActivity";
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	private BluetoothSPP bt = new BluetoothSPP(TribotActivity.this);
	private TextView textView;
	private MenuItem mDynamicMenuIcon;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribot);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		textView = (TextView) findViewById(R.id.TV_MAC_address);
		Intent iin = getIntent();
		Bundle b = iin.getExtras();

		if (b != null) {
			String device_Address = (String) b.get("EXTRA_DEVICE_ADDRESS");
			textView.setText("Device: " + device_Address);
		}
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
		return super.onOptionsItemSelected(item);
	}

	public void button_start_device_list(View v) {
		startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_DEVICE_ADDRESS);
	}

	public void button_Random_Math_Number(View v) {
		textView = (TextView) findViewById(R.id.TV_random_number);
		String rndmstring = String.valueOf(Math.random());
		textView.setText(rndmstring);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DEVICE_ADDRESS) {        // Check which request we're responding to. When doing more requests a switch case is probably a nicer way of doing this.
			if (resultCode == RESULT_OK) {                  // Make sure the request was successful
				if (data.hasExtra("EXTRA_DEVICE_ADDRESS")) {
					Bundle bundleResult = data.getExtras(); // Store the Intent data(=device address) that we've received from the DeviceListActivity in a bundle. The bundle consists of "EXTRA_DEVICE_ADDRESS, MAC_ADDRESS"
					String device = bundleResult.getString("EXTRA_DEVICE_ADDRESS");

					textView = (TextView) findViewById(R.id.TV_MAC_address);
					textView.setText("Device address: " + device);// Make sure the request was successful

					Log.e(TAG, "SetupService()");
					bt.setupService();
					startBluetoothService();

					Log.e(TAG, "Connecting to " + device);
					Toast.makeText(getApplicationContext(), "Connecting to " + device, Toast.LENGTH_SHORT).show();    //TODO Remove this when we've successfully sent through the address
					bt.connect(device);
				} else {
					Toast.makeText(getApplicationContext(), "Failed to get MAC address from ", Toast.LENGTH_SHORT).show();    //TODO Remove this when we've successfully sent through the address
				}
			}
		}
	}

	private void startBluetoothService() {
		// TODO Find a way to do this automatically
		// For connection with android device
		bt.startService(BluetoothState.DEVICE_ANDROID);

		//For connection with any micro-controller which communication with bluetooth serial port profile module
		//bt.startService(BluetoothState.DEVICE_OTHER);

		bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
			public void onDataReceived(byte[] data, String message) {
				Log.e(TAG, "OnDataReceivedListener ->\ndata " + data + "\nmessage " + message);
			}
		});

		bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
			public void onDeviceConnected(String name, String address) {
				Log.e(TAG, "BluetoothConnectionListener -> onDeviceConnected" + "\nname: " + name + "\taddress " + address);
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