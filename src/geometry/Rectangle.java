package geometry;

/**
 * Class or represents a rectangular shape in space.
 */

public class Rectangle {
    private Point upperLeft;
    private double width;
    private double height;

    /**
     * Constructs a geometry.Rectangle with a given upper-left point, width, and height.
     *
     * @param upperLeft The upper-left corner point.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }
    /**
     * Gets the upper-left corner point of the rectangle.
     *
     * @return The upper-left corner geometry.Point.
     */
    public Point getUpperLeft() {
        return this.upperLeft;
    }

    /**
     * Sets the upper-left corner point of the rectangle.
     * @param upperLeft - the new upper-left point.
     */
    public void setUpperLeft(Point upperLeft) {
        this.upperLeft = upperLeft;
    }

    /**
     * Gets the width of the rectangle.
     *
     * @return The width value.
     */

    public double getWidth() {
        return this.width;
    }
    /**
     * Gets the height of the rectangle.
     *
     * @return The height value.
     */
    public double getHeight() {
        return this.height;
    }
    /**
     * Returns the closest point on this rectangle to the given point.
     * If the point is inside the rectangle, the same point is returned.
     *
     * @param p the point to find the closest point to (may be null)
     * @return a new geometry.Point that is the closest point on the rectangle (or null if p is null)
     */
    public Point getClosestPointOnRectangle(Point p) {
        if (p == null) {
            return null;
        }
        double minX = this.upperLeft.getX();
        double minY = this.upperLeft.getY();
        double maxX = minX + this.width;
        double maxY = minY + this.height;

        double closestX = Math.max(minX, Math.min(p.getX(), maxX));
        double closestY = Math.max(minY, Math.min(p.getY(), maxY));

        return new Point(closestX, closestY);
    }

    /**
     * Gets the edges of the rectangle as a list of lines.
     * @return A list containing the four edges of the rectangle.
     */
    public java.util.List<Line> getEdges() {
        java.util.List<Line> edges = new java.util.ArrayList<Line>();
        Point upperRight = new Point(this.getUpperLeft().getX() + this.getWidth(), this.getUpperLeft().getY());
        Point bottomLeft = new Point(this.getUpperLeft().getX(), this.getUpperLeft().getY() + this.getHeight());
        Point bottomRight = new Point(this.getUpperLeft().getX() + this.getWidth(),
                this.getUpperLeft().getY() + this.getHeight());
        // Top edge
        edges.add(new Line(this.getUpperLeft(), upperRight));
        // Right edge
        edges.add(new Line(upperRight, bottomRight));
        // Bottom edge
        edges.add(new Line(bottomRight, bottomLeft));
        // Left edge
        edges.add(new Line(bottomLeft, this.getUpperLeft()));
        return edges;
    }

    /**
     * Finds all intersection points between the rectangle and a given line.
     * @param line The line to check for intersections.
     * @return A list of intersection points.
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        java.util.List<Point> intersectionPoints = new java.util.ArrayList<Point>();
        for (Line edge : this.getEdges()) {
            Point intersection = line.intersectionWith(edge);
            if (intersection != null) {
                intersectionPoints.add(intersection);
            }
        }
        return intersectionPoints;
    }

}
