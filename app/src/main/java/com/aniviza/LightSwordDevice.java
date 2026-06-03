package com.aniviza.lightsword;

import android.bluetooth.BluetoothDevice;

public class LightSwordDevice {
    private BluetoothDevice device;
    private String deviceId;
    private boolean isConnected;
    private float[] gyroData;
    private float[] accelData;
    private boolean isThrusting;
    private boolean isClashing;
    private boolean isWaving;
    
    public LightSwordDevice(BluetoothDevice device) {
        this.device = device;
        this.deviceId = device.getAddress();
        this.gyroData = new float[3];
        this.accelData = new float[3];
        this.isConnected = false;
    }
    
    // Getters and setters
    public BluetoothDevice getDevice() {
        return device;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public void setConnected(boolean connected) {
        isConnected = connected;
    }
    
    public float[] getGyroData() {
        return gyroData;
    }
    
    public void setGyroData(float[] gyroData) {
        this.gyroData = gyroData;
    }
    
    public float[] getAccelData() {
        return accelData;
    }
    
    public void setAccelData(float[] accelData) {
        this.accelData = accelData;
    }
    
    public boolean isThrusting() {
        return isThrusting;
    }
    
    public void setThrusting(boolean thrusting) {
        isThrusting = thrusting;
    }
    
    public boolean isClashing() {
        return isClashing;
    }
    
    public void setClashing(boolean clashing) {
        isClashing = clashing;
    }
    
    public boolean isWaving() {
        return isWaving;
    }
    
    public void setWaving(boolean waving) {
        isWaving = waving;
    }
    
    @Override
    public String toString() {
        return "LightSwordDevice{" +
                "deviceName='" + (device != null ? device.getName() : "Unknown") + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }
}
