package tools;

import collidables.Block;
import game.Game;
import sprites.Ball;

/**
 * BlockRemover is in charge of removing blocks from the game, as well as keeping count
 * of the number of blocks that remain.
 */
public class BlockRemover implements HitListener {
    private Game game;
    private Counter remainingBlocks;

    /**
     * Costructor for BlockRemover.
     * @param game - current game.
     * @param remainingBlocks - counter.
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    /**
     * When a block is hit, it is removed from the game, the hitter ball changes color to the block's color,
     * and the remaining blocks counter is decreased by one.
     * @param beingHit the block being hit.
     * @param hitter the ball that hit the block.
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(game);
        hitter.setColor(beingHit.getColor());
        remainingBlocks.decrease(1);
        beingHit.removeHitListener(this);
    }
}