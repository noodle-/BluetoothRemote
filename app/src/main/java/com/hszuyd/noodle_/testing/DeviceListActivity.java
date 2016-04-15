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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Set;

/**
 * This Activity appears as a dialog. It lists any paired devices and devices detected in the area after discovery.
 * When a device is chosen by the user, the MAC address of the device is sent back to the parent Activity in the result Intent.
 */
public class DeviceListActivity extends AppCompatActivity {
	private static final int REQUEST_ACCESS_COARSE_LOCATION = 1;            // Request code for ACCESS_COARSE_LOCATION permission
	private static final String TAG = "DevicelistActivity";                 // TAG for debug messages
	private BluetoothAdapter mBtAdapter;                                    // Member fields
	private ArrayAdapter<String> mNewDevicesArrayAdapter;                   // Newly discovered devices

	/**
	 * The BroadcastReceiver that listens for discovered devices and changes the title when discovery is finished.
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();                                                     // Store intent in the string "action"
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {                                      // When discovery finds a device
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);   // Get the BluetoothDevice object from the Intent
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {                         // If it's already paired, skip it, because it's been listed already
					//if (Arrays.asList(mNewDevicesArrayAdapter).contains(device)) {                // Does not work because it's an array of strings, not an array of values.
					//  Log.e(TAG, "Discovered duplicate device: " + device);                       // TODO find a way to check an array whether a string is present
					mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {                 // When discovery is finished
				ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressbar);            // Load progressbar
				assert mProgressBar != null;
				mProgressBar.setVisibility(View.GONE);                                              // Indicate indeterminate progress has stopped
			}
		}
	};

	private Intent starterIntent; // Gets the Activity as Intent

	/**
	 * The on-click listener for all devices in the ListViews which sends back the MAC address through an intent.
	 */
	private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			mBtAdapter.cancelDiscovery();   // Cancel discovery because it's costly and we're about to connect

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			// Create the result Intent and include the MAC address
			Intent data = new Intent();
			data.putExtra("EXTRA_DEVICE_ADDRESS", address);

			// Set result and finish/close this Activity
			setResult(Activity.RESULT_OK, data);
			finish();
		}
	};

	/**
	 * The on-click listener for all devices in the PairedDevice list which will delete the pair.
	 */
	private AdapterView.OnItemLongClickListener mDeviceHoldListener = new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> av, View v, int arg2, long arg3) {
			mBtAdapter.cancelDiscovery();   // Cancel discovery because it's costly and we're about to connect

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			// Sets the BluetoothAdapter to the default adapter
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			try {
				// Gets the remote device from the address we just got
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				Log.d(TAG, "onItemLongClick: " + device);

				// Gets the method to unpair a device from Connect()
				Connect connectBluetooth = new Connect();
				connectBluetooth.unpairDevice(device);

				// Restarts the activity so the paired device is gone from the view
				finish();
				startActivity(starterIntent);
			} catch (Exception e) {
				Log.e(TAG, "onItemLongClick: " + e.toString());
			}
			return true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		starterIntent = getIntent();                    // Set starterIntent to this activity
		setContentView(R.layout.activity_device_list);  // This pretty much loads all resource ID's etc
		setResult(Activity.RESULT_CANCELED);            // Set result CANCELED in case the user backs out

		// Find the views with their ID
		Button scanButton = (Button) findViewById(R.id.button_scan);
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);

		// Make sure the views are not null
		assert scanButton != null;
		assert pairedListView != null;
		assert newDevicesListView != null;

		// Initialize the button to perform device discovery
		scanButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
			}
		});

		// Initialize array adapters. One for already paired devices and one for newly discovered devices
		ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

		// Find and set up the ListView for paired devices
		pairedListView.setAdapter(pairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);
		pairedListView.setOnItemLongClickListener(mDeviceHoldListener);


		// Find and set up the ListView for newly discovered devices
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);


		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();                  // Get the local Bluetooth adapter
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices(); // Get a set of currently paired devices

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.subtitle_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired).toString();
			pairedDevicesArrayAdapter.add(noDevices);
		}

		// Load the toolbar so we can set the title
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);  // Set toolbar as actionbar?

		// Request coarse location permission to access the hardware identifiers
		// See http://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
			// TODO Add popup explaining why we need ACCESS_COARSE_LOCATION
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
						REQUEST_ACCESS_COARSE_LOCATION);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();       // Make sure we're not doing discovery anymore
		}
		this.unregisterReceiver(mReceiver);     // Unregister broadcast listeners
	}

	private void doDiscovery() {
		ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressbar);    // Load progressbar
		assert mProgressBar != null;
		mProgressBar.setVisibility(View.VISIBLE);   // Indicate indeterminate progress

		mNewDevicesArrayAdapter.clear();            // Remove all previously found devices
		findViewById(R.id.subtitle_new_devices).setVisibility(View.VISIBLE);   // Stop hiding the subtitle for discovered devices
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();           // Stop it because it's very resource intensive
		}
		mBtAdapter.startDiscovery();                // Request discover from BluetoothAdapter
	}
}
