package de.futjikato.segine.rendering;

import de.futjikato.segine.SegineException;
import de.futjikato.segine.TextureManager;
import de.futjikato.segine.map.Map;
import de.futjikato.segine.map.MapPixel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import java.util.Collection;

/**
 * @author moritzspindelhirn
 * @category de.futjikato.segine.rendering
 */
public class Renderer {

    private Runnable renderCallback;

    private boolean rendering = false;

    private FrameCounter frameCounter;

    private DebugOption debug = DebugOption.NONE;

    public Renderer(Map map) {
        final Map renderMap = map;
        renderCallback = new Runnable() {
            @Override
            public void run() {
                try {
                    Collection<MapPixel> pixels = renderMap.getPixels(0, 0, 10, 10, false);

                    int texturedBlocks = 0;
                    for(MapPixel px : pixels) {
                        Image texture = px.getTexture();
                        int blocksize = px.getBlocksize();

                        if(texture != null) {
                            int rx = px.getX() * blocksize;
                            int ry = px.getY() * blocksize;

                            texture.draw(rx, ry, texture.getWidth(), texture.getHeight());
                            texturedBlocks++;
                        }
                    }

                    log(String.format("Draw frame with %d blocks ( %d textured )", pixels.size(), texturedBlocks), DebugOption.INFO);
                } catch (SegineException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void init() {
        // render
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        // enable textures
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glLoadIdentity();
    }

    private void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
    }

    private void log(String message, DebugOption level) throws SegineException {
        if(level.equals(DebugOption.NONE))
            throw new SegineException("You cannot log messages on level NONE.");

        if(level.compareTo(debug) >= 0) {
            // TODO implement logger class abstraction stuff
            System.out.println(String.format("%s%s", level.getLogPrefix(), message));
        }
    }

    public void start(Runnable postRender) {

        rendering = true;
        while(rendering) {
            clear();
            // initialize OpenGL
            init();

            renderCallback.run();

            if(frameCounter != null) {
                frameCounter.frame();
            }

            // update screen
            Display.update();

            // call post render callback
            postRender.run();

            try {
                Thread.currentThread().sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFrameCounter(FrameCounter frameCounter) {
        this.frameCounter = frameCounter;
    }

    public void setDebug(DebugOption debug) {
        this.debug = debug;
    }

    public void end() {
        rendering = false;
    }
}
