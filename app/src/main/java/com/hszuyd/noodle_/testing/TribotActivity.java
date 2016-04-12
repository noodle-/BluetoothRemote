package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.io.Console;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import app.akexorcist.bluetoothspp.library.BluetoothSPP;
//import app.akexorcist.bluetoothspp.library.BluetoothState;


public class TribotActivity extends AppCompatActivity {
	private static final String TAG = "TriBotActivity";
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	private TextView textView;
	private MenuItem mDynamicMenuIcon;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothDevice device = null;
	private Connect connectThisShit;


	// Message types sent from the BluetoothReadService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

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

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "onCreate: BluetoothAdapter is null");
			finish();
		} else {
			Log.e(TAG, "onCreate: " + mBluetoothAdapter.toString());
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
				// Get the device MAC address
				String address = data.getExtras().getString("EXTRA_DEVICE_ADDRESS");

				// Get the BLuetoothDevice object
				device = mBluetoothAdapter.getRemoteDevice(address);
				Log.e(TAG, "onActivityResult: " + device.toString());
				//createRfcommSocket(device);

				connectThisShit = new Connect();
				connectThisShit.connect(device);

				textView = (TextView) findViewById(R.id.TV_MAC_address);
				textView.setText("Device address: " + address);// Make sure the request was successful
			}
		}
	}

	public static BluetoothSocket createRfcommSocket(BluetoothDevice device) {
		BluetoothSocket tmp = null;
		try {
			Class class1 = device.getClass();
			Class aclass[] = new Class[1];
			aclass[0] = Integer.TYPE;
			Method method = class1.getMethod("createRfcommSocket", aclass);
			Object aobj[] = new Object[1];
			aobj[0] = Integer.valueOf(1);

			tmp = (BluetoothSocket) method.invoke(device, aobj);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			Log.e(TAG, "createRfcommSocket() failed", e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Log.e(TAG, "createRfcommSocket() failed", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			Log.e(TAG, "createRfcommSocket() failed", e);
		}
		return tmp;
	}
}