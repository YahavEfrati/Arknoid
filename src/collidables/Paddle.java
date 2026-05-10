package collidables;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import sprites.Ball;
import sprites.Sprite;
import game.Game;

import java.awt.Color;

/**
 * The player's paddle: a controllable block that bounces the ball.
 *
 * <p>The paddle moves left/right based on keyboard input and changes the
 * outgoing angle depending on where the ball hits the top surface.</p>
 */
public class Paddle implements Sprite, Collidable {  //
    private biuoop.KeyboardSensor keyboard;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 10;
    private static final int PADDLE_X_OFFSET = 30;
    private static final int PADDLE_SPEED = 5;
    public static final Point PADDLE_STARTING_POINT = new Point(350, 570);

    private Rectangle rect;
    private Color color;

    /**
     * Create a paddle at the given rectangle and controlled by the supplied keyboard sensor.
     *
     * @param rect paddle bounds
     * @param keyboard keyboard sensor used for left/right input
     */
    public Paddle(Rectangle rect, biuoop.KeyboardSensor keyboard) {
        this.rect = rect;
        this.keyboard = keyboard;
    }
    /**
     * Geter for rectangle.
     * @return rect filed.
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Setter for rectangle.
     * @param rect - new rect.
     */
    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    /**
     * Get the next left x-coordinate.
     * @return left x-coordinate.
     */
    private double getNextLeftX() {
        double currentX = getRect().getUpperLeft().getX();
        double newX = currentX - PADDLE_SPEED;
        if (newX < PADDLE_X_OFFSET) {
            newX = PADDLE_X_OFFSET;
        }
        return newX;
    }
    /**
     * Get the next right x-coordinate.
     * @return right x-coordinate.
     */
    private double getNextRightX() {
        double currentX = getRect().getUpperLeft().getX();
        double newX = currentX + PADDLE_SPEED;
        if (newX + getRect().getWidth() > 800 - PADDLE_X_OFFSET) {
            newX = 800 - PADDLE_X_OFFSET - getRect().getWidth();
        }
        return newX;
    }

    /**
     * Move the paddle left by the configured speed; wraps around screen edges.
     */
    public void moveLeft() {
        Rectangle rect = getRect();
        double newX = rect.getUpperLeft().getX() - PADDLE_SPEED;
        if (newX + rect.getWidth() < PADDLE_X_OFFSET) {
            newX = Game.SCREEN_WIDTH - PADDLE_X_OFFSET;
        }
        rect.setUpperLeft(new Point(newX, rect.getUpperLeft().getY()));
        this.setRect(rect);
    }
    /**
     * Move the paddle right by the configured speed; wraps around screen edges.
     */
    public void moveRight() {
        Rectangle rect = getRect();
        double newX = rect.getUpperLeft().getX() + PADDLE_SPEED;
        if (newX > Game.SCREEN_WIDTH - PADDLE_X_OFFSET) {
            newX = -rect.getWidth() + PADDLE_X_OFFSET;
        }
        rect.setUpperLeft(new Point(newX, rect.getUpperLeft().getY()));
        this.setRect(rect);
    }

    /**
     * Notify the paddle that time has passed; move it left/right if the corresponding keys are pressed.
     */
    @Override
    public void timePassed() {
        biuoop.KeyboardSensor keyboard = this.keyboard;
        if (keyboard.isPressed(biuoop.KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else if (keyboard.isPressed(biuoop.KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    /**
     * Get the collision rectangle of the paddle, considering its potential movement
     * during the current time step.
     * @return the collision rectangle.
     */
    @Override
    public Rectangle getCollisionRectangle() {
        Rectangle currentRect = getRect();
        double currentX = currentRect.getUpperLeft().getX();
        double y = currentRect.getUpperLeft().getY();
        double width = currentRect.getWidth();
        double height = currentRect.getHeight();

        double startX = currentX;
        double endX = currentX + width;

        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            double nextX = getNextLeftX();
            startX = Math.min(currentX, nextX);
        } else if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            double nextX = getNextRightX();
            endX = Math.max(currentX + width, nextX + width);
        }
        return new Rectangle(new Point(startX, y), endX - startX, height);
    }

    /**
     * Notify this paddle that it has been hit by a ball at the given collision point
     * with the given current velocity. The paddle changes the ball's velocity depending
     * on where it hit the paddle.
     * @param hitter  the ball who hits.
     * @param collisionPoint  the point where the collision occurred
     * @param currentVelocity the incoming velocity of the object that hit this collidable
     * @return
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        Rectangle rect = getRect();
        double paddleY = rect.getUpperLeft().getY();
        if (Math.abs(collisionPoint.getY() - paddleY) <= Point.EPSILON) {
            double sectionSize = rect.getWidth() / 5;
            double hitXLocation = collisionPoint.getX() - rect.getUpperLeft().getX();
            double speed = currentVelocity.getSpeed();

            if (hitXLocation < 0) {
                hitXLocation = 0;
            }
            if (hitXLocation > rect.getWidth()) {
                hitXLocation = rect.getWidth() - 0.1;
            }

            if (hitXLocation < sectionSize) { // FIRST SECTION
                return Velocity.fromAngleAndSpeed(300, speed);
            } else if (hitXLocation < sectionSize * 2) { // SECOND SECTION
                return Velocity.fromAngleAndSpeed(330, speed);
            } else if (hitXLocation < sectionSize * 3) { // THIRD SECTION
                return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
            } else if (hitXLocation < sectionSize * 4) { //FORTH SECTION
                return Velocity.fromAngleAndSpeed(30, speed);
            } else { //FIFTH SECTION
                return Velocity.fromAngleAndSpeed(60, speed);
            }
        } else { // Else - behave like a normal block.
            return new Velocity(-currentVelocity.getDx(), currentVelocity.getDy());
        }
    }
    /**
     * Add this paddle to the given game as both a collidables.Collidable and a sprites.Sprite.
     * @param g the game to add the paddle to
     */
    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }
    /**
     * Sets the color of the paddle.
     *
     * @param color The new color.
     */
    public void setColor(Color color) {
        this.color = color;
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
}
