import biuoop.DrawSurface;
import java.awt.Color;

/**
 * A rectangular block in the game that can be collided with and drawn.
 *
 * <p>Blocks are both {@code Collidable} (they provide a collision rectangle and
 * a {@code hit} method) and {@code Sprite} (they know how to draw themselves
 * each frame).</p>
 */
public class Block implements Collidable, Sprite {
    private Rectangle rect;
    private Color color;

    /**
     * Constructs a Block with a rectangular shape and defines whether it acts as a frame.
     *
     * @param rect The rectangular boundary of the block.
     */
    public Block(Rectangle rect) {
        this.rect = rect;
    }

    /**
     * Constructs a Block with the specified position, size, and color.
     * @param upperLeft the upper-left point of the block
     * @param width the width of the block
     * @param height the height of the block
     * @param color the color of the block
     */
    public Block(Point upperLeft, double width, double height, Color color) {
        this.rect = new Rectangle(upperLeft, width, height);
        this.color = color;
    }

    /**
     * Copy constructor. Creates a deep copy of the given Block.
     *
     * @param other The Block to copy.
     */
    public Block(Block other) {
        this.rect = new Rectangle(other.rect.getUpperLeft(), other.rect.getWidth(), other.rect.getHeight());
        this.color = other.color;
    }
    /**
     * Sets the color of the block.
     *
     * @param color The new color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the block.
     *
     * @return The block's color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets a copy of the block's rectangular boundary.
     *
     * @return A new Rectangle object representing the boundary.
     */
    protected Rectangle getRect() {
        return new Rectangle(rect.getUpperLeft(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Sets the block's rectangular boundary.
     * @param rect the new rectangle
     */
    public void setRect(Rectangle rect) {
            this.rect = rect;
    }

    /**
     * Draw the block to the given surface using its color and an outline.
     *
     * @param surface the drawing surface
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(),
                (int) rect.getWidth(),   (int) rect.getHeight());
        surface.setColor(Color.BLACK);
        surface.drawRectangle((int) rect.getUpperLeft().getX(), (int) rect.getUpperLeft().getY(),
                (int) rect.getWidth(),   (int) rect.getHeight());
    }

    /**
     * Basic bounding-box overlap test between the ball and this block.
     *
     * @param center ball center point
     * @param r radius of the ball
     * @return true if overlapping/touching
     */
    public boolean isBallTouching(Point center, int r) {
        double minX = rect.getUpperLeft().getX();
        double minY = rect.getUpperLeft().getY();
        double maxX = minX + rect.getWidth();
        double maxY = minY + rect.getHeight();
        return (center.getX() + r > minX) && (center.getX() - r < maxX)
                && (center.getY() + r > minY) && (center.getY() - r < maxY);
    }

    /**
     * Check whether the ball is outside the block bounds (used for frames).
     *
     * @param center ball center
     * @param r radius
     * @return true if the ball is outside the block bounds
     */
    public boolean isBallOutdie(Point center, int r) {
        double minX = rect.getUpperLeft().getX();
        double minY = rect.getUpperLeft().getY();
        double maxX = minX + rect.getWidth();
        double maxY = minY + rect.getHeight();
        return (center.getX() + r > maxX || center.getX() - r < minX
                || center.getY() + r > maxY || center.getY() - r < minY);
    }

    /**
     * Handle a collision with this block. Returns the new velocity after impact.
     * The implementation flips velocity components depending on whether the
     * collision point lies on a vertical or horizontal edge (or both).
     *
     * @param collisionPoint collision location
     * @param currentVelocity incoming velocity
     * @return new velocity after collision
     */
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
        double minX = rect.getUpperLeft().getX();
        double maxX = minX + rect.getWidth();
        double minY = rect.getUpperLeft().getY();
        double maxY = minY + rect.getHeight();
        // Check which edges the collision point is on
        // Vertical edge check
        boolean flipX = (Math.abs(collisionPoint.getX() - minX) < Point.EPSILON)
                || (Math.abs(collisionPoint.getX() - maxX) < Point.EPSILON);
        // Horizontal edge check
        boolean flipY = (Math.abs(collisionPoint.getY() - minY) < Point.EPSILON)
                || (Math.abs(collisionPoint.getY() - maxY) < Point.EPSILON);
        // Handle corner case: if both flips are true, determine which axis to flip based on previous position
        if (flipX && flipY) {
            double prevX = collisionPoint.getX() - currentVelocity.getDx();
            double prevY = collisionPoint.getY() - currentVelocity.getDy();
            // Check if the previous position was within the X or Y range of the rectangle
            // Check X range
            boolean wasWithinXRange = (prevX > minX && prevX < maxX);
            // Check Y range
            boolean wasWithinYRange = (prevY > minY && prevY < maxY);
            if (wasWithinXRange) {
                flipX = false;
            } else if (wasWithinYRange) {
                flipY = false;
            }
        }
        double newDx = flipX ? -currentVelocity.getDx() : currentVelocity.getDx();
        double newDy = flipY ? -currentVelocity.getDy() : currentVelocity.getDy();
        return new Velocity(newDx, newDy);
    }

    /**
     * Get the collision rectangle of this block.
     * @return the collision rectangle
     */
    public Rectangle getCollisionRectangle() {
        return this.getRect();
    }

    /**
     * Notify the block that time has passed. (No action for static blocks.)
     */
    public void timePassed() {
        // No action needed for static blocks
    }

    /**
     * Add this block to the given game as both a Collidable and a Sprite.
     * @param g the game to add the block to
     */
    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }
}