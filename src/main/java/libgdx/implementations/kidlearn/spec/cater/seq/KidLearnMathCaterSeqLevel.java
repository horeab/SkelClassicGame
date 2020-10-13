package libgdx.implementations.kidlearn.spec.cater.seq;

public enum KidLearnMathCaterSeqLevel {

    L0(0, 10, 1, null, true),
    L1(0, 20, 2, null, true),
    L2(0, 100, 2, null, false),
    L3(0, 100, null, 5, true),
    L4(0, 20, null, 9, true),
    L5(0, 100, null, 5, false),
    ;

    public int min;
    public int max;
    public Integer interval;
    public Integer upTo;
    public boolean asc;

    KidLearnMathCaterSeqLevel(int min, int max, Integer interval, Integer upTo, boolean asc) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.upTo = upTo;
        this.asc = asc;
    }
}
