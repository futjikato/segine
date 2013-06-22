package de.futjikato.segine.map;

import de.futjikato.segine.SegineException;
import de.futjikato.segine.TextureManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author moritzspindelhirn
 * @todo Documentation
 * @category de.futjikato.segine.map
 */
public class MapPixel {

    private int x;
    private int y;
    private Color color;
    private Image texture;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public MapPixel setX(int x) {
        this.x = x;
        return this;
    }

    public MapPixel setY(int y) {
        this.y = y;
        return this;
    }

    public MapPixel setColor(Color color) {
        this.color = color;
        return this;
    }

    public Image getTexture() {
        if(texture == null) {
            try {
                texture = TextureManager.getInstance().getByColorKey(color.getRed(), color.getGreen(), color.getBlue());
            } catch (SegineException e) {
                return null;
            }
        }

        return texture;
    }

    public int getBlocksize() {
        return 25;
    }
}
