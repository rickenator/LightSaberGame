package com.aniviza.arcblade;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameSurfaceView extends GLSurfaceView {

    public GameSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new GameRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
