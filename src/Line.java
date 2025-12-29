/**
 * This class is for represnting a Line in space. A line is made up with two given diffrent points.
 * This class contains various operations on a line such as intersecting,
 * finding points between 2 lines, and more.
 * For all of the logic behind the lines, whe use equation for non-vertical lines ==> y = mx +b.
 * when m=slope , b =  y-intercept of the line.
 */
public class Line {
    /**
     * starting point of the line.
     */
    private Point start;
    /**
     * ending point of the line.
     */
    private Point end;

    /**
     * Constructors method by two given points.
     *
     * @param start point of line
     * @param end   point of line
     */
    public Line(Point start, Point end) {
        //Line must be from 2 diffrent points
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
    }

    /**
     * Constructor method by 2 sets of coordinates.
     *
     * @param x1 - x-coordinate of point 1.
     * @param y1 - y-coordinate of point 1.
     * @param x2 - x-coordinate of point 2.
     * @param y2 - y-coordinate of point 2.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    /**
     * Length measure Method.
     *
     * @return the length of the line.
     */
    public double length() {
        return start.distance(end);
    }

    /**
     * Method for geting the middle point of the.
     *
     * @return the middle point of the line.
     */
    public Point middle() {
        return new Point((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
    }

    /**
     * Geter for the start point.
     *
     * @return the start point of the line.
     */
    public Point start() {
        return new Point(start.getX(), start.getY());
    }

    /**
     * Geter for end point.
     *
     * @return the end point of the line.
     */
    public Point end() {
        return new Point(end.getX(), end.getY());
    }

    /**
     * Method to check if there is a intersection point between two lines.
     * We check few cases. vertical lines, parallel lines, shared start/end points lines,
     * and lines that have collinear intersection..
     *
     * @param other - the second line.
     * @return true if the lines intersect, false otherwise.
     */
    public boolean isIntersecting(Line other) {
        if (other == null) {
            return false;
        }
        Point intersectionPoint;
        //Case of the two lines are vertical and we cant use the formula.
        if (this.isVertical() && other.isVertical()) {
            //the lines are vertical and have diffrent x-values ==> parallel.
            if (Math.abs(this.start.getX() - other.start.getX()) > Point.EPSILON) {
                return false;
            }
            //when they share the same x-value, we need to check for subset.
            return this.isPointInRange(other.start) || this.isPointInRange(other.end)
                    || other.isPointInRange(this.start) || other.isPointInRange(this.end);
        }
        //Case of just one of the lines is vertical.
        if (this.isVertical() || other.isVertical()) {
            if (this.isVertical()) {
                intersectionPoint = other.getIntersectionWithVertical(this);
            } else {
                intersectionPoint = this.getIntersectionWithVertical(other);
            }
        } else {
            //Case of two regular lines. here we need to see if the two lines are parallel.
            double m1 = this.getSlope();
            double b1 = this.getB();
            double m2 = other.getSlope();
            double b2 = other.getB();
            //cheak if the two lines parallel
            if (Math.abs(m2 - m1) < Point.EPSILON) {
                if (Math.abs(b2 - b1) > Point.EPSILON) {
                    return false;
                }
                //if the lines have the same b we want to check if one is subset in another.
                return this.isPointInRange(other.start) || this.isPointInRange(other.end)
                        || other.isPointInRange(this.start) || other.isPointInRange(this.end);
            }
            //two non vertical non parallel lines.
            intersectionPoint = this.getIntersectionPoint(other);
        }
        //Cheking if the intersection point is on the Lines.
        return (this.isPointInRange(intersectionPoint) && other.isPointInRange(intersectionPoint));
    }

    /**
     * Method to check if there is a interscection point between three lines.
     *
     * @param other1 - the second line.
     * @param other2 - the third line.
     * @return true if this 2 lines intersect with this line, false otherwise.
     */
    public boolean isIntersecting(Line other1, Line other2) {
        if (other1 == null || other2 == null) {
            return false;
        }
        return (this.isIntersecting(other1) && this.isIntersecting(other2));
    }

    /**
     * This is the method that gets the intersection point between two lines.
     * Returns the point if and only if there is a single intersection point.
     * Returns null if the lines are parallel, overlapping (infinite intersections),
     * or do not have any intersection point.
     *
     * @param other
     * @return the intersection point if the lines intersect, and null otherwise.
     */
    public Point intersectionWith(Line other) {
        if (other == null) {
            return null;
        }
        Point intersectionPoint;
        //Case of two vertical lines.
        if (this.isVertical() && other.isVertical()) {
            if (Math.abs(this.start.getX() - other.start.getX()) > Point.EPSILON) {
                return null;
            }
            //if they got the same x-value, we check if there is only single intersection point.
            return this.getCoolinearInersection(other);
        }
        //Case of one of them is vertical
        if (this.isVertical()) {
            intersectionPoint = other.getIntersectionWithVertical(this);
        } else if (other.isVertical()) {
            intersectionPoint = this.getIntersectionWithVertical(other);
        } else {
            //Non vertical cases
            double m1 = this.getSlope();
            double b1 = this.getB();
            double m2 = other.getSlope();
            double b2 = other.getB();
            //Case of they are parallel.
            if (Math.abs(m1 - m2) < Point.EPSILON) {
                if (Math.abs(b1 - b2) > Point.EPSILON) {
                    return null;
                }
                //if they got the same b-values, we check if there is only single intersection.
                return this.getCoolinearInersection(other);
            }
            //Case of two regular lines (non parallel, non vertical).
            intersectionPoint = this.getIntersectionPoint(other);
        }
        //checks if the intersection point that we found is on both lines.
        if (this.isPointInRange(intersectionPoint) && other.isPointInRange(intersectionPoint)) {
            return intersectionPoint;
        } else {
            return null;
        }
    }

    /**
     * Method to check if thw lines are equal.
     *
     * @param other - the second line.
     * @return true if the lines are equal, false otherwise.
     */
    public boolean equals(Line other) {
        if (other == null) {
            return false;
        }
        return ((start.equals(other.start) && end.equals(other.end))
                || (start.equals(other.end) && end.equals(other.start)));
    }

    /**
     * Method to calculate a Slope of a line.
     * with to given points on the line, the equation is m = (y1-y2)/(x1-x2).
     *
     * @return the Slope of the line.
     */
    public double getSlope() {
        return ((end.getY() - start.getY()) / (end.getX() - start.getX()));
    }

    /**
     * Method to calculate the y-intercept point of a line, who represented by b in the equation.
     * we are doing here ==> b = y - mx
     *
     * @return the b in y=mx+b
     */
    public double getB() {
        return (start.getY() - (getSlope() * start.getX()));
    }

    /**
     * Hellper method for intersectionWith and isIntersecting.
     * checks if a line is vertical.
     *
     * @return true if it vertical, false otherwise.
     */
    public boolean isVertical() {
        return Math.abs(start.getX() - end.getX()) < Point.EPSILON;
    }
    /**
     * Hellper method for intersectionWith and isIntersecting.
     * checks if a line is horizontal.
     *
     * @return true if it horizontal, false otherwise.
     */
    public boolean isHorizontal() {
        return Math.abs(start.getY() - end.getY()) < Point.EPSILON;
    }

    /**
     * Hellper method for intersectionWith and isIntersecting.
     * cheaks if a given point is in range of a line.
     * in range - if the x,y values are between the start,end point values of the line.
     *
     * @param point - to check if in range.
     * @return true if is range, false otherwise.
     */
   //  Original implementation (kept for reference) - checks range using products. */

        private boolean isPointInRange(Point point) {
            double currentX = point.getX();
            double currentY = point.getY();
            return (((currentX - start.getX()) * (currentX - end.getX()) <= 0)
                    && ((currentY - start.getY()) * (currentY - end.getY()) <= 0));
        }




    /**
     * Hellper method for intersectionWith.
     * gets intersection point between to lines, assuming both are infinite,
     * and non vertical.
     *
     * @param other the second line.
     * @return the intersection point.
     */
    private Point getIntersectionPoint(Line other) {
        double x, y;
        x = (other.getB() - this.getB()) / (this.getSlope() - other.getSlope());
        y = (this.getSlope() * x) + this.getB();
        return new Point(x, y);
    }

    /**
     * Hellper method for intersectionWith.
     * gets intersection point between to lines, assuming both are infinite,
     * and one of them are vertical.
     *
     * @param verticalLine the second line.
     * @return the intersection point.
     */
    private Point getIntersectionWithVertical(Line verticalLine) {
        double m = this.getSlope();
        double x = verticalLine.start.getX();
        double b = this.getB();
        double y = (m * x) + b;
        return new Point(x, y);
    }

    /**
     * Helper method for intersectionWith.
     * Checks coolinear lines and returns a point only if they touch once,
     * at a single point, and they dont overlap.
     *
     * @param other - the second line.
     * @return the single shared point, or null otherwise
     */
    private Point getCoolinearInersection(Line other) {
        Point shared = this.linesShareEndStartPoint(other);
        if (shared == null) {
            return null;
        }
        //we gets the other start and end points that for fact the two lines dont share.
        Point p1 = this.start.equals((shared)) ? this.end() : this.start();
        Point p2 = other.start.equals(shared) ? other.end() : other.start();
        //for those points, if the in the range of the second line, the lines overlaping,
        //and the lines have infinite intersection points.
        if (this.isPointInRange(p2) || other.isPointInRange(p1)) {
            return null;
        }
        return shared;
    }

    /**
     * Hellper method for getCoolinearInersection.
     * Checks if two lines share a start/end point.
     * we mostly use it on two parallel or vertical lines.
     *
     * @param other -the second line.
     * @return the line they have in common, otherwise null.
     */
    private Point linesShareEndStartPoint(Line other) {
        if (this.start.equals(other.end) || this.start.equals(other.start)) {
            return this.start();
        } else if (this.end.equals(other.start) || this.end.equals(other.end)) {
            return this.end();
        } else {
            return null;
        }
    }

    /**
     * Find the closest intersection point between this line (starting point)
     * and the given rectangle's edges.
     *
     * @param rect the rectangle to test against
     * @return the closest intersection Point to the start of this line, or null if none
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        java.util.List<Point> points = rect.intersectionPoints(this);

        if (points.isEmpty()) {
            return null;
        }

        Point closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Point p : points) {
            if (p != null) {
                double currDist = this.start.distance(p);
                if (currDist < minDistance) {
                    minDistance = currDist;
                    closest = p;
                }
            }
        }
        return closest;
    }

//    /**
//     * Check whether a point lies on this (finite) line segment.
//     *
//     * @param p the point to test
//     * @return true if the point lies on the segment (including endpoints)
//     */
//    public boolean isPointOnLine(Point p) {
//        return this.isPointInRange(p) && (this.isVertical()
//                || Math.abs(p.getY() - (this.getSlope() * p.getX() + this.getB())) < Point.EPSILON);
//    }

}