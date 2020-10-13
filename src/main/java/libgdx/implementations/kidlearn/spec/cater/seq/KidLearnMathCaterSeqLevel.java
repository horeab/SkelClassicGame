package libgdx.implementations.kidlearn.spec.cater.seq;

public enum KidLearnMathCaterSeqLevel {

    L0(0, 10, 1f, null, true),
    L1(0, 20, 2f, null, true),
    L2(0, 100, 2f, null, false),
    L3(0, 100, null, 5f, true),
    L4(0, 100, null, 9f, true),
    L5(0, 100, null, 5f, false),
    ;

    public int min;
    public int max;
    public Float interval;
    public Float upTo;
    public boolean asc;

    KidLearnMathCaterSeqLevel(int min, int max, Float interval, Float upTo, boolean asc) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.upTo = upTo;
        this.asc = asc;
    }
}
