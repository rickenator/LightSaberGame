package com.aniviza.lightsaber;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;

public class SoundManager {
    private static final String TAG = "SoundManager";
    
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundMap;
    private Context context;
    
    // Sound IDs
    public static final int SOUND_THRUST = 1;
    public static final int SOUND_CLASH = 2;
    public static final int SOUND_WAVE = 3;
    public static final int SOUND_HAH = 4;
    public static final int SOUND_HO = 5;
    public static final int SOUND_PERRY = 6;
    public static final int SOUND_DUCK = 7;
    
    public SoundManager(Context context) {
        this.context = context;
        this.soundMap = new HashMap<>();
        
        // Initialize SoundPool (deprecated but functional for this use case)
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
        
        // Load sounds - in a real app, you would load actual sound files
        // This is a placeholder for sound loading
        Log.d(TAG, "SoundManager initialized");
    }
    
    public void loadSounds() {
        // In a real implementation, you would load sound files here:
        // soundMap.put(SOUND_THRUST, soundPool.load(context, R.raw.thrust, 1));
        // soundMap.put(SOUND_CLASH, soundPool.load(context, R.raw.clash, 1));
        // etc.
        
        Log.d(TAG, "Sounds loaded (placeholder)");
    }
    
    public void playSound(int soundId) {
        if (soundPool != null && soundMap.containsKey(soundId)) {
            soundPool.play(soundMap.get(soundId), 1.0f, 1.0f, 1, 0, 1.0f);
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
    
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
