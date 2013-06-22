package de.futjikato.segine.map;

import de.futjikato.segine.SegineException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author moritzspindelhirn
 * @category de.futjikato.segine.map
 */
public class MapReader {

    private Image mapfile;

    private Graphics mapGraphic;

    public MapReader(Image mapfile) throws SegineException {
        try {
            this.mapfile = mapfile;
            this.mapGraphic = mapfile.getGraphics();
        } catch (SlickException e) {
            throw new SegineException(e);
        }
    }

    public Map readMap() throws SegineException {
        Map map = new Map(mapfile.getWidth(), mapfile.getHeight());

        for(int x = 0 ; x < mapfile.getWidth() ; x++) {
            for(int y = 0 ; y < mapfile.getHeight() ; y++) {
                MapPixel pixel = new MapPixel();
                pixel.setX(x).setY(y).setColor(mapGraphic.getPixel(x, y));

                map.insert(pixel, false);
            }
        }

        return map;
    }

}
