package de.futjikato.segine;

import org.newdawn.slick.Image;

import java.util.TreeMap;

/**
 * @author moritzspindelhirn
 * @todo Documentation
 * @category de.futjikato.segine
 */
public class TextureManager {

    private static TextureManager instance;

    private Image[][][] colorKeyMap = new Image[256][256][256];

    private TextureManager() {

    }

    private boolean validateRgbValue(int r, int g, int b) {
        return (r < 0 || r > 255 ||
                g < 0 || g > 255 ||
                b < 0 || b > 255);
    }

    public static TextureManager getInstance() {
        if(instance == null)
            instance = new TextureManager();

        return instance;
    }

    public void setColorKey(int r, int g, int b, Image texture) throws SegineException {
        if(validateRgbValue(r, g, b))
            throw new SegineException("RGB value out of valid range.");

        colorKeyMap[r][g][b] = texture;
    }

    public Image getByColorKey(int r, int g, int b) throws SegineException {
        if(validateRgbValue(r, g, b))
            throw new SegineException("RGB value out of valid range.");

        return colorKeyMap[r][g][b];
    }
}
