/**
 * The player's paddle: a controllable block that bounces the ball.
 *
 * <p>The paddle moves left/right based on keyboard input and changes the
 * outgoing angle depending on where the ball hits the top surface.</p>
 */
public class Paddle extends Block implements Sprite, Collidable {  //
    private biuoop.KeyboardSensor keyboard;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 10;
    public static final int PADDLE_X_OFFSET = 30;
    public static final int PADDLE_SPEED = 5;
    public static final Point PADDLE_STARTING_POINT = new Point(350, 570);

    /**
     * Create a paddle at the given rectangle and controlled by the supplied keyboard sensor.
     *
     * @param rect paddle bounds
     * @param keyboard keyboard sensor used for left/right input
     */
    public Paddle(Rectangle rect, biuoop.KeyboardSensor keyboard) {
        super(rect);
        this.keyboard = keyboard;
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
    // Sprite
    @Override
    public void timePassed() {
        biuoop.KeyboardSensor keyboard = this.keyboard;
        if (keyboard.isPressed(biuoop.KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else if (keyboard.isPressed(biuoop.KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    @Override
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
        Rectangle rect = getRect();
        double paddleY = rect.getUpperLeft().getY();
        if (Math.abs(collisionPoint.getY() - paddleY) <= Point.EPSILON) {
            double sectionSize = rect.getWidth() / 5;
            double hitXLocation = collisionPoint.getX() - rect.getUpperLeft().getX();
            double speed = currentVelocity.getSpeed();

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
            return super.hit(collisionPoint, currentVelocity);
        }
    }
}
