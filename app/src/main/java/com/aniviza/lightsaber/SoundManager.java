package com.aniviza.lightsaber;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final String TAG = "SoundManager";
    
    private SoundPool soundPool;
    private Map<Integer, Integer> soundMap;
    private Context context;
    
    // Sound IDs
    public static final int SOUND_THRUST = 1;
    public static final int SOUND_CLASH = 2;
    public static final int SOUND_WAVE = 3;
    public static final int SOUND_HAH = 4;
    public static final int SOUND_HO = 5;
    public static final int SOUND_PERRY = 6;
    public static final int SOUND_DUCK = 7;
    
    // Flag for using live generation vs pre-recorded sounds
    private boolean useLiveGeneration = false;
    
    public SoundManager(Context context) {
        this.context = context;
        this.soundMap = new HashMap<>();
        
        // Initialize SoundPool
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(10, android.media.AudioManager.STREAM_MUSIC, 0);
        }
        
        Log.d(TAG, "SoundManager initialized");
    }
    
    public void loadSounds() {
        // Load pre-recorded sounds from resources
        // In a real implementation, you would load actual sound files from res/raw/
        // For now, we'll simulate the loading process
        
        Log.d(TAG, "Loading pre-recorded sounds...");
        // soundMap.put(SOUND_THRUST, soundPool.load(context, R.raw.thrust, 1));
        // soundMap.put(SOUND_CLASH, soundPool.load(context, R.raw.clash, 1));
        // soundMap.put(SOUND_WAVE, soundPool.load(context, R.raw.wave, 1));
        // soundMap.put(SOUND_HAH, soundPool.load(context, R.raw.hah, 1));
        // soundMap.put(SOUND_HO, soundPool.load(context, R.raw.ho, 1));
        // soundMap.put(SOUND_PERRY, soundPool.load(context, R.raw.perry, 1));
        // soundMap.put(SOUND_DUCK, soundPool.load(context, R.raw.duck, 1));
        
        Log.d(TAG, "Pre-recorded sounds loaded");
    }
    
    public void loadLiveGeneratedSounds() {
        // Enable live sound generation as fallback
        useLiveGeneration = true;
        Log.d(TAG, "Live sound generation enabled");
    }
    
    public void playSound(int soundId) {
        if (soundPool != null) {
            if (soundMap.containsKey(soundId)) {
                // Play pre-recorded sound if available
                soundPool.play(soundMap.get(soundId), 1.0f, 1.0f, 1, 0, 1.0f);
            } else if (useLiveGeneration) {
                // Generate sound live if no pre-recorded version
                generateAndPlaySound(soundId);
            } else {
                Log.w(TAG, "No sound found for ID: " + soundId);
            }
        }
    }
    
    public void playThrustSound() {
        playSound(SOUND_THRUST);
    }
    
    public void playClashSound() {
        playSound(SOUND_CLASH);
    }
    
    public void playWaveSound() {
        playSound(SOUND_WAVE);
    }
    
    public void playHahSound() {
        playSound(SOUND_HAH);
    }
    
    public void playHoSound() {
        playSound(SOUND_HO);
    }
    
    public void playPerrySound() {
        playSound(SOUND_PERRY);
    }
    
    public void playDuckSound() {
        playSound(SOUND_DUCK);
    }
    
    private void generateAndPlaySound(int soundId) {
        // Generate sound programmatically based on sound ID
        Log.d(TAG, "Generating sound live: " + soundId);
        // In a real implementation, this would use audio synthesis techniques
        // to generate the appropriate sound
        switch(soundId) {
            case SOUND_THRUST:
                // Generate thrust sound
                Log.d(TAG, "Generating thrust sound live");
                break;
            case SOUND_CLASH:
                // Generate clash sound
                Log.d(TAG, "Generating clash sound live");
                break;
            case SOUND_WAVE:
                // Generate wave sound
                Log.d(TAG, "Generating wave sound live");
                break;
            case SOUND_HAH:
                // Generate hah sound
                Log.d(TAG, "Generating hah sound live");
                break;
            case SOUND_HO:
                // Generate ho sound
                Log.d(TAG, "Generating ho sound live");
                break;
            case SOUND_PERRY:
                // Generate perry sound
                Log.d(TAG, "Generating perry sound live");
                break;
            case SOUND_DUCK:
                // Generate duck sound
                Log.d(TAG, "Generating duck sound live");
                break;
        }
    }
    
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
    
    public boolean isUseLiveGeneration() {
        return useLiveGeneration;
    }
    
    public void setUseLiveGeneration(boolean useLiveGeneration) {
        this.useLiveGeneration = useLiveGeneration;
    }
}
