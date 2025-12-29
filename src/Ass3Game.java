/**
 * YAHAV EFRATI 212535660
 * THE MAIN CLASS OF THE GAME.
 */
public class Ass3Game {
    /**
     * Run the game.
     */
    public static void runGame() {
        Game game = new Game();
        game.initialize();
        game.run();
    }

    /**
     * MAIN.
     * @param args - command line args.
     */
    public static void main(String[] args) {
       runGame();
    }
}
