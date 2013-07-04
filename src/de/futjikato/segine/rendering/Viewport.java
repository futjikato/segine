package de.futjikato.segine.rendering;

import de.futjikato.segine.SegineException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class Viewport {

    private float x;

    private float y;

    private int vpHeight;

    private int vpWidth;

    private int screenWidth;

    private int screenHeight;

    private float widthRatio;

    private float heightRatio;

    private float blockWidth;

    private float blockHeight;

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

        this.blockWidth = (float)blocksize * widthRatio;
        this.blockHeight = (float)blocksize * heightRatio;
    }

    public void createWindow(String title) throws SegineException {

        if(Display.isCreated()) {
            throw new SegineException("Window is already created.");
        }

        try {
            Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
            Display.setTitle(title);
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

    public float getBlockHeight() {
        return blockHeight;
    }

    public float getBlockWidth() {
        return blockWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getVpWidth() {
        return vpWidth;
    }

    public int getVpHeight() {
        return vpHeight;
    }

    public Dimension translateDimension(Dimension dim) {
        float rx = dim.getX();
        float ry = dim.getY();
        float rw = dim.getWidth();
        float rh = dim.getHeight();

        // translate position if needed
        if(!dim.isAbolutePosition()) {
            rx = rx * blockWidth;
            ry = ry * blockHeight;
        }
        rx -= x;
        ry -= y;

        // translate size if needed
        if(!dim.isAbsoluteSize()) {
            rw = rw * widthRatio;
            rh = rh * heightRatio;
        }

        return new Dimension(rx, ry, rw, rh);
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

        this.blockWidth = (float)internalBlocksize * widthRatio;
        this.blockHeight = (float)internalBlocksize * heightRatio;
    }
}
