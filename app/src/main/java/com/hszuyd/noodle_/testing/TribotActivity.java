package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class TribotActivity extends AppCompatActivity {

    private static final UUID MY_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_DEVICE_ADDRESS = 1;
    private MenuItem mDynamicMenuIcon;
    private TextView textView;
    //private BluetoothDevice device;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tribot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView2);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String device_Address = (String) b.get("EXTRA_DEVICE_ADDRESS");
            textView.setText("Device: " + device_Address);
        }
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
        return super.onOptionsItemSelected(item);
    }

    public void button_start_device_list(View v) {
        startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_DEVICE_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DEVICE_ADDRESS) {        // Check which request we're responding to. When doing more requests a switch case is probably a nicer way of doing this.
            if (resultCode == RESULT_OK) {                  // Make sure the request was successful
                if (data.hasExtra("EXTRA_DEVICE_ADDRESS")) {
                    Bundle bundleResult = data.getExtras(); // Store the Intent data(=device address) that we've received from the DeviceListActivity TODO Figure out why we can't simply use "String device = data.getStringExtra("device");"
                    String device = bundleResult.getString("EXTRA_DEVICE_ADDRESS");

                    textView = (TextView) findViewById(R.id.textView2);
                    textView.setText("Device address: " + device);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to get MAC address", Toast.LENGTH_SHORT).show();    //TODO Remove this when we've successfully sent through the address
                }

                //connect to device using the data we've just received !!!
                /*BluetoothSocket mSocket = null;
                try {
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

            } else {
                Toast.makeText(getApplicationContext(), "Failed to get MAC address from ", Toast.LENGTH_SHORT).show();    //TODO Remove this when we've successfully sent through the address
            }
        }
    }

    public void button_Random_Math_Number(View v) {
        textView = (TextView) findViewById(R.id.textView);
        String rndmstring = String.valueOf(Math.random());
        textView.setText(rndmstring);
    }
}