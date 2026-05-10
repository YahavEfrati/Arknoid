package tools;

import collidables.Block;
import sprites.Ball;

/**
 * A Class that tracks the score. Increases the score when blocks are hit
 * and when all blocks are removed.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;
    private Counter remainingBlocks;

    /**
     * Constructor.
     * @param scoreCounter - the score counter.
     * @param remainingBlocks - the remaining blocks counter.
     */
    public ScoreTrackingListener(Counter scoreCounter, Counter remainingBlocks) {
        this.remainingBlocks = remainingBlocks;
        this.currentScore = scoreCounter;
    }

    /**
     * When a block is hit, increase the score by 5.
     * If this hit caused the removal of the last block and we clear a level,
     * add an additional 100 points.
     * @param beingHit - the block being hit.
     * @param hitter - the ball that hit the block.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        currentScore.increase(5);
        if (remainingBlocks.getValue() <= 0) {
           currentScore.increase(100);
        }
    }
}