package de.futjikato.segine.game.camera;

import de.futjikato.segine.rendering.Viewport;

public class StaticCamera extends Camera {

    private float x;

    private float y;

    public StaticCamera(Viewport vp, float x, float y) {
        super(vp);

        this.x = x;
        this.y = y;
    }

    @Override
    public void frame(long delta) {
        vp.move(x, y);
    }
}
