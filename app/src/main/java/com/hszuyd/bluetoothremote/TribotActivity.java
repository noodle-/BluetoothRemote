package com.hszuyd.bluetoothremote;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

public class TribotActivity extends AppCompatActivity {
	// Tag for the log
	private static final String TAG = "TriBotActivity";
	// Used for onActivityForResult
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	// Sets the bluetoothadapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Gets the classes
	private Connect connectBluetooth = new Connect();
	private General g = new General(TribotActivity.this);
	// Sets an intent
	private Intent restartIntent;
	// Strings
	private String name;
	private String roundsNumber = "1";
	private String macAddress;
	// ID for views
	private MenuItem mDynamicMenuIcon;
	private TextView textviewMacAddress;
	private Button buttonSendRounds;
	private Button buttonSelectRounds;
	private Button buttonSendName;
	private ProgressBar mProgressBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribot);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Gets the Intent from Loginscreen/MainActivity
		Intent intentName = getIntent();
		Bundle nameBundle = intentName.getExtras();
		name = (String) nameBundle.get("NAME_PLAYER");
		if (Objects.equals(name, "") || Objects.equals(name, null)) {
			name = "Unknown";
			Log.d(TAG, "onCreate: " + name);
		}

		// Sets the bluetoothAdapter to the default adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "onCreate: BluetoothAdapter is null");
			finish();
		} else {
			Log.d(TAG, "onCreate: " + mBluetoothAdapter.toString());
		}

		// Gets the Activity as Intent
		restartIntent = getIntent();

		// Find the views with their ID
		buttonSendRounds = (Button) findViewById(R.id.button_send_rounds);
		buttonSendName = (Button) findViewById(R.id.button_send_name);
		buttonSelectRounds = (Button) findViewById(R.id.button_choose_rounds);
		textviewMacAddress = (TextView) findViewById(R.id.TV_MAC_address);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

		// Make sure the views are not null
		assert buttonSendRounds != null;
		assert buttonSendName != null;
		assert buttonSelectRounds != null;
		assert mProgressBar != null;

		// Set the visibility of the views to GONE
		buttonSendName.setVisibility(View.GONE);
		buttonSendRounds.setVisibility(View.GONE);
		buttonSelectRounds.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);

		// Set the string to the resource
		macAddress = getString(R.string.tribot_TV_MAC_address);
	}

	/**
	 * Starts the deviceListActivity.
	 */
	public void buttonStartDeviceList(View v) {
		startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_DEVICE_ADDRESS);
	}

	/**
	 * Sends the name entered in the Loginscreen to iPlay.
	 */
	public void buttonSendName(View v) {
		if (name != null) {
			connectBluetooth.write(name);
			buttonSelectRounds.setVisibility(View.VISIBLE);
			buttonSendName.setVisibility(View.GONE);
		} else {
			g.showToast("Please enter a username!");
		}
	}

	/**
	 * Pops up an AlertDialog so you can choose a number of rounds.
	 */
	public void buttonChooseRounds(View v) {
		final Dialog d = new Dialog(TribotActivity.this);
		d.setContentView(R.layout.dialog);
		Button b1 = (Button) d.findViewById(R.id.button1);
		final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
		np.setMaxValue(20);  // max value 100
		np.setMinValue(1);   // min value 0
		np.setWrapSelectorWheel(false);
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				roundsNumber = String.valueOf(np.getValue());
				buttonSendRounds.setVisibility(View.VISIBLE);
				buttonSelectRounds.setVisibility(View.GONE);
				d.dismiss();
			}
		});
		d.show();
	}

	/**
	 * Sends the number of rounds that want to be played to iPlay.
	 */
	public void buttonSendRounds(View v) {
		if (roundsNumber != null) {
			connectBluetooth.write(roundsNumber);
			buttonSendRounds.setVisibility(View.GONE);
			textviewMacAddress.setText(macAddress);
			finish();
			startActivity(restartIntent);
		} else {
			g.showToast("Please select a number of rounds!");
		}
	}

	/**
	 * Gets the result back from DeviceListActivity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Set progressbar to indicate loading
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);   // Indicate indeterminate progress
		}
		if (requestCode == REQUEST_DEVICE_ADDRESS) {        // Check which request we're responding to. When doing more requests a switch case is probably a nicer way of doing this.
			if (resultCode == RESULT_OK) {                  // Make sure the request was successful
				// Get the device MAC address
				String address = data.getExtras().getString("EXTRA_DEVICE_ADDRESS");

				// Get the BluetoothDevice object
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

				// Creates a handler that waits for 6 seconds and shows a progressbar. Shows the button to send the name after 6 seconds.
				// TODO create a thread or anything that automatically checks if the device is connected
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						assert mProgressBar != null;
						mProgressBar.setVisibility(View.GONE);
						buttonSendName.setVisibility(View.VISIBLE);
					}
				}, 6000);
				connectBluetooth.connect(device);

				// Show that the request was successful by putting it in a textview
				assert textviewMacAddress!= null;
				textviewMacAddress.setText("Device address: " + address);
			}
		}
	}

	/**
	 * Make sure that the socket gets closed if pressed on the back button
	 */
	public void onBackPressed() {
		BluetoothSocket mmSocket = connectBluetooth.getMmSocket();
		try {
			if (mmSocket != null) {
				mmSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finish();
	}
}
