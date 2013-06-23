package de.futjikato.segine.rendering;

import de.futjikato.segine.SegineException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Moritz
 * Date: 23.06.13
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
public interface RenderContainer {

    public void init() throws SegineException;

    public List<Renderable> filter(Viewport viewport);
}
