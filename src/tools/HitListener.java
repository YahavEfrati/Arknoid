package tools;

import collidables.Block;
import sprites.Ball;

/**
 * An interface for objects that want to be notified of hit events.
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     * @param beingHit - the block being hit.
     * @param hitter - the ball that hit the block.
     */
    void hitEvent(Block beingHit, Ball hitter);
}