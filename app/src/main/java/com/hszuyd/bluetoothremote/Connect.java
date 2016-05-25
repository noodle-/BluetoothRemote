package com.hszuyd.bluetoothremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class Connect {
	// Debugging
	private static final String TAG = "BluetoothReadService";
	private static final boolean D = true;
	// Static UUID
	private static final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// Member fields
	private final BluetoothAdapter mAdapter;
	private BluetoothSocket mmSocket;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private boolean mAllowInsecureConnections;

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 */
	public Connect() {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mAllowInsecureConnections = true;
	}

	/**
	 * Getter. Returns the mmSocket to TribotActivity.
	 */
	public BluetoothSocket getMmSocket() {
		return this.mmSocket;
	}

	/**
	 * Unpairs the device in DeviceListActivity
	 */
	public void unpairDevice(BluetoothDevice device) {
		try {
			Method method = device.getClass().getMethod("removeBond", (Class[]) null);
			method.invoke(device, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cancels any thread running and attempting a connection.
	 */
	public synchronized void start() {
		if (D) Log.d(TAG, "start");

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 *
	 * @param device The BluetoothDevice to connect
	 */
	public synchronized void connect(BluetoothDevice device) {
		if (D) Log.d(TAG, "connect to: " + device);

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 *
	 * @param socket The BluetoothSocket on which the connection was made
	 * @param device The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
		if (D) Log.d(TAG, "connected");

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 *
	 * @param out The Strings to write
	 */
	public void write(String out) {
		// Create temporary object
		ConnectedThread r;
		r = mConnectedThread;
		r.write(out.getBytes());
		String readed = out;
		Log.d(TAG, "write: " + readed);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		Log.e(TAG, "connectionLost: Connection failed");
		try {
			mmSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Connect.this.start();
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		Log.e(TAG, "connectionLost: Connection lost");
		try {
			mmSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Connect.this.start();
	}

	/**
	 * This thread runs while attempting to make an outgoing connection
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the given BluetoothDevice
			try {
				if (mAllowInsecureConnections) {
					Method method;

					method = device.getClass().getMethod("createRfcommSocket", int.class);
					tmp = (BluetoothSocket) method.invoke(device, 1);
				} else {
					tmp = device.createRfcommSocketToServiceRecord(SerialPortServiceClass_UUID);
				}
			} catch (Exception e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run() {
			Log.d(TAG, "Begin mConnectThread");
			setName("ConnectThread");

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery();

			// Make a connection to the BluetoothSocket
			try {
				// This is a blocking call and will only return on a successful connection or an exception
				mmSocket.connect();
			} catch (IOException e) {
				connectionFailed();
				// Close the socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG, "unable to close() socket during connection failure", e2);
				}
				// Start the service over to restart listening mode
				Connect.this.start();
				return;
			}

			// Reset the ConnectThread because we're done
			synchronized (Connect.this) {
				mConnectThread = null;
			}
			// Start the connected thread
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * This thread runs during a connection with a remote device.
	 * It handles all incoming and outgoing transmissions.
	 */
	private class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			Log.d(TAG, "Begin mConnectedThread");
			byte[] buffer = new byte[1024];
			int bytes;

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					String readed = new String(buffer, 0, bytes);

					// Send the obtained bytes to the log
					Log.d(TAG, "read: " + readed);


					//TODO put a check in here that will close the connection

				} catch (IOException e) {
					Log.e(TAG, "disconnected: bluetooth socket closed", e);
					connectionLost();
					break;
				}
			}
		}

		/**
		 * Write to the connected OutStream.
		 *
		 * @param buffer The bytes to write
		 */
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}
