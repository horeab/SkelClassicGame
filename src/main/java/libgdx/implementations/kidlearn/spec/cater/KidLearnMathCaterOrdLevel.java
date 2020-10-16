package libgdx.implementations.kidlearn.spec.cater;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;

public enum KidLearnMathCaterOrdLevel implements KidLearnMathCaterLevel {

    L0(1, 5, 1, 5, true, 4, KidLearnQuestionDifficultyLevel._0),
    L1(0, 100, 10, 6, true, 4, KidLearnQuestionDifficultyLevel._0),
    L2(-10, 10, 1, 6, true, 4, KidLearnQuestionDifficultyLevel._1),
    L3(0, 10, 0.1f, 6, true, 4, KidLearnQuestionDifficultyLevel._1),
    L4(0, 999, 1, 5, false, 4, KidLearnQuestionDifficultyLevel._2),
    ;

    public int min;
    public int max;
    public float interval;
    public int totalNrOfNumbers;
    public boolean asc;
    public int nrOfCorrectUnknownNumbers;
    public KidLearnQuestionDifficultyLevel difficulty;

    KidLearnMathCaterOrdLevel(int min, int max, float interval, int totalNrOfNumbers, boolean asc, int nrOfCorrectUnknownNumbers, KidLearnQuestionDifficultyLevel difficulty) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.totalNrOfNumbers = totalNrOfNumbers;
        this.asc = asc;
        this.nrOfCorrectUnknownNumbers = nrOfCorrectUnknownNumbers;
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