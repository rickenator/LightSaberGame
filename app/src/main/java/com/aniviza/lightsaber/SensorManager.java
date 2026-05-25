package com.aniviza.lightsaber;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorManager implements SensorEventListener {
    private static final String TAG = "SensorManager";
    
    private android.hardware.SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Sensor accelerometerSensor;
    
    // Sensor data variables
    private float[] gyroValues = new float[3];
    private float[] accelValues = new float[3];
    
    // Game-related variables
    private boolean isThrusting = false;
    private boolean isClashing = false;
    private boolean isWaving = false;
    
    // Sensitivity thresholds
    private static final float THRUST_THRESHOLD = 5.0f;
    private static final float CLASH_THRESHOLD = 8.0f;
    private static final float WAVE_THRESHOLD = 3.0f;
    
    public SensorManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        if (gyroscopeSensor == null) {
            Log.w(TAG, "Gyroscope sensor not available");
        }
        
        if (accelerometerSensor == null) {
            Log.w(TAG, "Accelerometer sensor not available");
        }
    }
    
    public void startSensing() {
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }
    
    public void stopSensing() {
        sensorManager.unregisterListener(this);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            System.arraycopy(event.values, 0, gyroValues, 0, 3);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelValues, 0, 3);
        }
        
        // Process sensor data to detect lightsaber actions
        detectActions();
    }
    
    private void detectActions() {
        // Calculate total acceleration
        float totalAcceleration = (float) Math.sqrt(
            accelValues[0] * accelValues[0] + 
            accelValues[1] * accelValues[1] + 
            accelValues[2] * accelValues[2]
        );
        
        // Calculate angular velocity from gyroscope
        float angularVelocity = (float) Math.sqrt(
            gyroValues[0] * gyroValues[0] + 
            gyroValues[1] * gyroValues[1] + 
            gyroValues[2] * gyroValues[2]
        );
        
        // Detect thrust (sharp movement)
        if (totalAcceleration > THRUST_THRESHOLD && angularVelocity > 2.0f) {
            isThrusting = true;
            Log.d(TAG, "Thrust detected!");
        } else {
            isThrusting = false;
        }
        
        // Detect clash (sudden impact)
        if (totalAcceleration > CLASH_THRESHOLD) {
            isClashing = true;
            Log.d(TAG, "Clash detected!");
        } else {
            isClashing = false;
        }
        
        // Detect wave (gentle movement)
        if (angularVelocity > WAVE_THRESHOLD && totalAcceleration < 3.0f) {
            isWaving = true;
            Log.d(TAG, "Wave detected!");
        } else {
            isWaving = false;
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this implementation
    }
    
    // Getters for detected actions
    public boolean isThrusting() {
        return isThrusting;
    }
    
    public boolean isClashing() {
        return isClashing;
    }
    
    public boolean isWaving() {
        return isWaving;
    }
    
    // Get current sensor values
    public float[] getGyroValues() {
        return gyroValues;
    }
    
    public float[] getAccelValues() {
        return accelValues;
    }
}
