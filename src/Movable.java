/**
 * This interface represents objects that can be moved.
 */
public interface Movable {

    /**
     * This method moves the object by the specified amounts.
     *
     * @param dx The change in the x-coordinate
     * @param dy The change in the y-coordinate
     */
    void move(double dx, double dy);
}
