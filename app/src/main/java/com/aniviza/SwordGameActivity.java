package com.aniviza.lightsword;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class SwordGameActivity extends AppCompatActivity {

    private Button btnConnect;
    private Button btnStartGame;
    private Button btnStopGame;
    private Button btnToggleSound;
    private TextView tvStatus;
    private TextView tvActions;
    private TextView tvDevices;

    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver receiver;
    private GameEngine gameEngine;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupBluetooth();
        setupClickListeners();
        setupGameEngine();
        
        // Initialize sound manager
        soundManager = new SoundManager(this);
        soundManager.loadSounds();
        
        tvStatus.setText("Status: Ready to start game");
    }

    private void initializeViews() {
        btnConnect = findViewById(R.id.btnConnect);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnStopGame = findViewById(R.id.btnStopGame);
        btnToggleSound = findViewById(R.id.btnToggleSound);
        tvStatus = findViewById(R.id.tvStatus);
        tvActions = findViewById(R.id.tvActions);
        tvDevices = findViewById(R.id.tvDevices);
    }

    private void setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show();
finish();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        // Register for broadcasts when a device is discovered
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Handle discovered device
                    tvDevices.setText(tvDevices.getText() + "\n" + device.getName() + " - " + device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    private void setupClickListeners() {
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverDevices();
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        btnStopGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });

        btnToggleSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSoundGeneration();
            }
        });
    }

    private void setupGameEngine() {
        gameEngine = new GameEngine();
    }

    private void discoverDevices() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        
        Toast.makeText(this, "Discovering devices...", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.startDiscovery();
        
        // Show paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            tvDevices.setText("Paired devices:\n");
            for (BluetoothDevice device : pairedDevices) {
                tvDevices.setText(tvDevices.getText() + device.getName() + " - " + device.getAddress() + "\n");
            }
        } else {
            tvDevices.setText("No paired devices found");
        }
    }

    private void startGame() {
        if (gameEngine != null) {
            gameEngine.startGame();
            tvStatus.setText("Status: Game started");
            tvActions.setText("Game is running - thrust, clash, and wave actions detected");
            Toast.makeText(this, "Game started", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopGame() {
        if (gameEngine != null) {
            gameEngine.stopGame();
            tvStatus.setText("Status: Game stopped");
            tvActions.setText("Actions will appear here");
            Toast.makeText(this, "Game stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleSoundGeneration() {
        // This would toggle between live sound generation and pre-recorded sounds
        // Implementation depends on actual SoundManager implementation
        tvStatus.setText("Status: Sound generation toggled");
        Toast.makeText(this, "Sound generation toggled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (soundManager != null) {
            soundManager.release();
        }
    }
}
