package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Bluetooth extends AppCompatActivity {
	private final static int REQUEST_ENABLE_BT = 1; //TODO figure out what this number means..
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Menu mMenu;

	public void bt_Check(View v){
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isEnabled()) {
				Snackbar.make(v, "BT is ON, now what?", Snackbar.LENGTH_LONG).show();
			} else {
				Snackbar.make(v, "Bluetooth is currently disabled", Snackbar.LENGTH_LONG)
						.setAction("ENABLE BLUETOOTH", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Snackbar.make(v, "Enabling bluetooth", Snackbar.LENGTH_LONG).show();
								Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
								startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
							}
						}).show();
			}
		} else {
			Snackbar.make(v, "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
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

	public void bt_Disable(View v){
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

	public void changeBluetoothIcon(Boolean btOn){
		MenuItem item = mMenu.findItem(R.id.tb_bluetooth);
		if (mMenu != null){
			if(btOn){
				item.setIcon(R.drawable.ic_bluetooth_disabled);
			}
			else{
				item.setIcon(R.drawable.ic_bluetooth);
			}
		}
	}
}
