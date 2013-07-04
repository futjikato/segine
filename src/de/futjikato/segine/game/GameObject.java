package de.futjikato.segine.game;

import de.futjikato.segine.rendering.Dimension;

public class GameObject {

    private Dimension position;

    public Dimension getDimension() {
        return position;
    }

    public void setDimension(Dimension position) {
        this.position = position;
    }

    protected void move(float x, float y) {
        position = new Dimension(position.getX() + x, position.getY() + y, position.getWidth(), position.getHeight(), position.isAbolutePosition(), position.isAbsoluteSize());
    }
}
