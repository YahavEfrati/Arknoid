import biuoop.GUI;
import biuoop.DrawSurface;

import java.util.Random;
import java.awt.Color;

/**
 * This class is for combining the Line and Point classes and making the actual work.
 * Here, we generate a 10 random black lines. For each line, we draw a blue dot on the middle point of the line.
 * For every line intersection, we draw a red dot on the intersection point.
 * If a triangle is made up by the intersections, we draw it in green.
 * ID: 212535660.
 */
public class AbstractArtDrawing {

    /**
     *This is the Main method that concludes the Main mission.
     */
    public void drawProject() {
        GUI gui = new GUI("Line Mission", 800, 600);
        DrawSurface drawSurface = gui.getDrawSurface();
        Line[] lines = new Line[10];
        //Loop for drawing 10 random lines on the screen, and a blue point in the middle of every line.
        for (int i = 0; i < lines.length; i++) {
            lines[i] = generateRandomLine();
            drawLine(lines[i], drawSurface, Color.black);
            Point middle = lines[i].middle();
            drawSurface.setColor(Color.BLUE);
            drawSurface.fillCircle((int) middle.getX(), (int) middle.getY(), 3);
        }
        //Loops for getting the intersection point between every two lines.
        //if there is one, it draw a red point at it.
        for (int i = 0; i < lines.length - 1; i++) {
            for (int j = i + 1; j < lines.length; j++) {
                Point intersectionPoint = lines[i].intersectionWith(lines[j]);
                if (intersectionPoint == null) {
                    continue;
                }
                drawSurface.setColor(Color.RED);
                drawSurface.fillCircle((int) intersectionPoint.getX(), (int) intersectionPoint.getY(), 3);
            }
        }
        //A loop that checks for triangles formed between the intersection points of the lines.
        for (int i = 0; i < lines.length - 2; i++) {
            for (int j = i + 1; j < lines.length - 1; j++) {
                for (int k = j + 1; k < lines.length; k++) {
                    //First, we check if there are 3 points of intersection between 3 lines.
                    if (!lines[i].isIntersecting(lines[j], lines[k])
                            || !lines[j].isIntersecting(lines[i], lines[k])) {
                        continue;
                    }
                    Point point1 = lines[i].intersectionWith(lines[j]);
                    Point point2 = lines[i].intersectionWith(lines[k]);
                    Point point3 = lines[j].intersectionWith(lines[k]);
                    //Now, we check that there are really 3 intersection points without lines that contain each other.
                    if (point1 == null || point2 == null || point3 == null) {
                        continue;
                    }
                    Line l1 = new Line(point1, point2);
                    Line l2 = new Line(point1, point3);
                    Line l3 = new Line(point2, point3);
                    drawLine(l1, drawSurface, Color.GREEN);
                    drawLine(l2, drawSurface, Color.GREEN);
                    drawLine(l3, drawSurface, Color.GREEN);

                }
            }
        }
        gui.show(drawSurface);
    }
    /**
     * Method that generate a randon line within the screen.
     * @return a new random Line object.
     */
    public Line generateRandomLine() {
        Random rand = new Random();
        int x1 = rand.nextInt(800) + 1;
        int y1 = rand.nextInt(600) + 1;
        int x2 = rand.nextInt(800) + 1;
        int y2 = rand.nextInt(600) + 1;
        return new Line(x1, y1, x2, y2);

    }
    /**
     * Draw a specific line in the screen.
     * @param l - the line we draw with his values and coordinates.
     * @param d - the surface screen object.
     * @param color the color we want to draw the line in.
     */
    public void drawLine(Line l, DrawSurface d, Color color) {
        d.setColor(color);
        d.drawLine((int) l.start().getX(), (int) l.start().getY(), (int) l.end().getX(), (int) l.end().getY());
    }
    /**
     * Main - program start from here.
     * @param args - noting relevent.
     */
    public static void main(String[] args) {
        AbstractArtDrawing act = new AbstractArtDrawing();
        act.drawProject();

    }
}




