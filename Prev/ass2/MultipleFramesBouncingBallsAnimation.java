import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;

/**
 * Class for Task-4.
 * Draws several different balls on the screen at different frames.
 */
public class MultipleFramesBouncingBallsAnimation {
    /**
     * A method for drawing balls on the screen in random colors and positions,
     * according to the sizes entered by the user.
     * But, half of the balls are inside the gray frame,
     * and the other half move freely throughout the screen but cannot enter the gray and yellow frame.
     *
     * @param args - Balls sizes by command line.
     */
    private static void drawAnimation(String[] args) {
        // Enviroment 1 for all the balls inside the gray frame.
        Environment environmentInGray = new Environment();
        Block grayFrame = new Block(new Rectangle(new Point(50, 50), 450, 450), true);
        grayFrame.setColorForBlock(Color.gray);
        // Adding the screen and the gray frame to the environment.
        environmentInGray.addCollidable(grayFrame);
        //Enviroment 2 for all the balls Outside the gray frame.
        Environment environmentOutsideGray = new Environment();
        Block yellowFrame = new Block(new Rectangle(new Point(450, 450), 150, 150), false);
        yellowFrame.setColorForBlock(Color.yellow);
        // The gray frame is also a collidable object for the balls outside the gray frame.
        grayFrame.setIsFrame(false);
        grayFrame.setColorForBlock(Color.gray);
        // Adding the screen and the two frames to the environment.
        environmentOutsideGray.addCollidable(GameUtils.SCREEN);
        environmentOutsideGray.addCollidable(grayFrame);
        environmentOutsideGray.addCollidable(yellowFrame);

        GUI gui = new GUI("title", (int) GameUtils.SCREEN.getRect().getWidth(),
                (int) GameUtils.SCREEN.getRect().getHeight());
        Ball[] balls = new Ball[args.length];
        Ball tempBall = new Ball(0, 0, 0, Color.red);
        for (int i = 0; i < args.length; i++) {
            if (args.length / 2 > i) {
                balls[i] = GameUtils.createNewBallBySize(Integer.parseInt(args[i]), environmentInGray);
            } else {
                balls[i] = GameUtils.createNewBallBySize(Integer.parseInt(args[i]), environmentOutsideGray);
            }
        }

//        collidables.Block block = new collidables.Block(new geometry.Rectangle(new geometry.Point(100, 100), 100, 100));
        Sleeper sleeper = new Sleeper();
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            grayFrame.drawOn(d);
            int j = 0;
            for (Ball ball : balls) {
                if (balls.length / 2 > j) {
                    ball.updateBallAndMove(environmentInGray);
                    ball.drawOn(d);
                } else {
                    ball.updateBallAndMove(environmentOutsideGray);
                    ball.drawOn(d);
                }
                j++;
            }
            yellowFrame.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(50);  // wait for 50 milliseconds.
        }
    }


    /**
     * main.
     *
     * @param args - command line.
     */
    public static void main(String[] args) {
        drawAnimation(args);
    }
}
