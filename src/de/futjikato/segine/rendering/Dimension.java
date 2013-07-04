package de.futjikato.segine.rendering;

/**
 * Created with IntelliJ IDEA.
 * User: Moritz
 * Date: 01.07.13
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class Dimension {

    private float x;

    private float y;

    private float width;

    private float height;

    private boolean absoluteSize;

    private boolean abolutePosition;

    public Dimension(float x, float y) {
        this(x, y, 1, 1);
    }

    public Dimension(float x, float y, float width, float height) {
        this(x, y, width, height, false, false);
    }

    public Dimension(float x, float y, float width, float height, boolean abolutePosition, boolean absoluteSize) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.abolutePosition = abolutePosition;
        this.absoluteSize = absoluteSize;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String toString() {
        return String.format("[Dimension X(%f) Y(%f) Width(%f) Height(%f)]", x, y, width, height);
    }

    public boolean isAbolutePosition() {
        return abolutePosition;
    }

    public boolean isAbsoluteSize() {
        return absoluteSize;
    }
}
