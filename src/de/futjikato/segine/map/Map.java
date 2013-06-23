package de.futjikato.segine.map;

import de.futjikato.segine.SegineException;
import de.futjikato.segine.rendering.RenderContainer;
import de.futjikato.segine.rendering.Renderable;
import de.futjikato.segine.rendering.Viewport;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author moritzspindelhirn
 * @category de.futjikato.segine.map
 */
public class Map implements RenderContainer {

    private MapPixel[][] storage;

    private int width;

    private int height;

    private String mapFile;

    private Image imageMap;

    private Graphics mapGraphic;

    public Map(String mapFile) {
        this.mapFile = mapFile;
    }

    private Map insert(MapPixel pixel, boolean allowOverwrite) throws SegineException {
        // check if pixel position is valid
        if(
            pixel.getX() < 0 ||
            pixel.getY() < 0 ||
            pixel.getX() > width ||
            pixel.getY() > height
        )
            throw new SegineException("Pixel out of map.");

        // check pixel position if overwrite is not allowed
        if(!allowOverwrite && storage[pixel.getX()][pixel.getY()] != null)
            throw new SegineException("Pixel position is already filled.");

        // store pixel
        storage[pixel.getX()][pixel.getY()] = pixel;

        return this;
    }

    public Collection<MapPixel> getPixels(int x, int y, int x2, int y2, boolean autoSizeCorrect) throws SegineException {

        // validate range
        if(!autoSizeCorrect) {
            if(
                x < 0 || x > width ||
                y < 0 || y > height ||
                x2 < 0 || x2 > width ||
                y2 < 0 || y2 > height
            )
                throw new SegineException("Pixel range reaches out of map.");
        }

        if(x2 <= x || y2 <= y )
            throw new SegineException("Pixel range zero or sub-zero.");

        Collection<MapPixel> pixels = new HashSet<MapPixel>();
        for(int iterX = x ; iterX < x2 ; iterX++) {
            for(int iterY = y ; iterY < y2 ; iterY++) {
                pixels.add(storage[iterX][iterY]);
            }
        }

        return pixels;
    }

    @Override
    public void init() throws SegineException {
        try {
            // load image map
            imageMap = new Image(mapFile);

            // extract graphic object
            mapGraphic = imageMap.getGraphics();
        } catch (SlickException e) {
            throw new SegineException(e);
        }

        // build basic storage array
        width = imageMap.getWidth();
        height = imageMap.getHeight();
        storage = new MapPixel[width][height];

        for(int x = 0 ; x < width ; x++) {
            for(int y = 0 ; y < height ; y++) {
                MapPixel pixel = new MapPixel();
                pixel.setX(x).setY(y).setColor(mapGraphic.getPixel(x, y));

                insert(pixel, false);
            }
        }
    }

    @Override
    public List<Renderable> filter(Viewport viewport) {
        List<Renderable> blocksInViewport = new LinkedList<Renderable>();

        // TODO replace demo code with real code working with viewport
        for(int x = 0 ; x < 10 ; x++ ) {
            for(int y = 0 ; y < 10 ; y++) {
                blocksInViewport.add(storage[x][y]);
            }
        }

        return blocksInViewport;
    }
}
