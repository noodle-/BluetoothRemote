package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TribotActivity extends AppCompatActivity {

	private final static int REQUEST_ENABLE_BT = 1; //TODO figure out what this number means..
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Menu mMenu;
	private boolean bluetoothOn = true;
	private int Whilerun = 1;

	/*Runnable myRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				while (Whilerun == 1) {
					Thread.sleep(500); // Waits for 1 second (1000 milliseconds)
					(new Runnable() {
						@Override
						public void run() {
							changeBluetoothIcon();
						}
					});
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// code for stopping current task so thread stops
			}
		}
	};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribot);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		/*new Thread(new Runnable() {
			public void run() {

				mImageView.post(new Runnable() {
					public void run() {
						mImageView.setImageBitmap(bitmap);
					}
				});
			}
		}).start();*/
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.tb_bluetooth) {
			tb_BT_OnOff(item);
		}
		return super.onOptionsItemSelected(item);
	}

	public void tb_BT_OnOff(MenuItem item) {
		//Enable Bluetooth
		if ((mBluetoothAdapter != null) && (!mBluetoothAdapter.isEnabled())) {
			mBluetoothAdapter.enable();
			Snackbar.make(this.findViewById(android.R.id.content), "Enabling Bluetooth", Snackbar.LENGTH_LONG).show();
			//item.setIcon(R.drawable.ic_bluetooth_disabled);
		}
		//Disable Bluetooth
		else if (mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
			Snackbar.make(this.findViewById(android.R.id.content), "Bluetooth is disabled", Snackbar.LENGTH_LONG).show();
			//item.setIcon(R.drawable.ic_bluetooth);
		}
		//Device does not support bluetooth
		else {
			Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
			//Snackbar.make(this.findViewById(android.R.id.content), "Device does not support Bluetooth", Snackbar.LENGTH_LONG).show();
		}
	}

	public void changeBluetoothIcon() {
		MenuItem item = mMenu.findItem(R.id.tb_bluetooth);
		if (mMenu != null) {
			if (bluetoothOn) {
				item.setIcon(R.drawable.ic_bluetooth_disabled);
				bluetoothOn = false;
			} else {
				item.setIcon(R.drawable.ic_bluetooth);
				bluetoothOn = true;
			}
		}
	}
}