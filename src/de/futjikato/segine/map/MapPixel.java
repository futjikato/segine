package de.futjikato.segine.map;

import de.futjikato.segine.SegineException;
import de.futjikato.segine.TextureManager;
import de.futjikato.segine.rendering.Dimension;
import de.futjikato.segine.rendering.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author moritzspindelhirn
 * @todo Documentation
 * @category de.futjikato.segine.map
 */
public class MapPixel implements Renderable {

    private int x;
    private int y;
    private int[] color;
    private Image texture;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(x, y, 1, 1);
    }

    public MapPixel setX(int x) {
        this.x = x;
        return this;
    }

    public MapPixel setY(int y) {
        this.y = y;
        return this;
    }

    public MapPixel setColor(int[] color) {
        this.color = color;
        return this;
    }

    public Image getImage() {
        if(texture == null) {
            try {
                texture = TextureManager.getInstance().getByColorKey(color[0], color[1], color[2]);
            } catch (SegineException e) {
                return null;
            }
        }

        return texture;
    }
}
