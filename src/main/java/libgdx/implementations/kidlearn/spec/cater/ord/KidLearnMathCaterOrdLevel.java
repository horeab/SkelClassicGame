package libgdx.implementations.kidlearn.spec.cater.ord;

public enum KidLearnMathCaterOrdLevel {

    L0(1, 5, 1, 5, true),
    L1(0, 100, 10, 6, true),
    L2(-10, 10, 1, 6, true),
    L3(0, 10, 0.1f, 6, true),
    L4(0, 999, 1, 5, false),
    ;

    public int min;
    public int max;
    public float interval;
    public int totalNrOfNumbers;
    public boolean asc;

    KidLearnMathCaterOrdLevel(int min, int max, float interval, int totalNrOfNumbers, boolean asc) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.totalNrOfNumbers = totalNrOfNumbers;
        this.asc = asc;
    }
}
