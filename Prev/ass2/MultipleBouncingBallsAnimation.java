import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;

/**
 * Class for Task-3.
 * Making multiple bouncing balls on the screen by the user input, in the command line.
 */
public class MultipleBouncingBallsAnimation {
    /**
     * A method for drawing balls on the screen in random colors and positions,
     * according to the sizes entered by the user.
     *
     * @param args - Balls sizes by command line.
     */

    private static void drawAnimation(String[] args) {
        Environment environment = new Environment();
        environment.addCollidable(GameUtils.SCREEN);
        GUI gui = new GUI("title", (int) GameUtils.SCREEN.getRect().getWidth(),
                (int) GameUtils.SCREEN.getRect().getHeight());
        Ball[] balls = new Ball[args.length];
        Ball tempBall = new Ball(0, 0, 0, Color.red);
        for (int i = 0; i < args.length; i++) {
            balls[i] = GameUtils.createNewBallBySize(Integer.parseInt(args[i]), environment);
        }
        Sleeper sleeper = new Sleeper();
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            for (Ball ball : balls) {
                ball.updateBallAndMove(environment);
                ball.drawOn(d);
            }
            gui.show(d);
            sleeper.sleepFor(50);  // wait for 50 milliseconds.
        }
    }




    /**
     * main.
     *
     * @param args - the balls size by the user input.
     */
    public static void main(String[] args) {
        drawAnimation(args);
    }
}


