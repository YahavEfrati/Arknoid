package sprites;

import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection that manages all sprites in the game.
 *
 * <p>Provides simple methods to add sprites, notify them that time passed,
 * and draw them on a surface. This class only modifies documentation and
 * comments — no behavioral changes.</p>
 */
public class SpriteCollection {
    private List<Sprite> sprites;

    /**
     * Create an empty sprites.SpriteCollection.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<>();
    }

    /**
     * Add a sprite to the collection so it will be drawn and updated.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }

    /**
     * Remove a sprite form the collection.
     * @param s the sprite to remove.
     */
    public void removeSprite(Sprite s) {
        this.sprites.remove(s);
    }

    /**
     * Notify all sprites that time has passed so they can update their state.
     */
    public void notifyAllTimePassed() {
        List<Sprite> spritesCopy = new ArrayList<>(this.sprites);
        for (Sprite s : spritesCopy) {
            s.timePassed();
        }
    }

    /**
     * Draw all sprites on the provided drawing surface.
     *
     * @param d the drawing surface
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite s : this.sprites) {
            s.drawOn(d);
        }
    }
}