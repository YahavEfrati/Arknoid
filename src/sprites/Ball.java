package sprites;

import biuoop.DrawSurface;
import collidables.Collidable;
import collidables.CollisionInfo;
import game.Game;
import game.GameEnvironment;
import geometry.Line;
import geometry.Point;
import geometry.Velocity;
import geometry.Rectangle;


import java.awt.Color;
import java.util.List;


/**
 * Represents a ball in the game world.
 *
 * <p>The ball has a center point, a radius and a velocity vector. It knows how to
 * draw itself and how to move one step while checking collisions with the game
 * environment.</p>
 */
public class Ball implements Sprite {
    /**
     * center point of the sprites.Ball.
     */
    private Point center;
    /**
     * the Radius of the ball - sprites.Ball Size.
     */
    private int r;
    /**
     * sprites.Ball color.
     */
    private Color color;
    /**
     * The velocity vector of the MovingBall.
     */
    private Velocity v;
    /**
     * The environment the ball moves in.
     */
    private GameEnvironment environment;
    //constructor

    /**
     * First constructor, with given point.
     *
     * @param center point of the sprites.Ball.
     * @param r      - the radius - the size of the sprites.Ball.
     * @param color  - the color of the sprites.Ball.
     */
    public Ball(Point center, int r, java.awt.Color color) {
        this.center = center;
        this.r = r;
        this.color = color;
        this.v = new Velocity(0, 0);
    }

    /**
     * Second constructor, with given x,y of the center.
     *
     * @param x     coordinate of the center fo the sprites.Ball.
     * @param y     coordinate of the center fo the sprites.Ball.
     * @param r     - the radius - the size of the sprites.Ball.
     * @param color - the color of the sprites.Ball.
     */
    public Ball(int x, int y, int r, java.awt.Color color) {
        this(new Point(x, y), r, color);
    }

    /**
     * First constructor, with given point, and speed.
     *
     * @param center point of the sprites.Ball.
     * @param r      - the radius - the size of the sprites.Ball.
     * @param color  - the color of the sprites.Ball.
     * @param v      - the speed vector.
     */
    public Ball(Point center, int r, java.awt.Color color, Velocity v) {
        this.center = center;
        this.r = r;
        this.color = color;
        this.v = v;
    }
    // accessors

    /**
     * Getter for x-coordinate.
     *
     * @return the x value from the center point.
     */
    public int getX() {
        return (int) center.getX();
    }

    /**
     * Getter for y-coordinate.
     *
     * @return the y value from the center point.
     */
    public int getY() {
        return (int) center.getY();
    }

    /**
     * Getter for center point.
     *
     * @return the center point.
     */
    public Point getCenter() {
        return new Point(getX(), getY());
    }

    /**
     * Getter for the sprites.Ball size - the radius.
     *
     * @return the r value.
     */
    public int getSize() {
        return r;
    }

    /**
     * Getter for the sprites.Ball color.
     *
     * @return the color.
     */
    public java.awt.Color getColor() {
        return color;
    }

    /**
     * Setter for the sprites.Ball color.
     * @param c the color.
     */
    public void setColor(Color c) {
        this.color = c;
    }

    /**
     * Seter for the sprites.Ball center - we need this for changing the sprites.Ball location.
     *
     * @param center - a new center point.
     */
    protected void setCenter(Point center) {
        this.center = center;
    }

