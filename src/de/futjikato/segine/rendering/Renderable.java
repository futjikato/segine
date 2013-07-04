package de.futjikato.segine.rendering;

import org.newdawn.slick.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Moritz
 * Date: 23.06.13
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public interface Renderable {

    public Image getImage();

    public int getX();

    public int getY();

    public Dimension getDimension();
}
