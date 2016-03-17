package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

public class Bluetooth extends AppCompatActivity {

	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private ArrayAdapter<String> mPairedDevice;
	private ArrayAdapter<String> mNewDevice;

	public void tb_OnOff() {
		if (mBluetoothAdapter != null) {
			if (!mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.enable();
				mBluetoothAdapter.startDiscovery();
				if (mBluetoothAdapter.isDiscovering()) {

				}
			} else {
				mBluetoothAdapter.disable();
			}
		}
		//This is here for test purposes only.
		else {
			new AlertDialog.Builder(this)
					.setTitle("Not compatible")
					.setMessage("Your phone does not support Bluetooth")
					.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.exit(0);
						}
					})
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}


	}

}
