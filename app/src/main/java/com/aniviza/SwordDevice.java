package com.aniviza.lightsword;

import android.bluetooth.BluetoothDevice;

public class SwordDevice {
    private BluetoothDevice device;
    private String deviceId;
    private boolean isConnected;
    private float[] gyroData;
    private float[] accelData;
    private boolean isThrusting;
    private boolean isClashing;
    private boolean isWaving;
    private boolean isBlocking;
    private boolean isParrying;
    private float swordAngle;
    private float swordVelocity;
    private long lastUpdate;
    private float[] lastGyroData;
    private float[] lastAccelData;
    private boolean isAttacking;
    private boolean isDefending;
    private float hitStrength;
    private int comboCount;

    // Sword fighting action types
    public static final int ACTION_THRUST = 1;
    public static final int ACTION_SLASH = 2;
    public static final int ACTION_BLOCK = 3;
    public static final int ACTION_PARRY = 4;
    public static final int ACTION_COMBO = 5;
    public static final int ACTION_SPECIAL = 6;

    // Sword fighting states
    public static final int STATE_IDLE = 0;
    public static final int STATE_MOVING = 1;
    public static final int STATE_ATTACKING = 2;
    public static final int STATE_DEFENDING = 3;
    public static final int STATE_BLOCKING = 4;
    public static final int STATE_COMBO = 5;
    public static final int STATE_SPECIAL = 6;
    
    private int currentState = STATE_IDLE;
    private int lastAction = 0;

    public SwordDevice(BluetoothDevice device) {
        this.device = device;
        this.deviceId = device.getAddress();
        this.gyroData = new float[3];
        this.accelData = new float[3];
        this.lastGyroData = new float[3];
        this.lastAccelData = new float[3];
        this.isConnected = false;
        this.lastUpdate = System.currentTimeMillis();
        this.swordAngle = 0.0f;
        this.swordVelocity = 0.0f;
        this.hitStrength = 0.0f;
        this.comboCount = 0;
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
        this.lastGyroData = gyroData.clone();
        calculateSwordVelocity();
        detectSwordAction();
    }
    
    public float[] getAccelData() {
        return accelData;
    }
    
    public void setAccelData(float[] accelData) {
        this.accelData = accelData;
        this.lastAccelData = accelData.clone();
        calculateSwordVelocity();
        detectSwordAction();
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

    // New methods for sword fighting mechanics
    public boolean isBlocking() {
        return isBlocking;
    }

    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    public boolean isParrying() {
        return isParrying;
    }

    public void setParrying(boolean parrying) {
        isParrying = parrying;
    }

    public float getSwordAngle() {
        return swordAngle;
    }

    public void setSwordAngle(float swordAngle) {
        this.swordAngle = swordAngle;
    }

    public float getSwordVelocity() {
        return swordVelocity;
    }

    public void setSwordVelocity(float swordVelocity) {
        this.swordVelocity = swordVelocity;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int state) {
        this.currentState = state;
    }

    public int getLastAction() {
        return lastAction;
    }

    public void setLastAction(int action) {
        this.lastAction = action;
    }

    public float getHitStrength() {
        return hitStrength;
    }

    public void setHitStrength(float hitStrength) {
        this.hitStrength = hitStrength;
    }

    public int getComboCount() {
        return comboCount;
    }

    public void setComboCount(int comboCount) {
        this.comboCount = comboCount;
    }

    public void incrementCombo() {
        this.comboCount++;
    }

    public void resetCombo() {
        this.comboCount = 0;
    }

    // Methods for detecting sword actions from sensor data
    private void calculateSwordVelocity() {
        // Calculate velocity based on acceleration and time delta
        long now = System.currentTimeMillis();
        long deltaTime = now - lastUpdate;
        if (deltaTime > 0) {
            // Simple calculation: velocity = acceleration * time
            float velocity = 0.0f;
            for (int i = 0; i < 3; i++) {
                velocity += Math.abs(accelData[i] - lastAccelData[i]) * deltaTime / 1000.0f;
            }
            this.swordVelocity = velocity / 3.0f; // Average velocity across all axes
        }
        this.lastUpdate = now;
    }

    private void detectSwordAction() {
        // Detect different sword fighting actions based on sensor data
        if (isAttacking) return; // Already attacking

        // Check for thrust (high acceleration in one direction)
        if (accelData[0] > 5.0f || accelData[1] > 5.0f || accelData[2] > 5.0f) {
            // Check if it's a strong thrust
            float maxAccel = Math.max(Math.max(Math.abs(accelData[0]), Math.abs(accelData[1])), Math.abs(accelData[2]));
            if (maxAccel > 10.0f) {
                this.isThrusting = true;
                this.currentState = STATE_ATTACKING;
                this.lastAction = ACTION_THRUST;
            }
        }

        // Check for slash (angular velocity)
        if (gyroData[0] > 2.0f || gyroData[1] > 2.0f || gyroData[2] > 2.0f) {
            float maxGyro = Math.max(Math.max(Math.abs(gyroData[0]), Math.abs(gyroData[1])), Math.abs(gyroData[2]));
            if (maxGyro > 5.0f) {
                this.isWaving = true;
                this.currentState = STATE_MOVING;
                this.lastAction = ACTION_SLASH;
            }
        }

        // Check for blocking (low acceleration)
        if (Math.abs(accelData[0]) < 1.0f && Math.abs(accelData[1]) < 1.0f && Math.abs(accelData[2]) < 1.0f) {
            this.isBlocking = true;
            this.currentState = STATE_BLOCKING;
            this.lastAction = ACTION_BLOCK;
        }
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
        if (attacking) {
            this.currentState = STATE_ATTACKING;
        } else {
            this.currentState = STATE_IDLE;
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setDefending(boolean defending) {
        isDefending = defending;
        if (defending) {
            this.currentState = STATE_DEFENDING;
        } else {
            this.currentState = STATE_IDLE;
        }
    }

    public boolean isDefending() {
        return isDefending;
    }
    
    @Override
    public String toString() {
        return "SwordDevice{" +
                "deviceName='" + (device != null ? device.getName() : "Unknown") + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }
}
