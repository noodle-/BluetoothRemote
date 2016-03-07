package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class TribotActivity extends AppCompatActivity {

	private final static int REQUEST_ENABLE_BT = 1; //TODO figure out what this number means..
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Bluetooth bluetooth = new Bluetooth();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribot);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		/*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});*/
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
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			//Disable Bluetooth
		} else if (mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
			Snackbar.make(this.findViewById(android.R.id.content), "Bluetooth is disabled", Snackbar.LENGTH_LONG).show();
		} else {
			Snackbar.make(this.findViewById(android.R.id.content), "Device does not support Bluetooth OR YOU HAF FUKKET UP", Snackbar.LENGTH_LONG).show();
		}
	}
}
