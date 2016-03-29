package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class TribotActivity extends AppCompatActivity {

	//private final static int REQUEST_ENABLE_BT = 1; //TODO figure out what this number means..
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	//Bluetooth bluetooth = new Bluetooth();
	boolean isOn = false;
	MenuItem mDynamicMenuIcon;
	//TODO fix the icon changing, often won't work properly.
	int iconID = R.drawable.ic_bluetooth;
	private Menu mMenu;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		mDynamicMenuIcon = menu.findItem(R.id.tb_bluetooth);
		if(mBluetoothAdapter.isEnabled()){
			mDynamicMenuIcon.setIcon(R.drawable.ic_bluetooth_disabled);
		}
		else{
			mDynamicMenuIcon.setIcon(R.drawable.ic_bluetooth);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.tb_bluetooth) {
			//bluetooth.tb_OnOff();
			if (iconID == R.drawable.ic_bluetooth_disabled && mBluetoothAdapter.isEnabled()) {
				item.setIcon(R.drawable.ic_bluetooth);
				iconID = R.drawable.ic_bluetooth;
			} else {
				item.setIcon(R.drawable.ic_bluetooth_disabled);
				iconID = R.drawable.ic_bluetooth_disabled;
				//startActivity(new Intent(this, Bluetooth.class));
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/*public void tb_BT_OnOff(MenuItem item) {
		bluetooth.tb_OnOff();
		if(mBluetoothAdapter != null){
			if(!mBluetoothAdapter.isEnabled()){
				mBluetoothAdapter.enable();
				Snackbar.make(this.findViewById(android.R.id.content), "Enabling Bluetooth", Snackbar.LENGTH_LONG).show();
			}
			else if(mBluetoothAdapter.isEnabled()){
				mBluetoothAdapter.disable();
				Snackbar.make(this.findViewById(android.R.id.content), "Bluetooth is disabled", Snackbar.LENGTH_LONG).show();
			}
		}
		//This is here for test purposes only.
		else{
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
	}*/

	/*public void button_changeBluetoothIcon_OnClick() {
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
}