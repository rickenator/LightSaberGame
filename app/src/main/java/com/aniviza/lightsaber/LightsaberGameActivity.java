package com.aniviza.lightsaber;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class LightsaberGameActivity extends Activity implements SensorEventListener {
    private static final String TAG = "LightsaberGame";
    
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Sensor accelerometerSensor;
    
    // Sensor data
    private float[] gyroValues = new float[3];
    private float[] accelValues = new float[3];
    
    // Game state
    private boolean isGameRunning = false;
    private boolean isThrusting = false;
    private boolean isClashing = false;
    private boolean isWaving = false;
    
    // Sound management
    private SoundManager soundManager;
    
    // UI elements
    private Button btnStart;
    private Button btnStop;
    private Button btnConnect;
    private Button btnToggleSound;
    private TextView tvStatus;
    private TextView tvActions;
    private TextView tvDevices;
    
    // Bluetooth manager
    private BluetoothManager bluetoothManager;
    
    // Game engine
    private GameEngine gameEngine;
    
    // Sensor thresholds
    private static final float THRUST_THRESHOLD = 5.0f;
    private static final float CLASH_THRESHOLD = 8.0f;
    private static final float WAVE_THRESHOLD = 3.0f;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupSensorManager();
        setupSoundManager();
        setupBluetoothManager();
        setupGameEngine();
        setupClickListeners();
        
        Log.d(TAG, "Lightsaber Game initialized");
    }
    
    private void initializeViews() {
        btnStart = findViewById(R.id.btnStartGame);
        btnStop = findViewById(R.id.btnStopGame);
        btnConnect = findViewById(R.id.btnConnect);
        btnToggleSound = findViewById(R.id.btnToggleSound);
        tvStatus = findViewById(R.id.tvStatus);
        tvActions = findViewById(R.id.tvActions);
        tvDevices = findViewById(R.id.tvDevices);
    }
    
    private void setupSensorManager() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        if (gyroscopeSensor == null) {
            Log.w(TAG, "Gyroscope sensor not available");
            Toast.makeText(this, "Gyroscope not available", Toast.LENGTH_SHORT).show();
        }
        
        if (accelerometerSensor == null) {
            Log.w(TAG, "Accelerometer sensor not available");
            Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupSoundManager() {
        soundManager = new SoundManager(this);
        soundManager.loadSounds(); // Load pre-recorded sounds
        Log.d(TAG, "Sound manager initialized");
    }
    
    private void setupBluetoothManager() {
        bluetoothManager = new BluetoothManager(this);
        Log.d(TAG, "Bluetooth manager initialized");
    }
    
    private void setupGameEngine() {
        gameEngine = new GameEngine();
        Log.d(TAG, "Game engine initialized");
    }
    
    private void setupClickListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });
        
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDevices();
            }
        });
        
        btnToggleSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSoundGeneration();
            }
        });
    }
    
    public void startGame() {
        if (!isGameRunning) {
            isGameRunning = true;
            tvStatus.setText("Game: Running");
            
            // Start sensors
            if (gyroscopeSensor != null) {
                sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
            }
            
            if (accelerometerSensor != null) {
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
            }
            
            Log.d(TAG, "Game started");
        }
    }
    
    public void stopGame() {
        if (isGameRunning) {
            isGameRunning = false;
            tvStatus.setText("Game: Stopped");
            
            // Stop sensors
            sensorManager.unregisterListener(this);
            
            Log.d(TAG, "Game stopped");
        }
    }
    
    private void connectDevices() {
        if (bluetoothManager.isBluetoothEnabled()) {
            // In a real implementation, this would discover and connect to devices
            tvStatus.setText("Searching for devices...");
            bluetoothManager.startDiscovery();
            tvDevices.setText("Devices found: None");
        } else {
            Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void toggleSoundGeneration() {
        boolean useLive = !soundManager.isUseLiveGeneration();
        soundManager.setUseLiveGeneration(useLive);
        
        if (useLive) {
            tvStatus.setText("Sound: Live Generation");
            Toast.makeText(this, "Using live sound generation", Toast.LENGTH_SHORT).show();
        } else {
            tvStatus.setText("Sound: Pre-recorded");
            Toast.makeText(this, "Using pre-recorded sounds", Toast.LENGTH_SHORT).show();
        }
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
        
        // Send data to other devices (Bluetooth)
        if (isGameRunning && bluetoothManager.isBluetoothEnabled()) {
            sendDataToDevices();
        }
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
            tvActions.setText("Action: Thrust");
            soundManager.playThrustSound();
            Log.d(TAG, "Thrust detected!");
        } else {
            isThrusting = false;
        }
        
        // Detect clash (sudden impact)
        if (totalAcceleration > CLASH_THRESHOLD) {
            isClashing = true;
            tvActions.setText("Action: Clash");
            soundManager.playClashSound();
            Log.d(TAG, "Clash detected!");
        } else {
            isClashing = false;
        }
        
        // Detect wave (gentle movement)
        if (angularVelocity > WAVE_THRESHOLD && totalAcceleration < 3.0f) {
            isWaving = true;
            tvActions.setText("Action: Wave");
            soundManager.playWaveSound();
            Log.d(TAG, "Wave detected!");
        } else {
            isWaving = false;
        }
    }
    
    private void sendDataToDevices() {
        // In a real implementation, this would send sensor data to connected devices
        // For now, we'll just log it
        Log.d(TAG, "Sending data to devices - Gyro: " + gyroValues[0] + ", " + gyroValues[1] + ", " + gyroValues[2]);
        Log.d(TAG, "Sending data to devices - Accel: " + accelValues[0] + ", " + accelValues[1] + ", " + accelValues[2]);
        
        // Simulate sending data through Bluetooth
        if (bluetoothManager != null) {
            // This is where we'd actually send data through Bluetooth
            Log.d(TAG, "Sending data via Bluetooth (simulated)");
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release();
        }
        if (sensorManager != null && isGameRunning) {
            sensorManager.unregisterListener(this);
        }
    }
}
