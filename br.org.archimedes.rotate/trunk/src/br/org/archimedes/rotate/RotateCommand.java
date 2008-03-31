/*
 * Created on 18/08/2006
 */

package br.org.archimedes.rotate;

import java.util.Collection;

import br.org.archimedes.exceptions.IllegalActionException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.interfaces.UndoableCommand;
import br.org.archimedes.model.Drawing;
import br.org.archimedes.model.Element;
import br.org.archimedes.model.Layer;
import br.org.archimedes.model.Point;


/**
 * Belongs to package br.org.archimedes.model.commands.
 */
public class RotateCommand implements UndoableCommand {

    private Point reference;

    private double angle;

    private Collection<Element> selection;


    /**
     * Constructor.
     * 
     * @param selection
     *            The collection of elements that should be rotated.
     * @param reference
     *            The reference point for the rotation
     * @param angle
     *            The angle in radians
     * @throws NullArgumentException
     *             Thrown if any argument is null.
     */
    public RotateCommand (Collection<Element> selection,
            Point reference, double angle) throws NullArgumentException {

        if (selection == null || reference == null) {
            throw new NullArgumentException();
        }

        this.selection = selection;
        this.reference = reference;
        this.angle = angle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.org.archimedes.model.commands.Command#doIt(br.org.archimedes.model.Drawing)
     */
    public void doIt (Drawing drawing) throws IllegalActionException,
            NullArgumentException {

        rotateElements(drawing, angle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.org.archimedes.model.commands.UndoableCommand#undoIt(br.org.archimedes.model.Drawing)
     */
    public void undoIt (Drawing drawing) throws IllegalActionException,
            NullArgumentException {

        rotateElements(drawing, -angle);
    }

    /**
     * @param drawing
     *            The drawing in which the elements should be.
     * @param angle
     *            The angle of the rotatioh in radians
     * @throws NullArgumentException
     *             Thrown if the drawing is null.
     * @throws IllegalActionException
     *             Thrown if some element is not in the drawing.
     */
    private void rotateElements (Drawing drawing, double angle)
            throws NullArgumentException, IllegalActionException {

        if (drawing == null) {
            throw new NullArgumentException();
        }
        boolean shouldThrowIllegal = false;
        for (Element element : selection) {
            Layer layer = element.getLayer();
            if (layer != null && !layer.isLocked()
                    && drawing.contains(layer)
                    && layer.contains(element)) {
                element.rotate(reference, angle);
            }
            else {
                shouldThrowIllegal = true;
            }
        }

        if (shouldThrowIllegal) {
            // TODO Que mensagem que eu ponho??
            throw new IllegalActionException();
        }
    }
}