package com.hszuyd.noodle_.testing;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
	private String tv_MAC_address;
	// ID for views
	private MenuItem mDynamicMenuIcon;
	private TextView tvMacAddress;
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
		tvMacAddress = (TextView) findViewById(R.id.TV_MAC_address);
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
		tv_MAC_address = getString(R.string.tribot_TV_MAC_address);
	}

	/**
	 * Creates the optionmenu with a MenuItem & sets the icon.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		mDynamicMenuIcon = menu.findItem(R.id.tb_bluetooth);
		mDynamicMenuIcon.setIcon(R.drawable.ic_favorite_white);
		return true;
	}

	/**
	 * Action when clicked on the icon/button on the toolbar.
	 */
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

	/**
	 * Starts the deviceListActivity.
	 */
	public void button_start_device_list(View v) {
		startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_DEVICE_ADDRESS);
	}

	/**
	 * Generates a number between 0 and 1.
	 */
	public void button_Random_Math_Number(View v) {
		tvMacAddress = (TextView) findViewById(R.id.TV_random_number);
		String rndmstring = String.valueOf(Math.random());
		tvMacAddress.setText(rndmstring);
	}

	/**
	 * Sends the name entered in the Loginscreen to iPlay.
	 */
	public void button_Send_Name(View v) {
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
	public void button_Choose_Rounds(View v) {
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
	public void button_Send_Rounds(View v) {
		if (roundsNumber != null) {
			connectBluetooth.write(roundsNumber);
			buttonSendRounds.setVisibility(View.GONE);
			tvMacAddress.setText(tv_MAC_address);
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
				Log.d(TAG, "onActivityResult: " + device.toString());

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
				assert tvMacAddress != null;
				tvMacAddress.setText("Device address: " + address);
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
