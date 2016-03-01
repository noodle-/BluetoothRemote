package com.hszuyd.noodle_.testing; // TODO change package name

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	//private final static int REQUEST_ENABLE_BT = 1; //TODO figure out what this number means..
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  //TODO dunno whether it's okay to do this right here?

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);  //TODO No idea what this does...
		//setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) //This is a weird popup near the bottom of the screen
						.setAction("Action", null).show();      //Action that should be run when the snackbar? is pressed
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	// do something when the button is clicked
	public void button_nuke_OnClick(View v) {
		Toast.makeText(this, "BLEEP BLOOP", Toast.LENGTH_LONG).show();
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isEnabled()) {
				Snackbar.make(v, "BT is ON, now what?", Snackbar.LENGTH_LONG).show();
			} else {
				Snackbar.make(v, "Bluetooth currently disabled, click here to enable it", Snackbar.LENGTH_LONG).show();
				//.setAction("Action", null).show();      //Turn bluetooth on
			}
		} else {
			Snackbar.make(v, "Device does not support Bluetooth...", Snackbar.LENGTH_LONG).show();
		}
	}

	public void button_search_OnClick(View v) {
		//BluetoothSocket MySocket = createRfcommSocketToServiceRecord(UUID);
		if (mBluetoothAdapter == null) {
			Snackbar.make(v, "Device does not support Bluetooth...", Snackbar.LENGTH_LONG).show();
		} else {
			// Making this device discoverable through Bluetooth for 300 seconds, this automatically enables bluetooth. TODO Learn wtf I'm doing here
			Intent discoverableIntent = new
					Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	public void button_bt_disable_OnClick(View v) {
		Snackbar.make(v, "Disabling bluetooth", Snackbar.LENGTH_LONG).show(); //TODO remove this, it's just for debugging.
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.disable();
				Snackbar.make(v, "Bluetooth has been disabled", Snackbar.LENGTH_LONG).show();
			} else {
				Snackbar.make(v, "Bluetooth was not enabled", Snackbar.LENGTH_LONG).show();
			}
		} else {
			Snackbar.make(v, "Device does not support Bluetooth...", Snackbar.LENGTH_LONG).show();
		}
	}
}

// TODO split app into two interfaces BlockRemote & RobotRemote
// TODO I think I don't need this..
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//
//		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
// TODO Use this to check whether Bluetooth is enabled
//	if (!mBluetoothAdapter.isEnabled()) {
//		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//		startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//	}

