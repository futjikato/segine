package de.futjikato.segine.game;

import de.futjikato.segine.SegineException;
import de.futjikato.segine.rendering.*;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class Player extends GameObject implements RenderContainer, Renderable, Observer {

    private Input input;

    private List<Renderable> renderContainerList = new ArrayList<Renderable>();

    public Player() {
        input = new Input(Display.getHeight());
        setDimension(getBasicDimension());
        renderContainerList.add(this);
    }

    @Override
    public void init() throws SegineException {

    }

    @Override
    public List<Renderable> filter(Viewport viewport) {
        return renderContainerList;
    }


    @Override
    public int getRenderOrder() {
        return 50;
    }

    /**
     * The player observes the renderer which will notify the player on every frame
     * This method triggers the input handling
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Renderer) {
            handleInput(arg);
            return;
        }
    }

    private void handleInput(Object delta) {
        // if a delta is supplied by renderer use for frame independent movement
        long multiplier = 16;
        if(delta instanceof Long) {
            multiplier = (Long) delta;
        }

        float baseSpeed = 0.0625f;

        if(input.isKeyDown(Input.KEY_W)) {
            move(0, -(baseSpeed*multiplier));
        }

        if(input.isKeyDown(Input.KEY_A)) {
            move(-(baseSpeed*multiplier), 0);
        }

        if(input.isKeyDown(Input.KEY_S)) {
            move(0, (baseSpeed*multiplier));
        }

        if(input.isKeyDown(Input.KEY_D)) {
            move((baseSpeed*multiplier), 0);
        }
    }

    public abstract Dimension getBasicDimension();
}
