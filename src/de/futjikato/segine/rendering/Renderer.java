package de.futjikato.segine.rendering;

import de.futjikato.segine.SegineException;
import de.futjikato.segine.TextureManager;
import de.futjikato.segine.map.Map;
import de.futjikato.segine.map.MapPixel;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author moritzspindelhirn
 * @category de.futjikato.segine.rendering
 */
public class Renderer {

    private Runnable renderCallback;

    private boolean rendering = false;

    private FrameCounter frameCounter;

    private DebugOption debug = DebugOption.NONE;

    private List<RenderContainer> renderContainer = new LinkedList<RenderContainer>();

    private Viewport vp;

    public Renderer() throws SegineException {
        createWindow(700, 700, "Segine");
        initOpenGL();
        vp = new Viewport();
    }

    private void initOpenGL() {
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

    private void createWindow(int width, int height, String title) throws SegineException {
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(title);
            Display.create();
        } catch (LWJGLException e) {
            throw new SegineException(e);
        }
    }

    private void log(String message, DebugOption level) throws SegineException {
        if(level.equals(DebugOption.NONE))
            throw new SegineException("You cannot log messages on level NONE.");

        if(level.compareTo(debug) >= 0) {
            // TODO implement logger class abstraction stuff
            System.out.println(String.format("%s%s", level.getLogPrefix(), message));
        }
    }

    public void addRenderContainer(RenderContainer container) {
        renderContainer.add(container);
    }

    public void start() throws SegineException {
        start(null, null);
    }

    public void start(Runnable preRender, Runnable postRender) throws SegineException {

        // init all container
        for(RenderContainer container : renderContainer) {
            container.init();
        }

        rendering = true;
        while(rendering) {
            // call pre render callback
            if(preRender != null) {
                preRender.run();
            }

            // render all container
            for(RenderContainer container : renderContainer) {
                List<Renderable> renderItem = container.filter(vp);

                for(Renderable renderable : renderItem) {
                    Image img = renderable.getImage();
                    if(img != null) {
                        img.draw(renderable.getX() * 50, renderable.getY() * 50, 50, 50);
                    }
                }
            }

            /* ++++++++++++++++++++++ */
            try {
                new Image("game/ground/grass02.png").draw(0, 0, 50, 50);
            } catch (SlickException e) {
                throw new SegineException(e);
            }
            /* ++++++++++++++++++++++ */

            // notify frameCounter if one exists
            if(frameCounter != null) {
                frameCounter.frame();
            }

            // call post render callback
            if(postRender != null) {
                postRender.run();
            }

            Display.update();

            if(Display.isCloseRequested()) {
                rendering = false;
            }
        }

        // remove window on close
        Display.destroy();
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
