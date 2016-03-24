package com.hszuyd.noodle_.testing; // TODO change package name?

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

public class KickPanelActivity extends AppCompatActivity {
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
		// Show a different icon based on random int
		if (Math.random() > 0.5) { // TODO This should probably replaced with "!= 0" or something similar instead, because we have no idea what the output of Math.random() is.
			mDynamicMenuIcon.setIcon(R.drawable.ic_thumb_up);
		} else {
			mDynamicMenuIcon.setIcon(R.drawable.ic_thumb_down);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();                   // Restart activity
		startActivity(getIntent()); // Restart activity
		return super.onOptionsItemSelected(item);   // Why?
	}


	public void button_test_A(View v) {
		Toast.makeText(this, "This is a Toast", Toast.LENGTH_SHORT).show();
	}

	public void button_test_B(View v) {
		Snackbar.make(v, "This is a Snackbar", Snackbar.LENGTH_LONG).show();
	}

	public void button_start_device_list(View v) {
		startActivity(new Intent(this, DeviceListActivity.class));
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
