package de.futjikato.segine.map;

import de.futjikato.segine.SegineException;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author moritzspindelhirn
 * @category de.futjikato.segine.map
 */
public class Map {

    private MapPixel[][] storage;

    private int width;

    private int height;

    protected Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.storage = new MapPixel[width][height];
    }

    protected Map insert(MapPixel pixel, boolean allowOverwrite) throws SegineException {
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
}
