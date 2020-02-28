package libgdx.implementations.memory.spec;

public enum GameLevel {

    _0(2, 2, 6),
    _1(3, 2, 7),
    _2(4, 2, 9),
    _3(4, 4, 11),
    _4(5, 4, 14),
    _5(6, 5, 17),
    _6(6, 6, 20),;

    private int rows;
    private int cols;
    private int maxItems;

    GameLevel(int rows, int cols, int maxItems) {
        this.rows = rows;
        this.cols = cols;
        this.maxItems = maxItems;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMaxItems() {
        return maxItems;
    }
}
