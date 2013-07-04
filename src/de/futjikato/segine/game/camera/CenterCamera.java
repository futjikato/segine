package de.futjikato.segine.game.camera;

import de.futjikato.segine.game.GameObject;
import de.futjikato.segine.rendering.Dimension;
import de.futjikato.segine.rendering.Viewport;

public class CenterCamera extends Camera {

    private GameObject center;

    public CenterCamera(Viewport vp, GameObject center) {
        super(vp);

        this.center = center;
    }

    @Override
    public void frame(long delta) {
        Dimension dim = center.getDimension();
        float x = dim.getX() - ( vp.getScreenWidth() / 2 ) + ( dim.getWidth() / 2 );
        float y = dim.getY() - ( vp.getScreenHeight() / 2 ) + ( dim.getHeight() / 2 );

        vp.move(x, y);
    }
}
