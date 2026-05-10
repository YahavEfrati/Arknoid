package geometry;

/**
 *This class is for representing a geometry.Point in the space. A point is built by two Positive numbers
 * who reresents the x and y coordinates.
 */
public class Point {
    /** This epsilon is the threshold to compare doubles.*/
    public static final     double EPSILON = 0.00001;
    /** x-coordinate. */
    private double x;
    /** y-coordinate. */
    private double y;

    /**
     * Constructor method for this class.
     * @param x for the x-coordinate of this point.
     * @param y for the y-coordinate of this point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // distance -- return the distance of this point to the other point

    /**
     *Distance -- return the distance of this point to the other point.
     * @param other point is space.
     * @return the distance from this point.
     */
    public double distance(Point other) {
        if (other == null) {
            return 0;
        }
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }
    /**
     * Method for checking if two points are equal.
     * @param other point in space.
     * @return true is the points are equal, false otherwise.
     */
    public boolean equals(Point other) {
        if (other == null) {
            return false;
        }
        return Math.abs(this.x - other.getX()) < EPSILON
                && Math.abs(this.y - other.getY()) < EPSILON;
    }
    /**
     * Geter for x.
     * @return x-value.
     */
    public double getX() {
        return x;
    }
    /**
     * Geter for y.
     * @return y-value.
     */
    public double getY() {
        return y;
    }
}