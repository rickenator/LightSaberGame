package com.aniviza.lightsword;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Full-screen transparent overlay that renders two combat visual effects:
 * <ul>
 *   <li><b>Clash flash</b> – a brief bright screen flash triggered by sword clashes,
 *       scaled to hit strength. Call {@link #triggerFlash(float)} from the UI thread.</li>
 *   <li><b>Hum glow</b> – a soft edge bloom whose brightness tracks sword activity,
 *       smoothed via a low-pass filter. Drive it by calling {@link #setHumTarget(float)}
 *       with a normalised sword velocity, then {@link #tickHumGlow()} at ~30 fps.</li>
 * </ul>
 *
 * <p>All named constants below are tunable to adjust the visual feel.</p>
 */
public class CombatOverlayView extends View {

    // -------------------------------------------------------------------------
    // Tunable constants — clash flash
    // -------------------------------------------------------------------------

    /** Base colour of the clash flash (cyan-white). */
    static final int FLASH_COLOR = Color.argb(255, 210, 245, 255);

    /** Peak alpha (0–1) at maximum hit strength. */
    static final float FLASH_MAX_ALPHA = 0.85f;

    /** Minimum flash fade-out duration in milliseconds (weakest hit). */
    static final long FLASH_MIN_DURATION_MS = 100;

    /** Maximum flash fade-out duration in milliseconds (strongest hit). */
    static final long FLASH_MAX_DURATION_MS = 280;

    // -------------------------------------------------------------------------
    // Tunable constants — hum glow
    // -------------------------------------------------------------------------

    /** Colour of the hum glow edge bloom (blue-cyan). */
    static final int GLOW_COLOR = Color.argb(255, 60, 160, 255);

    /** Minimum glow alpha when the sword is idle (0–1). */
    static final float GLOW_MIN_ALPHA = 0.03f;

    /** Maximum glow alpha at peak sword velocity (0–1). */
    static final float GLOW_MAX_ALPHA = 0.30f;

    /** Low-pass smoothing factor applied each tick (0 = frozen, 1 = instant). */
    static final float HUM_SMOOTHING = 0.12f;

    /** Period of the idle breath pulse in milliseconds. */
    static final long HUM_IDLE_PULSE_PERIOD_MS = 1600;

    /** Amplitude of the idle breath pulse as a fraction of max glow. */
    static final float HUM_IDLE_PULSE_AMPLITUDE = 0.04f;

    // -------------------------------------------------------------------------
    // Internal state — no allocations inside onDraw/tickHumGlow
    // -------------------------------------------------------------------------

    private final Paint flashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint glowPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float flashAlpha  = 0f;
    private float humTarget   = 0f;
    private float humSmoothed = 0f;

    private ValueAnimator flashAnimator;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CombatOverlayView(Context context) {
        super(context);
        init();
    }

    public CombatOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CombatOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        flashPaint.setStyle(Paint.Style.FILL);
        flashPaint.setColor(FLASH_COLOR);
        flashPaint.setAlpha(0);

        glowPaint.setStyle(Paint.Style.FILL);
        glowPaint.setAlpha(0);

        // Must not intercept touch events so buttons/labels underneath stay usable.
        setClickable(false);
        setFocusable(false);
    }

    // -------------------------------------------------------------------------
    // Size change — build radial gradient for edge bloom (once per resize)
    // -------------------------------------------------------------------------

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            float cx = w / 2f;
            float cy = h / 2f;
            // Radius to the corner so the gradient fills the entire screen.
            float radius = (float) Math.sqrt(cx * cx + cy * cy);
            int rgb = Color.rgb(
                    Color.red(GLOW_COLOR),
                    Color.green(GLOW_COLOR),
                    Color.blue(GLOW_COLOR));
            // Transparent in the centre, opaque glow colour at the edges.
            RadialGradient gradient = new RadialGradient(
                    cx, cy, radius,
                    new int[]{Color.TRANSPARENT, rgb},
                    new float[]{0.35f, 1.0f},
                    Shader.TileMode.CLAMP);
            glowPaint.setShader(gradient);
        }
    }

    // -------------------------------------------------------------------------
    // Public API — clash flash
    // -------------------------------------------------------------------------

    /**
     * Trigger a screen flash. <b>Must be called on the main (UI) thread.</b>
     *
     * @param intensity 0–1, typically mapped from {@code SwordDevice.getHitStrength()}.
     *                  Values are clamped; anything above 0 produces at least a faint flash.
     */
    public void triggerFlash(float intensity) {
        intensity = Math.max(0.1f, Math.min(1.0f, intensity));

        // Cancel any in-flight animator so rapid clashes replace rather than stack.
        if (flashAnimator != null && flashAnimator.isRunning()) {
            flashAnimator.cancel();
        }

        final float peakAlpha = FLASH_MAX_ALPHA * intensity;
        long duration = FLASH_MIN_DURATION_MS
                + (long) ((FLASH_MAX_DURATION_MS - FLASH_MIN_DURATION_MS) * intensity);

        flashAlpha = peakAlpha;
        flashAnimator = ValueAnimator.ofFloat(peakAlpha, 0f);
        flashAnimator.setDuration(duration);
        flashAnimator.addUpdateListener(animation -> {
            flashAlpha = (float) animation.getAnimatedValue();
            invalidate();
        });
        flashAnimator.start();
        invalidate();
    }

    // -------------------------------------------------------------------------
    // Public API — hum glow
    // -------------------------------------------------------------------------

    /**
     * Set the desired hum-intensity target driven by sword activity
     * (0 = idle / no sword, 1 = maximum velocity). Thread-safe.
     */
    public void setHumTarget(float intensity) {
        humTarget = Math.max(0f, Math.min(1.0f, intensity));
    }

    /**
     * Advance the hum glow by one frame (~33 ms).
     * <b>Must be called on the main thread.</b>
     * Smooths towards the current target and adds a gentle idle breath pulse.
     */
    public void tickHumGlow() {
        // Low-pass: move towards target at rate HUM_SMOOTHING per tick.
        humSmoothed += HUM_SMOOTHING * (humTarget - humSmoothed);

        // Idle breath: sinusoidal pulse so a still-but-active sword still glows softly.
        float pulse = HUM_IDLE_PULSE_AMPLITUDE
                * (float) Math.sin(2.0 * Math.PI * System.currentTimeMillis() / HUM_IDLE_PULSE_PERIOD_MS);

        float displayIntensity = Math.max(0f, Math.min(1f, humSmoothed + pulse));
        float alpha = GLOW_MIN_ALPHA + (GLOW_MAX_ALPHA - GLOW_MIN_ALPHA) * displayIntensity;
        glowPaint.setAlpha((int) (alpha * 255));
        invalidate();
    }

    // -------------------------------------------------------------------------
    // Drawing — no allocations here
    // -------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        // Hum glow: radial gradient bloom from screen edges.
        canvas.drawRect(0, 0, w, h, glowPaint);

        // Clash flash: full-screen tinted overlay.
        if (flashAlpha > 0.001f) {
            flashPaint.setAlpha((int) (flashAlpha * 255));
            canvas.drawRect(0, 0, w, h, flashPaint);
        }
    }
}
