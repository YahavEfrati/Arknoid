package sprites;

import biuoop.DrawSurface;

/**
 * A drawable and updatable object in the game.
 *
 * <p>Sprites are responsible for drawing themselves and updating their
 * internal state each frame. Typical implementations include {@code sprites.Ball},
 * {@code collidables.Block} and {@code collidables.Paddle}.</p>
 */
public interface Sprite {
    /**
     * Draw this sprite on the provided drawing surface.
     *
     * @param d the drawing surface to draw onto
     */
    void drawOn(DrawSurface d);

    /**
     * Notify the sprite that time has passed; used to advance animation or
     * movement by one frame.
     */
    void timePassed();
}