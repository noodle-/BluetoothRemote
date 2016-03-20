package com.hszuyd.noodle_.testing;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class Bluetooth extends AppCompatActivity {

	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private ArrayAdapter<String> mPairedDevice;
	private ArrayAdapter<String> mNewDevice;

	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
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
					mNewDevice.add(device.getName() + "\n" + device.getAddress());
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (mNewDevice.getCount() == 0) {
					String noDevices = getResources().getText(R.string.none_found).toString();
					mNewDevice.add(noDevices);
				}
			}
		}
	};

	// The on-click listener for all devices in the ListViews
	private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			String noDevicesPaired = getResources().getText(R.string.none_paired).toString();
			String noDevicesFound = getResources().getText(R.string.none_found).toString();


			// Cancel discovery because it's costly and we're about to connect
			mBluetoothAdapter.cancelDiscovery();

			String info = ((TextView) v).getText().toString();

			if ((info != noDevicesPaired) && (info != noDevicesFound)) {

				if (info.length() >= 17) {
					// Get the device MAC address, which is the last 17 chars in the View
					String address = info.substring(info.length() - 17);

					// Create the result Intent and include the MAC address
					Intent intent = new Intent();
					intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

					// Set result and finish this Activity
					setResult(Activity.RESULT_OK, intent);
				} else {
					setResult(Activity.RESULT_CANCELED);
				}

				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Setup Window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		//Set result canceled if user back out
		setContentView(R.layout.activity_bluetooth);

		//Initialize discovery
		doDiscovery();

		//Initialize array adapters.
		mPairedDevice = new ArrayAdapter<>(this, R.layout.content_bluetooth); //OR device_name (not sure couldn't test yet)
		mNewDevice = new ArrayAdapter<>(this, R.layout.content_bluetooth); //OR device_name (not sure couldn't test yet)

		//Setup paired devices in a List
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevice);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		//Setup new devices in a List
		ListView newDeviceListView = (ListView) findViewById(R.id.new_devices);
		newDeviceListView.setAdapter(mNewDevice);
		newDeviceListView.setOnItemClickListener(mDeviceClickListener);

		//Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		//Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		//Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

		//Add each paired device to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevice.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired).toString();
			mPairedDevice.add(noDevices);
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
	}

	//Starts the discovery
	private void doDiscovery() {
		// Indicate scanning in the title
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);

		// Turn on sub-title for new devices
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// If we're already discovering, stop it
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}

		// Request discover from BluetoothAdapter
		mBluetoothAdapter.startDiscovery();
	}

	public void tb_OnOff() {
		if (mBluetoothAdapter != null) {
			if (!mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.enable();
			} else {
				mBluetoothAdapter.disable();
			}
		}
	}

}
