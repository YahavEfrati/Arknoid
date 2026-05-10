package geometry;

/**
 * Class for speed vectors of moving objects.
 * A Velcocity vector is can be presented in two ways.
 * One is (x,y) coordinates from (0,0).
 * The second way is by the line who created by the x,y, who represented by the speed (length of the line),
 * and the angle of the vector from the X-tzir.
 */
public class Velocity {
    /**
     * The delta x.
     */
    private double dx;
    /**
     * The delta y.
     */
    private double dy;
    /**
     * The angle of the vector, in degrees.
     */
    private double angle;
    /**
     * The speed = the length of the vector.
     */
    private double speed;

    /**
     * Constructor.
     *
     * @param dx - delta x.
     * @param dy - delta y.
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        this.angle = getAngleByPoints();
        this.speed = getSpeedByPoints();
    }

    /**
     * Calculating the speed with the dx dy.
     *
     * @return the speed value.
     */
    private double getSpeedByPoints() {
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    /**
     * Calculating the angle with the dx dy.
     *
     * @return the angle value, in degrees.
     */
    private double getAngleByPoints() {
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * A second constructor, by speed and angle.
     *
     * @param angle of the vector.
     * @param speed of the vector.
     * @return a new geometry.Velocity object.
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double radians = Math.toRadians(angle);
        double dx = speed * Math.sin(radians);
        double dy = -speed * Math.cos(radians);
        return new Velocity(dx, dy);
    }

    /**
     * Geter for x.
     *
     * @return dx value.
     */
    public double getDx() {
        return dx;
    }

    /**
     * Geter for y.
     *
     * @return dy value.
     */
    public double getDy() {
        return dy;
    }

    /**
     * Geter for speed.
     *
     * @return speed value.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Geter for angle.
     *
     * @return angle value.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Method for making objects move. For each run, the method advances the object by one speed.
     *
     * @param p - a center point of an object.
     * @return the new point, after the update.
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }

}
