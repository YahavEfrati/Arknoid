package collidables;

import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import sprites.Ball;

/**
 * Interface for objects that can be collided with in the game.
 *
 * <p>collidables.Collidable objects provide their collision rectangle and handle being hit
 * by a moving object. Implementations decide how to respond to hits and
 * what new velocity should be returned.</p>
 */
public interface Collidable {
    /**
     * Return the collision rectangle of the object.
     *
     * @return the geometry.Rectangle that defines the collision bounds
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify the object that it was hit at the given point with the provided velocity.
     * Implementations should compute and return the new velocity that should be used
     * after the collision.
     *
     * @param hitter  the ball who hits.
     * @param collisionPoint  the point where the collision occurred
     * @param currentVelocity the incoming velocity of the object that hit this collidable
     * @return the new velocity after the collision
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
