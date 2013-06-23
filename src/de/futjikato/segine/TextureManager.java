package de.futjikato.segine;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.TreeMap;

/**
 * @author moritzspindelhirn
 * @todo Documentation
 * @category de.futjikato.segine
 */
public class TextureManager {

    private static TextureManager instance;

    private Image[][][] colorKeyMap = new Image[256][256][256];

    private String[][][] colorKeyNameMap = new String[256][256][256];

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

    public void setColorKey(int r, int g, int b, String filename) throws SegineException {
        if(validateRgbValue(r, g, b))
            throw new SegineException("RGB value out of valid range.");

        colorKeyNameMap[r][g][b] = filename;
    }

    public Image getByColorKey(int r, int g, int b) throws SegineException {
        if(validateRgbValue(r, g, b))
            throw new SegineException("RGB value out of valid range.");

        try {
            if(colorKeyMap[r][g][b] == null && colorKeyNameMap[r][g][b] != null) {
                colorKeyMap[r][g][b] = new Image(colorKeyNameMap[r][g][b]);
            }
        } catch(SlickException e) {
            throw new SegineException(e);
        }

        return colorKeyMap[r][g][b];
    }
}
