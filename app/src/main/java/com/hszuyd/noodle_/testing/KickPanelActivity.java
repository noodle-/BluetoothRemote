package com.hszuyd.noodle_.testing; // TODO change package name


import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class KickPanelActivity extends AppCompatActivity {
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Bluetooth bluetooth = new Bluetooth();

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
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show(); //Action that should be run when the snackbar!? is pressed
			}
		});
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public void button_bt_check_OnClick(View v) {
		//bluetooth.bt_Check(v);
	}

	/*public void button_search_OnClick(View v) {
		bluetooth.bt_Search(v);
	}*/

	public void button_bt_disable_OnClick(View v) {
		mBluetoothAdapter.disable();
		Snackbar.make(this.findViewById(android.R.id.content), "Bluetooth is disabled", Snackbar.LENGTH_LONG).show();
	}

}

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
// TODO Create bluetoothsocket
//BluetoothSocket MySocket = createRfcommSocketToServiceRecord(UUID);


//private final static int REQUEST_ENABLE_BT = 1; //TODO figure out what this number means..
	/*public void bt_Check(View v) {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isEnabled()) {
				Snackbar.make(v, "BT is ON, now what?", Snackbar.LENGTH_LONG).show();
			} else {
				Snackbar.make(v, "Bluetooth is currently disabled", Snackbar.LENGTH_LONG)
						.setAction("ENABLE BLUETOOTH", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Snackbar.make(v, "Enabling bluetooth", Snackbar.LENGTH_LONG).show();
								/*Intent enableBtIntent = new Intent(that, KickPanelActivity.class);
								startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
								mBluetoothAdapter.enable();
							}
						}).show();
			}
		} else {
			Snackbar.make(v, "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
		}
	}

	public void tb_Check(MenuItem item) {
		if ((mBluetoothAdapter != null) && (!mBluetoothAdapter.isEnabled())) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else{
				Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
				Snackbar.make(v, "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
		}
	}

	public void tb_Check2(MenuItem item) {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isEnabled()) {
				Toast.makeText(Bluetooth.this, "Bluetooth is on!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Bluetooth.this, "Bluetooth is currently disabled", Toast.LENGTH_SHORT).show();
				mBluetoothAdapter.enable();
				/*Snackbar.make(v, "Bluetooth is currently disabled", Snackbar.LENGTH_LONG)
						.setAction("ENABLE BLUETOOTH", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Snackbar.make(v, "Enabling bluetooth", Snackbar.LENGTH_LONG).show();
								/*Intent enableBtIntent = new Intent(that, KickPanelActivity.class);
								startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
								mBluetoothAdapter.enable();
							}
						}).show();
			}
		} else {
			Toast.makeText(Bluetooth.this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
			/*Snackbar.make(v, "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
		}
	}

	public void bt_Search(View v){
		if (mBluetoothAdapter == null) {
			Snackbar.make(v, "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
		} else {
			// Making this device discoverable through Bluetooth for 300 seconds, this automatically enables bluetooth. TODO Learn what the hell I'm doing here
			Intent discoverableIntent = new
					Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);

		}
	}

	public void bt_Disable(View v) {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.disable();
				Snackbar.make(v, "Disabling Bluetooth", Snackbar.LENGTH_LONG).show();
			} else {
				Snackbar.make(v, "Bluetooth was not enabled", Snackbar.LENGTH_LONG).show();
			}
		} else {
			Snackbar.make(v, "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
		}
	}

	private Menu mMenu;
	boolean isOn = false;
	public void button_changeBluetoothIcon_OnClick() {
		MenuItem item = mMenu.findItem(R.id.tb_bluetooth);
		if (mMenu != null) {
			if (isOn = false) {
				item.setIcon(R.drawable.ic_bluetooth_disabled);
				isOn = true;
			} else {
				item.setIcon(R.drawable.ic_bluetooth);
				isOn = false;
			}
		}
	}*/