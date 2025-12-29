import biuoop.DrawSurface;
import biuoop.Sleeper;
import biuoop.GUI;

import java.awt.Color;
import java.util.Random;

/**
 * Utility class for game helper methods, such as ball creation and movement parameters.
 *
 * <p>This class is the central game container: it holds the GUI, the sprite
 * collection and the environment (collidables). It is responsible for creating
 * the initial level geometry (borders, paddle, blocks) and running the main
 * animation loop.</p>
 */
public class Game {

    private GUI gui = new GUI("Game", SCREEN_WIDTH, SCREEN_HEIGHT);
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Random random = new Random();
    /**
     * The default screen dimensions defined (800x600).
     */
    //SCREEN DIMENSIONS AND BORDER SIZE
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int BORDER_SIZE = 20;
    //BALL CONFIGURATION
    public static final int NUBMER_OF_BALLS = 2;
    public static final int BALL_RADIUS = 4;
    //BLOCK CONFIGURATION
    public static final int NUM_OF_ROWS = 6;
    public static final int NUM_OF_COLUMNS = 12;
    public static final int BLOCK_WIDTH = 50;
    public static final int BLOCK_HEIGHT = 25;

    /**
     * Add a collidable object to the game environment.
     *
     * @param c collidable to add
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Add a sprite to the game's sprite collection so it will be updated and drawn.
     *
     * @param s sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Initialize a new game: create the Blocks and Ball (and Paddle)
     * and add them to the game. This must be called before calling {@link #run()}.
     */
    public void initialize() {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        createBorders();
        createPaddle();
        addObsetecalsToGame();
        addBallsToGame();
    }

    private void createBorders() {

        Block topBlock = new Block(new Rectangle(new Point(0, 0), SCREEN_WIDTH, BORDER_SIZE));
        topBlock.setColor(Color.GRAY);
        topBlock.addToGame(this);
        Block bottomBlock = new Block(new Rectangle(new Point(0, SCREEN_HEIGHT - BORDER_SIZE),
                SCREEN_WIDTH, BORDER_SIZE));
        bottomBlock.setColor(Color.GRAY);
        bottomBlock.addToGame(this);
        Block leftBlock = new Block(new Rectangle(new Point(0, BORDER_SIZE), BORDER_SIZE,
                SCREEN_HEIGHT - 2 * BORDER_SIZE));
        leftBlock.setColor(Color.GRAY);
        leftBlock.addToGame(this);
        Block rightBlock = new Block(new Rectangle(new Point(SCREEN_WIDTH - BORDER_SIZE, BORDER_SIZE),
                BORDER_SIZE, SCREEN_HEIGHT - 2 * BORDER_SIZE));
        rightBlock.setColor(Color.GRAY);
        rightBlock.addToGame(this);
    }

    private void createPaddle() {
        Paddle paddle = new Paddle(new Rectangle(Paddle.PADDLE_STARTING_POINT, Paddle.PADDLE_WIDTH,
                Paddle.PADDLE_HEIGHT), this.gui.getKeyboardSensor());
        paddle.setColor(Color.ORANGE);
        paddle.addToGame(this);
    }

    /**
     * Create and add the configured number of balls to the game.
     * Each ball is placed using a random start point and given an initial velocity.
     */
    public void addBallsToGame() {
        for (int i = 0; i < NUBMER_OF_BALLS; i++) {
            Point center = getStartPoint();
            Ball ball = new Ball(center, BALL_RADIUS, Color.WHITE);
            ball.setVelocity(Velocity.fromAngleAndSpeed(45 + i * 30, 5));
            ball.setGameEnvironment(this.environment);
            ball.addToGame(this);
        }
    }
    /**
     * Generates a random starting point for the ball in the upper half of the screen.
     * Ensures the point is within the screen boundaries and does not overlap with any existing blocks.
     *
     * @return A valid Point object in the upper half of the screen.
     */
    private Point getStartPoint() {
        Random rand = new Random();
        boolean isInsideBlock;
        Point p;
        do {
            isInsideBlock = false;
            // Generate X within the screen borders
            int x = rand.nextInt(SCREEN_WIDTH - 2 * BORDER_SIZE) + BORDER_SIZE;
            // Generate Y only in the UPPER HALF of the screen (excluding border)
            int y = rand.nextInt((SCREEN_HEIGHT / 2) - BORDER_SIZE) + BORDER_SIZE;
            p = new Point(x, y);
            // Check if the generated point is inside any existing collidable (Block/Paddle)
            for (Collidable c : this.environment.getCollidables()) {
                Rectangle rect = c.getCollisionRectangle();
                double minX = rect.getUpperLeft().getX();
                double maxX = minX + rect.getWidth();
                double minY = rect.getUpperLeft().getY();
                double maxY = minY + rect.getHeight();
                // Check if point (x,y) is strictly inside the rectangle bounds
                if (p.getX() >= minX && p.getX() <= maxX && p.getY() >= minY && p.getY() <= maxY) {
                    isInsideBlock = true;
                    break; // Found a collision, need to regenerate
                }
            }
        } while (isInsideBlock); // Repeat until a valid point is found
        return p;
    }

    /**
     * Add the rows/columns of blocks (obstacles) to the level.
     * This method arranges the blocks in rows with decreasing columns per row
     * and assigns a random color per row.
     */
    public void addObsetecalsToGame() {
        int startY = BORDER_SIZE + BLOCK_WIDTH;

        for (int i = 0; i < NUM_OF_ROWS; i++) {
            Color colorforRow = getRandColor();
            for (int j = 0; j < NUM_OF_COLUMNS - i; j++) {
                double x = (SCREEN_WIDTH - BORDER_SIZE) - ((j + 1) * BLOCK_WIDTH);
                double y = startY + (i * BLOCK_HEIGHT);
                Block block = new Block(new Rectangle(new Point(x, y), BLOCK_WIDTH, BLOCK_HEIGHT));
                block.setColor(colorforRow);
                block.addToGame(this);
            }
        }
    }

    private Color getRandColor() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r, g, b);
    }

    /**
     * Run the game -- start the animation loop. This method blocks and will
     * continuously draw frames until the program is stopped.
     */
    public void run() {
        // code to run the game
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;
        while (true) {
            long startTime = System.currentTimeMillis(); // timing
            DrawSurface d = gui.getDrawSurface();
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            // timing
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}
