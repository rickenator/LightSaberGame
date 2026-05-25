package com.aniviza.lightsaber;

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

public class MainActivity extends AppCompatActivity {

    private Button btnConnect;
    private Button btnStartGame;
    private TextView tvStatus;
    private TextView tvDevices;

    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupBluetooth();
        setupClickListeners();
    }

    private void initializeViews() {
        btnConnect = findViewById(R.id.btnConnect);
        btnStartGame = findViewById(R.id.btnStartGame);
        tvStatus = findViewById(R.id.tvStatus);
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
                // Start the game logic
                Toast.makeText(MainActivity.this, "Game starting...", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