    /**
     * Method to draw the sprites.Ball on the given DrawSurface.
     *
     * @param surface - the current screen we use.
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.getColor());
        surface.fillCircle(getX(), getY(), getSize());
        surface.setColor(Color.BLACK);
        surface.drawCircle(getX(), getY(), getSize());
    }


    /**
     * Set the game environment used for collision detection.
     *
     * @param environment the game.GameEnvironment instance
     */
    public void setGameEnvironment(GameEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Seter for velocity, by given new Vector.
     *
     * @param v - the new vector.
     */
    public void setVelocity(Velocity v) {
        this.v = new Velocity(v.getDx(), v.getDy());
    }

    /**
     * Seter for velocity, by given a new x,y vector coordinates.
     *
     * @param dx - delta x value.
     * @param dy - delta y value.
     */
    protected void setVelocity(double dx, double dy) {
        this.v = new Velocity(dx, dy);
    }

    /**
     * Geter for the geometry.Velocity vetor value.
     *
     * @return a new vector.
     */
    public Velocity getVelocity() {
        return new Velocity(v.getDx(), v.getDy());
    }

    /**
     * Move the ball one step according to its velocity, checking for collisions along
     * its trajectory. On collision the ball's center is repositioned exactly one radius
     * away from the collision point (so it just touches the collidable) and the
     * collidable's hit(...) is used to update the velocity.
     *
     * <p>This method avoids tunneling by considering the linear trajectory from the
     * current center to the next center given by velocity and asking the environment
     * for the closest collision along that segment.</p>
     */
    public void moveOneStep() {
        CollisionInfo stuckInfo = gotStuck();
        if (stuckInfo != null) {
            pushOutside(stuckInfo);
            return;
        }
        Line trajectory = new Line(this.getCenter(), this.getVelocity().applyToPoint(this.getCenter()));
        CollisionInfo collisionInfo = this.environment.getClosestCollision(trajectory);
        // No collision, move normally
        if (collisionInfo == null) {
            this.setCenter(this.getVelocity().applyToPoint(this.getCenter()));
        } else {
            // Move to just before the collision point
            Point collisionPoint = collisionInfo.collisionPoint();
            Velocity currentVelocity = this.getVelocity();
            double newX = collisionPoint.getX() - (Math.signum(currentVelocity.getDx()) * 2);
            double newY = collisionPoint.getY() - (Math.signum(currentVelocity.getDy()) * 2);
            this.setCenter(new Point(newX, newY));
            Collidable collidable = collisionInfo.collisionObject();
            Velocity newVelocity = collidable.hit(this, collisionPoint, currentVelocity);
            this.setVelocity(newVelocity);
        }
    }

    /**
     * Notify this ball that time has passed and advance it by one step.
     * This is called each frame by the game loop.
     */
    public void timePassed() {
        this.moveOneStep();
    }

    /**
     * Add this ball to the given game so it will be drawn and updated.
     *
     * @param g the game to add this ball to
     */
    public void addToGame(Game g) {
        g.addSprite(this);
    }

    /**
     * Checks if the ball is stuck inside any collidable object in the environment.
     *
     * @return collidables.CollisionInfo object containing information about the collidable
     * the ball is stuck in, or null if the ball is not stuck.
     */
    private CollisionInfo gotStuck() {
        List<Collidable> collidables = this.environment.getCollidables();
        for (Collidable c : collidables) {
            Rectangle rect = c.getCollisionRectangle();
            if (rect != null) {
                if (isPointInsideCollidable(this.getCenter(), new CollisionInfo(this.getCenter(), c))) {
                    return new CollisionInfo(this.getCenter(), c);
                }
            }
        }
        return null;
    }

    /**
     * Checks if a given point is inside a collidable object.
     *
     * @param center         The point representing the center of the ball.
     * @param collidableInfo collidables.CollisionInfo object containing information about the collidable.
     * @return true if the point is inside the collidable, false otherwise.
     */
    private boolean isPointInsideCollidable(Point center, CollisionInfo collidableInfo) {
        Rectangle rect = collidableInfo.collisionObject().getCollisionRectangle();
        if (rect == null) {
            return false;
        }
        double minX = rect.getUpperLeft().getX();
        double maxX = minX + rect.getWidth();
        double minY = rect.getUpperLeft().getY();
        double maxY = minY + rect.getHeight();
        return (maxX > center.getX()) && (minX < center.getX()) && (maxY > center.getY())
                && (minY < center.getY());
    }

    /**
     * Pushes the ball outside of the collidable object.
     * It checks 4 possible escape directions (Left, Right, Top, Bottom).
     * It selects the closest escape point that is also within the screen boundaries.
     *
     * @param collidableInfo Information about the object the ball is stuck inside.
     */
    private void pushOutside(CollisionInfo collidableInfo) {
        Rectangle rect = collidableInfo.collisionObject().getCollisionRectangle();
        double minX = rect.getUpperLeft().getX();
        double maxX = minX + rect.getWidth();
        double minY = rect.getUpperLeft().getY();
        double maxY = minY + rect.getHeight();

        double epsilon = 0.5; // Small buffer to ensure we are truly outside

        // 1. Define 4 possible escape points
        // We keep the original Y when escaping horizontally, and original X when escaping vertically
        Point[] candidates = new Point[4];
        candidates[0] = new Point(minX - epsilon, this.center.getY()); // Left
        candidates[1] = new Point(maxX + epsilon, this.center.getY()); // Right
        candidates[2] = new Point(this.center.getX(), minY - epsilon); // Top
        candidates[3] = new Point(this.center.getX(), maxY + epsilon); // Bottom

        Point bestPoint = null;
        double minDistance = Double.MAX_VALUE;

        // 2. Iterate over candidates to find the best valid one
        for (Point p : candidates) {
            if (isPointOnScreen(p)) {
                double d = this.center.distance(p);
                if (d < minDistance) {
                    minDistance = d;
                    bestPoint = p;
                }
            }
        }

        // 3. Apply the new position
        if (bestPoint != null) {
            this.setCenter(bestPoint);
        } else {
            // Fallback: If all points are invalid (extremely rare, e.g., block covers entire screen),
            // just move to the closest edge regardless of screen bounds to avoid infinite loop.
            // (Logic similar to previous version as a fail-safe)
            double distLeft = Math.abs(this.center.getX() - minX);
            double distRight = Math.abs(this.center.getX() - maxX);
            if (distLeft < distRight) {
                this.setCenter(new Point(minX - epsilon, this.center.getY()));
            } else {
                this.setCenter(new Point(maxX + epsilon, this.center.getY()));
            }
        }
    }

    /**
     * Checks if a point is within the screen boundaries.
     * Assumes screen starts at (0,0) and ends at (800, 600) - utilizing game.Game constants.
     *
     * @param p The point to check.
     * @return true if the point is on screen, false otherwise.
     */
    private boolean isPointOnScreen(Point p) {
        // We add a small margin (radius) so the ball doesn't get pushed partially into the wall
        // But for strict point checking, 0 to Width is enough.
        return p.getX() >= Game.BORDER_SIZE && p.getX() <= Game.SCREEN_WIDTH - Game.BORDER_SIZE
                && p.getY() >= Game.BORDER_SIZE && p.getY() <= Game.SCREEN_HEIGHT - Game.BORDER_SIZE;
    }

    /**
     * Removes this ball from the game.
     * @param g the game to remove the ball from.
     */
    public void removeFromGame(Game g) {
        g.removeSprite(this);
    }

}
