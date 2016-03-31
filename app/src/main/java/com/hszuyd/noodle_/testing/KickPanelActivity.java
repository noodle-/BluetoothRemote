package com.hszuyd.noodle_.testing; // TODO change package name?

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.UUID;

public class KickPanelActivity extends AppCompatActivity {
	private static final UUID MY_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
	private static final int REQUEST_DEVICE_ADDRESS = 1;
	// Inflate the menu; this adds items to the action bar if it is present.
	MenuItem mDynamicMenuIcon;

	// Draw all stuff when loading the activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kickpanel);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Replace this with something useful
				Snackbar.make(view, "This is an example", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show(); //Action that should be run when the snackbar is pressed
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		mDynamicMenuIcon = menu.findItem(R.id.tb_bluetooth);
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
		return super.onOptionsItemSelected(item);   // Why?
	}


	public void button_test_A(View v) {
		Toast.makeText(this, "This is a Toast", Toast.LENGTH_SHORT).show();
	}

	public void button_test_B(View v) {
		Snackbar.make(v, "This is a Snackbar", Snackbar.LENGTH_LONG).show();
	}

	public void button_start_device_list(View v) {
		startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_DEVICE_ADDRESS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_DEVICE_ADDRESS) {        // Check which request we're responding to. When doing more requests a switch case is probably a nicer way of doing this.
			if (resultCode == RESULT_OK) {                  // Make sure the request was successful
				if (data.hasExtra("EXTRA_DEVICE_ADDRESS")) {
					Bundle bundleResult = data.getExtras(); // Store the Intent data(=device address) that we've received from the DeviceListActivity in a bundle. The bundle consists of "EXTRA_DEVICE_ADDRESS, MAC_ADDRESS"
					String device = bundleResult.getString("EXTRA_DEVICE_ADDRESS");
					BluetoothSocket mSocket = null;

					Toast.makeText(getApplicationContext(), "Device address: " + device, Toast.LENGTH_SHORT).show();    //TODO Remove this when we've successfully sent through the address
				} else {
					Toast.makeText(getApplicationContext(), "Failed to get MAC address from ", Toast.LENGTH_SHORT).show();    //TODO Remove this when we've successfully sent through the address

					//connect to device using the data we've just received !!!
				/*				try {
					mSocket = device.createInsecureRfcommSocketToServiceRecord (MY_UUID);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					mSocket.connect();
				} catch (IOException e) {
					try {
						mSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}*/
				}
			}
		}
	}
}

/* TODO I think I don't need this..
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	//noinspection SimplifiableIfStatement
	if (id == R.id.action_settings) {
		return true;
	}
	return super.onOptionsItemSelected(item);
}   */

/* TODO Create bluetoothsocket
BluetoothSocket MySocket = createRfcommSocketToServiceRecord(UUID); */

/* TODO Snackbar with custom button to enable bluetooth
private final static int REQUEST_ENABLE_BT = 1;
Snackbar.make(v,"Bluetooth is currently disabled",Snackbar.LENGTH_LONG)
		.setAction("ENABLE BLUETOOTH",new View.OnClickListener(){
		@Override
		public void onClick(View v){
			Snackbar.make(v,"Enabling bluetooth",Snackbar.LENGTH_LONG).show();
			Intent enableBtIntent=new Intent(that,KickPanelActivity.class);
			startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
			mBluetoothAdapter.enable();
		}
	}).show();  */

/* TODO Snackbar, from another view else
Snackbar.make(this.findViewById(android.R.id.content), "This is a Snackbar", Snackbar.LENGTH_LONG).show(); // Snackbar which can be called from the toolbar */

/* TODO Enable bluetooth with intent
private final static int REQUEST_ENABLE_BT = 1;
Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
startActivityForResult(enableIntent, REQUEST_ENABLE_BT);    */

/* TODO Enable bluetooth
mBluetoothAdapter.enable(); */

/* TODO Make discoverable with intent
public void bt_Search(View v){
	if (mBluetoothAdapter == null) {
		Snackbar.make(v, "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
	} else {
		// Making this device discoverable through Bluetooth for 300 seconds, this automatically enables bluetooth.
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(discoverableIntent);
	}
}   */
