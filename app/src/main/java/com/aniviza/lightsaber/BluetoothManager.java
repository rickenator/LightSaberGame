package com.aniviza.lightsaber;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    private static final String TAG = "BluetoothManager";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private Handler handler;
    
    public BluetoothManager(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.handler = new Handler();
    }
    
    public boolean isBluetoothAvailable() {
        return bluetoothAdapter != null;
    }
    
    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }
    
    public void enableBluetooth() {
        if (!isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // This would need to be called from an Activity context
        }
    }
    
    public Set<BluetoothDevice> getPairedDevices() {
        return bluetoothAdapter.getBondedDevices();
    }
    
    public void startDiscovery() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }
    
    public BluetoothSocket createSocket(BluetoothDevice device) {
        try {
            return device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Error creating socket", e);
            return null;
        }
    }
    
    public void connectToDevice(BluetoothDevice device, BluetoothSocket socket) {
        // Cancel discovery to prevent connection delays
        bluetoothAdapter.cancelDiscovery();
        
        try {
            socket.connect();
            Log.d(TAG, "Connected to device: " + device.getName());
        } catch (IOException e) {
            Log.e(TAG, "Connection failed", e);
            try {
                socket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close socket", closeException);
            }
        }
    }
    
    public void sendData(BluetoothSocket socket, String data) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error sending data", e);
        }
    }
    
    public void receiveData(BluetoothSocket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            
            while (true) {
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    String received = new String(buffer, 0, bytes);
                    Log.d(TAG, "Received: " + received);
                    // Handle received data
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error receiving data", e);
        }
    }
}
