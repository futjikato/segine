package de.futjikato.segine.game.camera;

import de.futjikato.segine.rendering.Viewport;

public abstract class Camera {

    protected Viewport vp;

    public Camera(Viewport vp) {
        this.vp = vp;
    }

    public abstract void frame(long delta);

}
