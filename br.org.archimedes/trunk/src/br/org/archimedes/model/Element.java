/*
 * Created on 17/04/2006
 */

package br.org.archimedes.model;

import java.util.Collection;
import java.util.List;

import br.org.archimedes.exceptions.IllegalActionException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.gui.opengl.OpenGLWrapper;
import br.org.archimedes.Utils;

/**
 * Belongs to package br.org.archimedes.model.
 * 
 * @author marivb
 */
public abstract class Element {

    protected Layer parentLayer;


    /**
     * @return Returns the parentLayer.
     */
    public Layer getLayer () {

        return parentLayer;
    }

    /**
     * @param parentLayer
     *            The parentLayer to set.
     */
    public void setLayer (Layer parentLayer) {

        this.parentLayer = parentLayer;
    }

    /**
     * @return A copy of this element.
     */
    public abstract Element clone ();

    /**
     * @param rectangle
     *            The rectangle to test
     * @return False if there is any point beyond the rectangle border, true
     *         otherwise.
     * @throws NullArgumentException
     *             thrown if the rectangle is null.
     */
    public boolean isInside (Rectangle rectangle) {

        Rectangle boundary = getBoundaryRectangle();
        return boundary.isInside(rectangle);
    }

    /**
     * @param rectangle
     *            The rectangle to test
     * @return True if this element intersects the rectangle, false otherwise
     *         (if it is inside it does not intersect).
     * @throws NullArgumentException
     *             thrown if the rectangle is null.
     */
    public abstract boolean intersects (Rectangle rectangle)
            throws NullArgumentException;

    /**
     * Calculates the intersection points between the elements.
     * 
     * @param element
     *            The other element.
     * @return A collection with the intersection points. It's empty if there is
     *         no intersection or if there are infinite intersections. The
     *         intersections may not be contained in the elements.
     * @throws NullArgumentException
     *             thrown if the element is null.
     */
    public Collection<Point> getIntersection (Element element)
            throws NullArgumentException {
    	if (element == null)
    		throw new NullArgumentException();
    	Intersector intersector = Utils.getIntersectorFor(this, element);
    	return intersector.getIntersections(this, element);
    }

    /**
     * Moves the element's points by the given vector.
     * 
     * @param pointsToMove
     *            A collection with the points of this element that should be
     *            moved.
     * @param vector
     *            The translation vector
     * @throws NullArgumentException
     *             Thrown if any argument is null.
     */
    public void move (Collection<Point> pointsToMove, Vector vector)
            throws NullArgumentException {

        if (pointsToMove == null || vector == null) {
            throw new NullArgumentException();
        }

        for (Point point : pointsToMove) {
            point.move(vector.getX(), vector.getY());
        }
    }

    /**
     * Moves the element by the given delta.
     * 
     * @param deltaX
     *            The delta in the x direction.
     * @param deltaY
     *            The delta in the y direction.
     */
    public void move (double deltaX, double deltaY) {

        try {
            move(getPoints(), new Vector(new Point(deltaX, deltaY)));
        }
        catch (NullArgumentException e) {
            // Should never happen
            e.printStackTrace();
        }
    }

    /**
     * @param object
     *            The element to be compared.
     * @return true if element is the same as this, false otherwise.
     */
    public abstract boolean equals (Object object);

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode () {

        String stringRep = toString();
        return stringRep.hashCode();
    }

    /**
     * @return the rectangle that contains this element, or a rectangle with 0
     *         width an height if this element cannot be contained.
     */
    public abstract Rectangle getBoundaryRectangle ();

    /**
     * @param area
     *            The area in which the reference points are
     * @return The reference points of the element in the specified area
     */
    public abstract Collection<? extends ReferencePoint> getReferencePoints (
            Rectangle area);

    /**
     * Calculates the closest projection of a point on the element. It does not
     * check whether or not the projection is contained in the element.
     * 
     * @param point
     *            The point to project
     * @return The closest projection point or null if the operation does not
     *         apply.
     * @throws NullArgumentException
     *             In case the point is null
     */
    public abstract Point getProjectionOf (Point point)
            throws NullArgumentException;

    /**
     * Checks if the element contains the point. By contain it is understood
     * that the borders of the element (if it has borders) contains the point.
     * 
     * @param point
     *            The point to be checked
     * @return True if the element contains the point. False otherwise.
     */
    public abstract boolean contains (Point point) throws NullArgumentException;

