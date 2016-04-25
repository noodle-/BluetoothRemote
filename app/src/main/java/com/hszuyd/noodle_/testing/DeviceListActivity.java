package com.hszuyd.noodle_.testing;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
	General g = new General(DeviceListActivity.this);                       // Load the General class
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
				if (mProgressBar != null) {                                                         // Make sure the item exists
					mProgressBar.setVisibility(View.GONE);                                          // Indicate indeterminate progress has stopped
				}
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
			supportFinishAfterTransition();
		}
	};

	/**
	 * The on-click listener for all devices in the PairedDevice list which will delete the pair.
	 */
	private AdapterView.OnItemLongClickListener mDeviceHoldListener = new AdapterView.OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> av, View v, int arg2, long arg3) {
			mBtAdapter.cancelDiscovery();   // Cancel discovery because it's costly

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      // Sets the BluetoothAdapter to the default adapter
			try {
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);        // Gets the remote device from the address we just got
				switch (device.getBondState()) {
					case BluetoothDevice.BOND_NONE:
						g.pairDevice(device);
						g.showToast("Pairing");
					case BluetoothDevice.BOND_BONDED:
						g.unpairDevice(device);
						g.showToast("Un-pairing");
				}

				// Restarts the activity so the paired/unpaired devices are added/removed from the view
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

		// Initialize the button to perform device discovery
		if (scanButton != null) {       // Make sure the item exist
			scanButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					doDiscovery();
				}
			});
		}

		// Initialize array adapters. One for already paired devices and one for newly discovered devices
		ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

		// Find and set up the ListView for paired devices

		if (pairedListView != null) {   // Make sure the item exists
			pairedListView.setAdapter(pairedDevicesArrayAdapter);
			pairedListView.setOnItemClickListener(mDeviceClickListener);
			pairedListView.setOnItemLongClickListener(mDeviceHoldListener);
		}

		// Find and set up the ListView for newly discovered devices
		if (newDevicesListView != null) {   // Make sure the item exists
			newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
			newDevicesListView.setOnItemClickListener(mDeviceClickListener);
			newDevicesListView.setOnItemLongClickListener(mDeviceHoldListener);
		}

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

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);                // Load toolbar (with title, icon etc)
		setSupportActionBar(mToolbar);                                          // Cast toolbar as actionbar
		getSupportActionBar().setDisplayShowHomeEnabled(true);                  // Show home/back button
		if (mToolbar != null) {
			mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);    // Set back button icon
			mToolbar.setNavigationOnClickListener(new View.OnClickListener() {      // Initialize the onclick listener to navigate back
				@Override
				public void onClick(View v) {
					supportFinishAfterTransition();                                 // Finish with animation
				}
			});
		}

		// Handle runtime permissions for ACCESS_COARSE_LOCATION permission to find nearby bluetooth devices
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
			switch (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
				case PackageManager.PERMISSION_DENIED:
					((TextView) new AlertDialog.Builder(this)
							.setTitle("Runtime Permissions up ahead")
							.setMessage(Html.fromHtml("<p>To find nearby bluetooth devices please click \"Allow\" on the runtime permissions popup.</p>" +
									"<p>For more info see <a href=\"http://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id\">here</a>.</p>"))
							.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
										ActivityCompat.requestPermissions(DeviceListActivity.this,
												new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
												REQUEST_ACCESS_COARSE_LOCATION);
									}
								}
							})
							.show()
							.findViewById(android.R.id.message))
							.setMovementMethod(LinkMovementMethod.getInstance());       // Make the link clickable. Needs to be called after show(), in order to generate hyperlinks
					break;
				case PackageManager.PERMISSION_GRANTED:
					break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		supportFinishAfterTransition();     // Use the transition while going back
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
		if (mProgressBar != null) {                     // Make sure the item exists
			mProgressBar.setVisibility(View.VISIBLE);   // Indicate indeterminate progress
		}

		mNewDevicesArrayAdapter.clear();            // Remove all previously found devices
		findViewById(R.id.subtitle_new_devices).setVisibility(View.VISIBLE);   // Stop hiding the subtitle for discovered devices
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();           // Stop it because it's very resource intensive
		}
		mBtAdapter.startDiscovery();                // Request discover from BluetoothAdapter
	}
}
