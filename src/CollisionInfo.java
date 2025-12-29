/**
 * Small value object that holds collision details returned by the environment.
 */
public class CollisionInfo {

    private Point p;
    private Collidable object;

    /**
     * Create a CollisionInfo with the collision point and the collidable that was hit.
     *
     * @param collisionPoint the point of collision
     * @param collidableObject the collidable that was hit
     */
    public CollisionInfo(Point collisionPoint, Collidable collidableObject) {
        this.p = collisionPoint;
        this.object = collidableObject;
    }

    /**
     * The collision point.
     *
     * @return the point where the collision occurred
     */
    public Point collisionPoint() {
        return this.p;
    }

    /**
     * The collidable object that was hit.
     *
     * @return the collidable object
     */
    public Collidable collisionObject() {
        return this.object;
    }
}