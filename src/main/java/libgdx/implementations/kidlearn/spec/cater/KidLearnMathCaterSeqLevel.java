package libgdx.implementations.kidlearn.spec.cater;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;

public enum KidLearnMathCaterSeqLevel implements KidLearnMathCaterLevel {

    L0(0, 10, 1f, null, true, 3, false, KidLearnQuestionDifficultyLevel._0),
    L1(0, 20, 2f, null, true, 3, false, KidLearnQuestionDifficultyLevel._0),
    L2(0, 100, 2f, null, false, 3, false, KidLearnQuestionDifficultyLevel._0),
    L3(0, 100, null, 5f, true, 3, false, KidLearnQuestionDifficultyLevel._1),
    L4(0, 100, null, 9f, true, 3, false, KidLearnQuestionDifficultyLevel._1),
    L5(0, 100, null, 5f, false, 3, false, KidLearnQuestionDifficultyLevel._2),
    L6(0, 100, null, 9f, true, 3, true, KidLearnQuestionDifficultyLevel._2),
    L7(0, 100, null, 5f, false, 3, false, KidLearnQuestionDifficultyLevel._2),
    L8(0, 100, null, 5f, false, 3, false, KidLearnQuestionDifficultyLevel._2),
    L9(0, 100, null, 5f, false, 3, false, KidLearnQuestionDifficultyLevel._2),
    ;

    public int min;
    public int max;
    public Float interval;
    public Float upTo;
    public boolean asc;
    public int nrOfCorrectUnknownNumbers;
    public boolean evenNr;
    public KidLearnQuestionDifficultyLevel difficulty;

    KidLearnMathCaterSeqLevel(int min, int max, Float interval, Float upTo, boolean asc, int nrOfCorrectUnknownNumbers, boolean evenNr, KidLearnQuestionDifficultyLevel difficulty) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.upTo = upTo;
        this.asc = asc;
        this.nrOfCorrectUnknownNumbers = nrOfCorrectUnknownNumbers;
        this.evenNr = evenNr;
        this.difficulty = difficulty;
    }

    @Override
    public KidLearnQuestionDifficultyLevel difficulty() {
        return difficulty;
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
