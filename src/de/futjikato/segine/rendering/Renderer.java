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

    private boolean rendering = false;

    private FrameCounter frameCounter;

    private DebugOption debug = DebugOption.NONE;

    private List<RenderContainer> renderContainer = new LinkedList<RenderContainer>();

    private Viewport vp;

    private int syncFps = 60;

    private boolean useSyncFps = true;

    public Renderer(Viewport vp) throws SegineException {
        this.vp = vp;
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
        while(rendering) {
            // call pre render callback
            if(preRender != null) {
                preRender.run();
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
                        Dimension renderDim = vp.translateDimension(dim);
                        img.draw(renderDim.getX(), renderDim.getY(), renderDim.getWidth(), renderDim.getHeight());
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

        // remove window on close
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
}
