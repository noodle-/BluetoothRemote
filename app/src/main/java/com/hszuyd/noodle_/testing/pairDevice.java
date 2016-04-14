package com.hszuyd.noodle_.testing;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.Method;

public class pairDevice extends Thread{
	public pairDevice(BluetoothDevice device){
		try {
			Method method = device.getClass().getMethod("createBond", (Class[]) null);
			method.invoke(device, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
