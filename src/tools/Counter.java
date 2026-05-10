package tools;
/**
 * Class for counter tool for keeping track of the game.
 */
public class Counter {
    private int num;
    /**
     * Constructor for counter.
     * @param num starting number.
     */
    public Counter(int num) {
        this.num = num;
    }

    /**
     * add number to current count.
     * @param number to add.
     */
    public void increase(int number) {
        num = num + number;
    }
    /**
     * subtract number from current count.
     * @param number to subtract.
     */
    public void decrease(int number) {
        num = num - number;
    }

    /**
     * get current count.
     * @return current number.
     */
    public int getValue() {
        return num;
    }
}