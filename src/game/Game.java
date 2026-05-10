package game;

import biuoop.DrawSurface;
import biuoop.Sleeper;
import biuoop.GUI;
import collidables.Block;
import collidables.Collidable;
import collidables.Paddle;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import sprites.Ball;
import sprites.ScoreIndicator;
import sprites.Sprite;
import sprites.SpriteCollection;
import tools.BallRemover;
import tools.BlockRemover;
import tools.Counter;
import tools.ScoreTrackingListener;

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

    private GUI gui = new GUI("game.Game", SCREEN_WIDTH, SCREEN_HEIGHT);
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter remainingBlocks;
    private Counter remainingBalls; // = new Counter(NUBMER_OF_BALLS);
    private Counter score; // = new Counter(0);
    private Random random = new Random();

    /**
     * The default screen dimensions defined (800x600).
     */
    //SCREEN DIMENSIONS AND BORDER SIZE
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int BORDER_SIZE = 20;
    //BALL CONFIGURATION
    private static final int NUBMER_OF_BALLS = 3;
    private static final int BALL_RADIUS = 4;
    //BLOCK CONFIGURATION
    private static final int NUM_OF_ROWS = 6;  //6
    private static final int NUM_OF_COLUMNS = 12;  //12
    private static final int BLOCK_WIDTH = 50;
    private static final int BLOCK_HEIGHT = 25;
    //Text To Display Configuration
    private static final int TEXT_SIZE = 25;


    /**
     * Add a collidable object to the game environment.
     *
     * @param c collidable to add.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Remove a collidable object from the game environment.
     * @param c collidable to remove.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
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
     * Remove a sprite from the game's sprite collection.
     * @param s sprite to remove.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Initialize a new game: create the Blocks and sprites.Ball (and collidables.Paddle)
     * and add them to the game. This must be called before calling {@link #run()}.
     */
    public void initialize() {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        addBallsToGame();
        createBorders();
        createScore();
        createPaddle();
        addObsetecalsToGame();
    }

    private void createScore() {
        score = new Counter(0);
        ScoreIndicator scoreIndicator = new ScoreIndicator(score);
        this.sprites.addSprite(scoreIndicator);
    }

    private void createBorders() {
        Block topBlock = new Block(new Rectangle(new Point(0, 0), SCREEN_WIDTH, BORDER_SIZE));
        topBlock.setColor(Color.GRAY);
        topBlock.addToGame(this);
        Block leftBlock = new Block(new Rectangle(new Point(0, BORDER_SIZE), BORDER_SIZE,
                SCREEN_HEIGHT - BORDER_SIZE));
        leftBlock.setColor(Color.GRAY);
        leftBlock.addToGame(this);
        Block rightBlock = new Block(new Rectangle(new Point(SCREEN_WIDTH - BORDER_SIZE, BORDER_SIZE),
                BORDER_SIZE, SCREEN_HEIGHT - BORDER_SIZE));
        rightBlock.setColor(Color.GRAY);
        rightBlock.addToGame(this);
        //DEATH BLOCK
        Block deathBlock = new Block(new Rectangle(new Point(0, SCREEN_HEIGHT),
                SCREEN_WIDTH, BORDER_SIZE));
        deathBlock.setColor(Color.GRAY);
        deathBlock.addHitListener(new BallRemover(this, remainingBalls));
        deathBlock.addToGame(this);
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
        remainingBalls = new Counter(NUBMER_OF_BALLS);
        Point center = getStartPoint();
        for (int i = 0; i < NUBMER_OF_BALLS; i++) {
            Ball ball = new Ball(center, BALL_RADIUS, Color.WHITE);
            ball.setVelocity(Velocity.fromAngleAndSpeed(45 + i * 30, 5));
            ball.setGameEnvironment(this.environment);
            ball.addToGame(this);
        }
    }
    /**
     * Generates a random starting point in the safe zone below the blocks.
     * It calculates exactly where the blocks end to avoid spawning inside them.
     *
     * @return A valid Point object.
     */
    private Point getStartPoint() {
        Random rand = new Random();
        // Generate X within the screen borders
        int x = rand.nextInt(SCREEN_WIDTH - 2 * BORDER_SIZE) + BORDER_SIZE;
        // Calculate the Safe Y Zone:
        int blocksStartY = BORDER_SIZE + BLOCK_WIDTH;
        int totalBlocksHeight = NUM_OF_ROWS * BLOCK_HEIGHT;
        // the Y coordinate where the lowest block ends
        int blocksEndY = blocksStartY + totalBlocksHeight;
        int safeMargin = 20;
        int minY = blocksEndY + safeMargin;
        //maximum Y is the middle of the screen
        int maxY = SCREEN_HEIGHT / 2;
        // In case the blocks take up more than half the screen,
        // we extend the spawning area downwards to ensure there is a valid range.
        if (maxY <= minY) {
            maxY = minY + 50;
        }
        // Generate Y randomly within the safe gap (between blocks and middle screen)
        int y = rand.nextInt(maxY - minY) + minY;
        return new Point(x, y);
    }

    /**
     * Add the rows/columns of blocks (obstacles) to the level.
     * This method arranges the blocks in rows with decreasing columns per row
     * and assigns a random color per row.
     */
    public void addObsetecalsToGame() {
        int startY = BORDER_SIZE + BLOCK_WIDTH;
        remainingBlocks = new Counter(numberOfBlocksInGame());
        BlockRemover remover = new BlockRemover(this, remainingBlocks);
        ScoreTrackingListener scoreTracking = new ScoreTrackingListener(score, remainingBlocks);
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            Color colorforRow = getRandColor();
            for (int j = 0; j < NUM_OF_COLUMNS - i; j++) {
                double x = (SCREEN_WIDTH - BORDER_SIZE) - ((j + 1) * BLOCK_WIDTH);
                double y = startY + (i * BLOCK_HEIGHT);
                Block block = new Block(new Rectangle(new Point(x, y), BLOCK_WIDTH, BLOCK_HEIGHT));
                block.setColor(colorforRow);
                block.addHitListener(remover);
                block.addHitListener(scoreTracking);
                block.addToGame(this);

            }
        }
    }

    /**
     * Helper method to count how much blocks we start with.
     * @return number of blocks
     */
    private int numberOfBlocksInGame() {
        int counter = 0;
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLUMNS - i; j++) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * get random color.
     * @return random color.
     */
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
            if (remainingBlocks.getValue() <= 0 || remainingBalls.getValue() <= 0) {
                break;
            }
        }
        endGame();
    }

    /**
     * End the game and display the final score.
     */
    public void endGame() {
        DrawSurface d = this.gui.getDrawSurface();
        d.setColor(Color.WHITE);
        d.fillRectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        String msg = "Game Over.";
        if (remainingBlocks.getValue() <= 0) {
            msg = "You Win!";
        }
        d.setColor(Color.BLACK);
        d.drawText(SCREEN_HEIGHT / 2, SCREEN_WIDTH - SCREEN_HEIGHT, msg, TEXT_SIZE * 2);
        d.drawText(SCREEN_HEIGHT / 2, SCREEN_HEIGHT / 2, "Your Score: " + score.getValue(), TEXT_SIZE);
        this.gui.show(d);
        //Print to the console as well by the Assingment instructors.
        System.out.println(msg);
        System.out.println("Your Score: " + score.getValue());
        //Wait 8 second and Close the Game.
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        sleeper.sleepFor(8000);
        gui.close();
    }
}
