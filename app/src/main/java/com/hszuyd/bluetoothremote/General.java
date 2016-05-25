package com.hszuyd.bluetoothremote;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;

public class General {
	final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Context mContext;   // Context from activity which call this class

	public General(Context context) {
		mContext = context;
	}

	public void showToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}

	public void showSnackbar(View view, String message) {
		Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();   // This may overlap the FAB button!
	}

	/**
	 * Show different dialogs based on Bluetooth adapter availability, show none if there's a bluetooth adapter and it's enabled
	 */
	public void checkBluetooth() {
		if (mBluetoothAdapter == null || mBluetoothAdapter.getAddress().equals(null)) {
			new AlertDialog.Builder(mContext)
					.setTitle("Warning")
					.setMessage("Bluetooth adapter not found. \n" +
							"\n" +
							"Please run this app on a different device instead.")
					.setPositiveButton(
							"Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									System.exit(0);
								}
							})
					.setCancelable(false)
					.setIcon(R.drawable.ic_warning)
					.show();
		} else if (!mBluetoothAdapter.isEnabled()) {
			new AlertDialog.Builder(mContext)
					.setTitle("Warning")
					.setMessage("Bluetooth is currently disabled. \n" +
							"\n" +
							"Please click 'Enable Bluetooth' to continue using this app")
					.setPositiveButton(
							"Enable Bluetooth",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									mBluetoothAdapter.enable();
								}
							}
					)
					.setNegativeButton("Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									System.exit(0);
								}
							})
					.setCancelable(false)
					.setIcon(R.drawable.ic_warning)
					.show();
		}
	}

	/**
	 * Initiates pairing of a BluetoothDevice using reflection
	 */
	public void pairDevice(BluetoothDevice device) {
		try {
			Method method = device.getClass().getMethod("createBond", (Class[]) null);
			method.invoke(device, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initiates un-pairing of a BluetoothDevice using reflection
	 */
	public void unpairDevice(BluetoothDevice device) {
		try {
			Method method = device.getClass().getMethod("removeBond", (Class[]) null);
			method.invoke(device, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/* TODO Use this for multiple menu actions
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

/* TODO Try to pair using reflection method
Log.e(TAG, "Pairing");
try {
	Method m = device.getClass().getMethod("createBond", (Class[]) null);
	m.invoke(device, (Object[]) null);
} catch (Exception e) {
	e.printStackTrace();
}*/

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

/* TODO Enable bluetooth with intent
private final static int REQUEST_ENABLE_BT = 1;
Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
startActivityForResult(enableIntent, REQUEST_ENABLE_BT);    */

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
}*/
