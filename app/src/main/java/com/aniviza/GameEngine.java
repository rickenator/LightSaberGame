package com.aniviza.lightsword;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private static final String TAG = "GameEngine";

    /**
     * Callback interface for visual combat effects.
     * Implementations must dispatch any view operations to the main thread.
     */
    public interface VisualEffectListener {
        /**
         * Called when a sword clash is detected.
         *
         * @param hitStrength raw hit-strength value from {@link SwordDevice#getHitStrength()};
         *                    0 means no data available, positive values indicate hit intensity.
         */
        void onClashFlash(float hitStrength);
    }

    private List<SwordDevice> connectedDevices;
    private boolean isGameRunning;
    private int gameScore;
    private int roundNumber;
    private long gameStartTime;
    private VisualEffectListener visualEffectListener;
    
    public GameEngine() {
        this.connectedDevices = new ArrayList<>();
        this.isGameRunning = false;
        this.gameScore = 0;
        this.roundNumber = 1;
        this.gameStartTime = System.currentTimeMillis();
    }
    
    public void startGame() {
        isGameRunning = true;
        gameStartTime = System.currentTimeMillis();
        Log.d(TAG, "Game started");
    }
    
    public void stopGame() {
        isGameRunning = false;
        Log.d(TAG, "Game stopped");
    }
    
    public void addDevice(SwordDevice device) {
        if (!connectedDevices.contains(device)) {
            connectedDevices.add(device);
            Log.d(TAG, "Added device: " + device.toString());
        }
    }
    
    public void removeDevice(SwordDevice device) {
        connectedDevices.remove(device);
        Log.d(TAG, "Removed device: " + device.toString());
    }
    
    public List<SwordDevice> getConnectedDevices() {
        return connectedDevices;
    }

    /**
     * Register a listener to receive visual-effect callbacks (e.g. clash flash).
     * Pass {@code null} to remove the listener.
     */
    public void setVisualEffectListener(VisualEffectListener listener) {
        this.visualEffectListener = listener;
    }

    public void processDeviceData(SwordDevice device) {
        // Process data from a device and determine actions
        if (device.isThrusting()) {
            Log.d(TAG, "Device " + device.getDeviceId() + " is thrusting");
            // Play thrust sound on all devices
            // SoundManager.getInstance().playThrustSound();
            handleSwordAction(device, SwordDevice.ACTION_THRUST);
        }
        
        if (device.isClashing()) {
            Log.d(TAG, "Device " + device.getDeviceId() + " is clashing");
            // Notify visual-effect listener so the UI can flash the screen.
            if (visualEffectListener != null) {
                visualEffectListener.onClashFlash(device.getHitStrength());
            }
            // Play clash sound on all devices
            // SoundManager.getInstance().playClashSound();
            handleSwordAction(device, SwordDevice.ACTION_PARRY);
        }
        
        if (device.isWaving()) {
            Log.d(TAG, "Device " + device.getDeviceId() + " is waving");
            // Play wave sound on all devices
            // SoundManager.getInstance().playWaveSound();
            handleSwordAction(device, SwordDevice.ACTION_SLASH);
        }
        
        // Handle blocking
        if (device.isBlocking()) {
            Log.d(TAG, "Device " + device.getDeviceId() + " is blocking");
            handleSwordAction(device, SwordDevice.ACTION_BLOCK);
        }
        
        // Handle attacking
        if (device.isAttacking()) {
            Log.d(TAG, "Device " + device.getDeviceId() + " is attacking");
            handleSwordAction(device, SwordDevice.ACTION_THRUST);
        }
        
        // Handle defending
        if (device.isDefending()) {
            Log.d(TAG, "Device " + device.getDeviceId() + " is defending");
            handleSwordAction(device, SwordDevice.ACTION_BLOCK);
        }
    }
    
    private void handleSwordAction(SwordDevice device, int actionType) {
        // Handle different sword fighting actions
        switch (actionType) {
            case SwordDevice.ACTION_THRUST:
                // Handle thrust action
                device.incrementCombo();
                gameScore += 10;
                Log.d(TAG, "Thrust action detected. Score: " + gameScore);
                break;
                
            case SwordDevice.ACTION_SLASH:
                // Handle slash action
                device.incrementCombo();
                gameScore += 8;
                Log.d(TAG, "Slash action detected. Score: " + gameScore);
                break;
                
            case SwordDevice.ACTION_BLOCK:
                // Handle block action
                gameScore += 5;
                Log.d(TAG, "Block action detected. Score: " + gameScore);
                break;
                
            case SwordDevice.ACTION_PARRY:
                // Handle parry action
                gameScore += 15;
                Log.d(TAG, "Parry action detected. Score: " + gameScore);
                break;
                
            case SwordDevice.ACTION_COMBO:
                // Handle combo action
                device.incrementCombo();
                gameScore += 20;
                Log.d(TAG, "Combo action detected. Score: " + gameScore);
                break;
                
            case SwordDevice.ACTION_SPECIAL:
                // Handle special action
                gameScore += 25;
                Log.d(TAG, "Special action detected. Score: " + gameScore);
                break;
                
            default:
                Log.d(TAG, "Unknown action type: " + actionType);
                break;
        }
        
        // Check for combo completion
        if (device.getComboCount() >= 3) {
            Log.d(TAG, "Combo completed! Device " + device.getDeviceId() + " scored extra points");
            gameScore += 30;
            device.resetCombo();
        }
    }
    
    public void synchronizeGameplay() {
        // Synchronize gameplay across all connected devices
        if (!isGameRunning) return;
        
        Log.d(TAG, "Synchronizing gameplay across " + connectedDevices.size() + " devices");
        
        // In a real implementation, this would:
        // 1. Send synchronized game state
        // 2. Coordinate timing between devices
        // 3. Handle multiplayer interactions
    }
    
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    public int getGameScore() {
        return gameScore;
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
    
    public void incrementRound() {
        roundNumber++;
    }
    
    public long getGameDuration() {
        if (isGameRunning) {
            return System.currentTimeMillis() - gameStartTime;
        }
        return 0;
    }
    
    public void resetGame() {
        this.gameScore = 0;
        this.roundNumber = 1;
        this.gameStartTime = System.currentTimeMillis();
        this.connectedDevices.clear();
    }
}
