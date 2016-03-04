package com.hszuyd.noodle_.testing; // TODO change package name


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class KickPanelActivity extends AppCompatActivity {
	private Bluetooth bluetooth = new Bluetooth();

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

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public void button_bt_check_OnClick(View v) {
		bluetooth.bt_Check(v);
	}

	/*public void button_search_OnClick(View v) {
		bluetooth.bt_Search(v);
	}*/

	public void button_bt_disable_OnClick(View v) {
		bluetooth.bt_Disable(v);
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
// TODO Create bluetoothsocket
//BluetoothSocket MySocket = createRfcommSocketToServiceRecord(UUID);