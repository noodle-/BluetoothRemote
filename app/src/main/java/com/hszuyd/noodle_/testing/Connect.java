package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by remco on 12-4-2016.
 */
public class Connect {
	private String TAG = "Connect ";
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket mmSocket;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;

	public synchronized void connect(BluetoothDevice device) {
		Log.e(TAG, "connect to: " + device);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
	}

	private class ConnectThread extends Thread {
		private String TAG = "Connect: ";
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;

			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server code TODO fix this comment
				Log.i(TAG, "Device Name: " + mmDevice.getName());
				Log.i(TAG, "Device UUID: " + mmDevice.getUuids()[0].getUuid());
				tmp = device.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());

			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
//			if (mBluetoothAdapter.isDiscovering()) {
//				mBluetoothAdapter.cancelDiscovery();
//			}

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
			} catch (IOException connectException) {
				Log.e(TAG, "run: " + connectException.toString());
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// Do work to manage the connection (in a separate thread)
			//manageConnectedSocket(mmSocket);
			mConnectedThread = new ConnectedThread(mmSocket);
			mConnectedThread.start();
		}

		/**
		 * Will cancel an in-progress connection, and close the socket
		 */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) { }

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];  // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					// Send the obtained bytes to the UI activity
					//mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
					//		.sendToTarget();
				} catch (IOException e) {
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) { }
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
	}
}
