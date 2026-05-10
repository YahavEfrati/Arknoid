package game;

import collidables.Collidable;
import collidables.CollisionInfo;
import geometry.Line;
import geometry.Point;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game environment, managing a collection of collidable objects.
 */
public class GameEnvironment {
    private List<Collidable> collidables;

    /**
     * Constructs a new Environment, initializing the list of collidable objects.
     */
    public GameEnvironment() {
        this.collidables = new ArrayList<>();
    }

    /**
     * Adds a collidable object to the environment.
     * If the object is a collidables.Block, a copy of it is created and added to ensure independence.
     *
     * @param c The collidables.Collidable object to add.
     */
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }
    /**
     * Remove a collidable form the collection.
     * @param c the collidable to remove.
     */
    public void removeCollidable(Collidable c) {
        this.collidables.remove(c);
    }

    /**
     * Gets the list of all collidable objects in the environment.
     *
     * @return The list of collidables.Collidable objects.
     */
    public List<Collidable> getCollidables() {
        return collidables;
    }

    /**
     * Determines the closest collision point along a given trajectory.
     * @param trajectory The line representing the trajectory of a moving object.
     * @return A collidables.CollisionInfo object containing the closest collision point and the collidable object,
     *         or null if there is no collision.
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        Point closestPoint = null;
        Collidable closestCollidable = null;
        List<Collidable> copyOfCollidables = new ArrayList<>(this.collidables);
        for (Collidable c : copyOfCollidables) {
            Point p = trajectory.closestIntersectionToStartOfLine(c.getCollisionRectangle());
            if (p != null) {
                if (closestPoint == null
                        || p.distance(trajectory.start()) < closestPoint.distance(trajectory.start())) {
                    closestPoint = p;
                    closestCollidable = c;
                }
            }
        }

        if (closestPoint == null) {
            return null;
        }
        return new CollisionInfo(closestPoint, closestCollidable);
    }
}