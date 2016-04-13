package com.hszuyd.noodle_.testing;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TribotActivity extends AppCompatActivity {
	private static final String TAG = "TriBotActivity";
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	private TextView textView;
	private MenuItem mDynamicMenuIcon;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothDevice device = null;
	private Connect connectThisShit = new Connect();
	private String name;
	private CharSequence rounds[];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribot);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Intent intentName = getIntent();
		Bundle nameBundle = intentName.getExtras();
		name = (String) nameBundle.get("NAME_PLAYER");

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "onCreate: BluetoothAdapter is null");
			finish();
		} else {
			Log.e(TAG, "onCreate: " + mBluetoothAdapter.toString());
		}

		Spinner dropdown = (Spinner)findViewById(R.id.spinner_rounds);
		String[] items = new String[]{"1", "2", "3"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
		if (dropdown != null) {
			dropdown.setAdapter(adapter);
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

	public void button_Send_Message(View v){
		connectThisShit.write(name);
		connectThisShit.write("5");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DEVICE_ADDRESS) {        // Check which request we're responding to. When doing more requests a switch case is probably a nicer way of doing this.
			if (resultCode == RESULT_OK) {                  // Make sure the request was successful

				// Get the device MAC address
				String address = data.getExtras().getString("EXTRA_DEVICE_ADDRESS");

				// Get the BLuetoothDevice object
				device = mBluetoothAdapter.getRemoteDevice(address);
				Log.e(TAG, "onActivityResult: " + device.toString());

				connectThisShit.connect(device);


				textView = (TextView) findViewById(R.id.TV_MAC_address);
				assert textView != null;
				textView.setText("Device address: " + address);// Make sure the request was successful by showing it in a textview
			}
		}
	}
}