    /**
     * Verifies if the elements are collinear
     * 
     * @param element
     *            The element to be compared with this
     * @return true if the elements are collinear, false if they are not
     *         collinear or if they cannot be compared
     */
    public abstract boolean isCollinearWith (Element element);

    /**
     * Verifies if the elements are parallel
     * 
     * @param element
     *            The element to be compared with this
     * @return true if the elements are parallel, false if they are not parallel
     *         or if they cannot be compared
     */
    public abstract boolean isParallelTo (Element element);

    /**
     * Rotate the element around the reference point counter-clockwise.
     * 
     * @param rotateReference
     *            the reference point
     * @param angle
     *            the angle to rotate the element (in radians)
     * @throws NullArgumentException
     *             throw if the reference point is null.
     */
    public void rotate (Point rotateReference, double angle)
            throws NullArgumentException {

        if (rotateReference == null) {
            throw new NullArgumentException();
        }
        for (Point point : getPoints()) {
            point.rotate(rotateReference, angle);
        }
    }

    /**
     * Scales the element by proportion, maintaining the reference point intact.
     * 
     * @param scaleReference
     *            The reference point
     * @param proportion
     *            The proportion
     * @throws NullArgumentException
     *             In case the reference is null
     * @throws IllegalActionException
     *             In case the proportion is negative
     */
    public void scale (Point scaleReference, double proportion)
            throws NullArgumentException, IllegalActionException {

        if (scaleReference == null) {
            throw new NullArgumentException();
        }
        if (proportion <= 0) {
            throw new IllegalActionException();
        }

        for (Point point : getPoints()) {
            point.scale(scaleReference, proportion);
        }
    }

    /**
     * Returns a list of points that define the element so that operations like
     * scale and move may be performed on it.
     * 
     * @return A list of points that defines this element
     */
    public abstract List<Point> getPoints ();

    /**
     * @return true if the element is closed, false otherwise.
     */
    public boolean isClosed () {

        return false;
    }

    /**
     * @param point
     *            Point to find the nearest extreme point.
     * @return The nearest extreme point to the passed point. If there are more
     *         than one return any of them.
     * @throws NullArgumentException
     *             Thrown if the point is null.
     */
    public abstract Point getNearestExtremePoint (Point point)
            throws NullArgumentException;

    /**
     * Mirrors this element relative to a given axis. This changes the element
     * being mirrored.
     * 
     * @param p1
     *            The first point to define the axis
     * @param p2
     *            The second point to define the axis
     * @throws NullArgumentException
     *             In case the mirror axis is null.
     * @throws IllegalActionException
     *             In case the element's layer is locked or the mirror is not
     *             valid.
     */
    public void mirror (Point p1, Point p2) throws NullArgumentException,
            IllegalActionException {

        if (p1 == null || p2 == null) {
            throw new NullArgumentException();
        }
        if (parentLayer != null && parentLayer.isLocked()) {
            throw new IllegalActionException();
        }

        List<Point> points = getPoints();
        
        calculateMirror(p1, p2, points);
        
        if (parentLayer != null) {
            Collection<Element> elements = parentLayer.getElements();

            for (Element element : elements) {
                if (element != this && element.equals(this)) {
                    throw new IllegalActionException();
                }
            }
        }
    }

    /**
     * Mirrors each point in the list relative to the axis.
     * 
     * @param p1
     *            The first point to define the axis
     * @param p2
     *            The second point to define the axis
     * @param points
     *            The points to be mirrored.
     */
    private void calculateMirror (Point p1, Point p2, List<Point> points) {

        for (Point point : points) {
                        
            double a, b, c, d, e, f;
            
            double x, y;
            
            a = p2.getX() - p1.getX();
            b = p2.getY() - p1.getY();
            c= p1.getY() - p2.getY();
            d = p2.getX() - p1.getX();
            e = point.getX() * (p2.getX() - p1.getX()) + point.getY()*(p2.getY() - p1.getY());
            f = p1.getY()*(p2.getX() - p1.getX()) - p1.getX() * (p2.getY() - p1.getY());
            
            x = (d*e - f*b)/(d*a - b*c);
            y = (e - a*x)/b;
            
            Point projection = new Point(x,y);
            
            if ( !point.equals(projection)) {
                Vector moveBy = new Vector(point, projection);
                moveBy = moveBy.multiply(2.0);
                point.move(moveBy.getX(), moveBy.getY());
            }
        }
    }
    
    public abstract void draw(OpenGLWrapper wrapper);
}