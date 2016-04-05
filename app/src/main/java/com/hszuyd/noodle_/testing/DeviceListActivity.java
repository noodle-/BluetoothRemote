package com.hszuyd.noodle_.testing;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends Activity {
	private BluetoothAdapter mBtAdapter;                            // Member fields
	private ArrayAdapter<String> mNewDevicesArrayAdapter;           // Newly discovered devices

	/**
	 * The BroadcastReceiver that listens for discovered devices and changes the title when discovery is finished
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { // When discovery is finished
//				setProgressBarIndeterminateVisibility(false);
				Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
				mToolbar.setTitle(R.string.select_device);
				if (mNewDevicesArrayAdapter.getCount() == 0) {  // Show none_found if no devices are found
					String noDevices = getResources().getText(R.string.none_found).toString();
					mNewDevicesArrayAdapter.add(noDevices);
				}
			}
		}
	};

	/**
	 * The on-click listener for all devices in the ListViews
	 */
	private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			mBtAdapter.cancelDiscovery();

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			// Create the result Intent and include the MAC address
			Intent data = new Intent();
			data.putExtra("EXTRA_DEVICE_ADDRESS", address);

			// Set result and finish(=close?) this Activity
			setResult(RESULT_OK, data);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the window TODO No idea what this does
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_device_list);

		setResult(Activity.RESULT_CANCELED);    // Set result CANCELED in case the user backs out

		// Initialize the button to perform device discovery
		Button scanButton = (Button) findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);
			}
		});

		// Initialize array adapters. One for already paired devices and one for newly discovered devices
		ArrayAdapter<String> pairedDevicesArrayAdapter =
				new ArrayAdapter<String>(this, R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(pairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();  // Get the local Bluetooth adapter

		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices(); // Get a set of currently paired devices

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired).toString();
			pairedDevicesArrayAdapter.add(noDevices);
		}

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);    // Load the toolbar so we can set the title
//		setSupportActionBar(mToolbar); TODO WHY ARE WE DOING THIS EVERYWHERE ELSE???
		mToolbar.setTitle("Device list");

		// Request coarse location permission to access the hardware identifiers
		// See http://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
		int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
					MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();   // Make sure we're not doing discovery anymore
		}

		this.unregisterReceiver(mReceiver); // Unregister broadcast listeners
	}

	private void doDiscovery() {
		setProgressBarIndeterminateVisibility(true);    // TODO no idea what this does

		// Indicate scanning in the title
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(R.string.scanning);

		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);   // Stop hiding the subtitle for new devices
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();   // Stop it because it's very resource intensive
		}
		mBtAdapter.startDiscovery(); // Request discover from BluetoothAdapter
	}
}
