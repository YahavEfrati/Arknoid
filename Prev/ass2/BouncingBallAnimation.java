import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;

/**
 * Class for Task-2.
 * Getting form the command line the starting point and the speed for the ball,
 * And showing a black ball by the arguments.
 */
public class BouncingBallAnimation {
    /**
     * Drawing a black ball on the screen by the user information.
     *
     * @param start - starting center point of the ball.
     * @param dx    - delta x for the ball speed.
     * @param dy    - delta y for the ball speed.
     */
    private static void drawAnimation(Point start, double dx, double dy) {
        GUI gui = new GUI("title", 800, 600);
        Sleeper sleeper = new Sleeper();
        Environment environment = new Environment();
        environment.addCollidable(GameUtils.SCREEN);
        Ball ball = new Ball((int) start.getX(), (int) start.getY(), 30, Color.BLACK);
        ball.setVelocity(dx, dy);
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            ball.updateBallAndMove(environment);
            ball.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(50);  // wait for 50 milliseconds.
        }
    }

    /**
     * main.
     *
     * @param args arguments form command line.
     */
    public static void main(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double dx = Double.parseDouble(args[2]);
        double dy = Double.parseDouble(args[3]);
        drawAnimation(new Point(x, y), dx, dy);
    }
}
