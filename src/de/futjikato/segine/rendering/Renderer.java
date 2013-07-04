package de.futjikato.segine.rendering;

import de.futjikato.segine.SegineException;
import de.futjikato.segine.TextureManager;
import de.futjikato.segine.game.camera.Camera;
import de.futjikato.segine.map.Map;
import de.futjikato.segine.map.MapPixel;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * @author moritzspindelhirn
 * @category de.futjikato.segine.rendering
 */
public class Renderer extends Observable {

    private boolean rendering = false;

    private FrameCounter frameCounter;

    private DebugOption debug = DebugOption.NONE;

    private List<RenderContainer> renderContainer = new LinkedList<RenderContainer>();

    private Viewport vp;

    private int syncFps = 60;

    private boolean useSyncFps = true;

    private Camera camera;

    private long lastFrame;

    public Renderer(Viewport vp) throws SegineException {
        this.vp = vp;
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
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

    public void addRenderContainer(RenderContainer container) {
        int index = 0;
        for(RenderContainer listElement : renderContainer) {
            if(listElement.getRenderOrder() > container.getRenderOrder()) {
                renderContainer.add(index, container);
                return;
            }
            index++;
        }
        renderContainer.add(container);
    }

    public void start() throws SegineException {
        start(null, null);
    }

    public void start(Runnable preRender, Runnable postRender) throws SegineException {
        initOpenGL();

        // init all container
        for(RenderContainer container : renderContainer) {
            container.init();
        }

        rendering = true;
        lastFrame = getTime();
        while(rendering) {
            // call pre render callback
            if(preRender != null) {
                preRender.run();
            }

            // calculate delta from last frame
            long newFrame = getTime();
            long delta = newFrame - lastFrame;
            lastFrame = newFrame;

            // notify observers about upcoming new frame
            setChanged();
            notifyObservers(delta);

            // call camera to adjust viewport to render
            if(camera != null) {
                camera.frame(delta);
            }

            // clear
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();

            // render all container
            for(RenderContainer container : renderContainer) {
                List<Renderable> renderItem = container.filter(vp);

                for(Renderable renderable : renderItem) {
                    Image img = renderable.getImage();
                    Dimension dim = renderable.getDimension();
                    if(img != null && dim != null) {
                        dim = vp.translateDimension(dim);
                        img.draw(dim.getX(), dim.getY(), dim.getWidth(), dim.getHeight());
                    }
                }
            }

            Display.update();

            if(useSyncFps) {
                Display.sync(syncFps);
            }

            // notify frameCounter if one exists
            if(frameCounter != null) {
                frameCounter.frame();
            }

            // call post render callback
            if(postRender != null) {
                postRender.run();
            }

            if(Display.isCloseRequested()) {
                rendering = false;
            }

            // if screen was resized update dimension ratio
            if(Display.wasResized()) {
                // update viewport render ratio
                vp.updateScreenRatio();
                // set new canvas size within openGL context
                GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            }
        }

        // close window
        vp.close();
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

    public void setSyncFps(int fps) {
        syncFps = fps;
    }

    public void useSyncFps(boolean flag) {
        useSyncFps = flag;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
