package libgdx.implementations.kidlearn.spec.cater.seq;

import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;

public enum KidLearnMathCaterSeqLevel implements KidLearnMathCaterLevel {

    L0(0, 10, 1f, null, true, 3, false),
    L1(0, 20, 2f, null, true, 3, false),
    L2(0, 100, 2f, null, false, 3, false),
    L3(0, 100, null, 5f, true, 3, false),
    L4(0, 100, null, 9f, true, 3, false),
    L5(0, 100, null, 5f, false, 3, false),
    L6(0, 100, null, 9f, true, 3, true),
    ;

    public int min;
    public int max;
    public Float interval;
    public Float upTo;
    public boolean asc;
    public int nrOfCorrectUnknownNumbers;
    public boolean evenNr;

    KidLearnMathCaterSeqLevel(int min, int max, Float interval, Float upTo, boolean asc, int nrOfCorrectUnknownNumbers, boolean evenNr) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.upTo = upTo;
        this.asc = asc;
        this.nrOfCorrectUnknownNumbers = nrOfCorrectUnknownNumbers;
        this.evenNr = evenNr;
    }

    @Override
    public boolean asc() {
        return asc;
    }

    @Override
    public int nrOfCorrectUnknownNumbers() {
        return nrOfCorrectUnknownNumbers;
    }
}
