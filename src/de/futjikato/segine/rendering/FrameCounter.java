package de.futjikato.segine.rendering;

import org.lwjgl.Sys;

/**
 * @author moritzspindelhirn
 * @category de.futjikato.segine.rendering
 */
public abstract class FrameCounter {

    private long lastFrame;

    private long lastUpdate;

    private int frames;

    public FrameCounter() {
        reset();
    }

    public void frame() {
        lastFrame = getTime();
        frames++;

        if(lastFrame - lastUpdate > 1000) {
            update(frames);
            reset();
        }
    }

    public abstract void update(int frames);

    private void reset() {
        lastFrame = getTime();
        lastUpdate = getTime();
        frames = 0;
    }

    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

}
