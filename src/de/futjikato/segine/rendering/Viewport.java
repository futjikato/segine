package de.futjikato.segine.rendering;

import de.futjikato.segine.SegineException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Created with IntelliJ IDEA.
 * User: Moritz
 * Date: 23.06.13
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class Viewport {

    private float x;

    private float y;

    private int vpHeight;

    private int vpWidth;

    private int screenWidth;

    private int screenHeight;

    private float widthRatio;

    private float heightRatio;

    private float blocksize;

    private int internalBlocksize;

    public Viewport(int blocksize, float startX, float startY, int width, int height, int screenWidth, int screenHeight) {
        this.internalBlocksize = blocksize;
        this.x = startX;
        this.y = startY;
        this.vpHeight = height;
        this.vpWidth = width;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        widthRatio = (float)screenWidth / vpWidth;
        heightRatio = (float)screenHeight / vpHeight;
        this.blocksize = (float)blocksize * widthRatio;
    }

    public void createWindow(String title) throws SegineException {

        if(Display.isCreated()) {
            throw new SegineException("Window is already created.");
        }

        try {
            Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
            Display.setTitle(title);
            Display.setResizable(true);
            Display.create();
        } catch (LWJGLException e) {
            throw new SegineException(e);
        }
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public Dimension translateDimension(Dimension dim) {
        float rx = (dim.getX() * blocksize) - x;
        float ry = (dim.getY() * blocksize) - y;
        return new Dimension(rx, ry, dim.getWidth() * widthRatio, dim.getHeight() * heightRatio);
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void close() {
        Display.destroy();
    }

    protected void updateScreenRatio() {
        this.screenHeight = Display.getHeight();
        this.screenWidth = Display.getHeight();

        widthRatio = (float)screenWidth / vpWidth;
        heightRatio = (float)screenHeight / vpHeight;

        this.blocksize = (float)internalBlocksize * widthRatio;
    }
}
