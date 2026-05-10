package sprites;

import biuoop.DrawSurface;
import tools.Counter;

import java.awt.Color;

/** A ScoreIndicator class.
 * A sprite that shows the current score on the screen.
 */
public class ScoreIndicator implements Sprite {
    private Counter scroe;
    private static int scorePositionX = 350;
    private static int scorePositionY = 15;
    private static int fontSize = 16;

    /**
     * Constructor.
     * @param score - the score counter.
     */
    public ScoreIndicator(Counter score) {
        this.scroe = score;
    }

    /**
     * Draw the score on the given DrawSurface.
     * @param d the drawing surface to draw onto
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.BLACK);
        d.drawText(scorePositionX, scorePositionY, "SCORE: " + scroe.getValue(), fontSize);
    }

    /**
     * Notify the sprite that time has passed.
     */
    @Override
    public void timePassed() {
        //No action for text.
    }
}
