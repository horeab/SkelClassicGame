package libgdx.implementations.kidlearn.spec.math;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnMathCaterOrdLevel implements KidLearnMathCaterLevel, KidLearnLevel {

    L0(1, 5, 1, 5, true, 4,
            KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_ord_asc, false),
    L1(0, 10, 1, 6, true, 4,
            KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_ord_asc, false),
    L2(0, 100, 10, 6, true, 4,
            KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_ord_asc, true),
    L3(0, 10, 1, 6, false, 4,
            KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_ord_desc, false),
    L4(-10, 10, 1, 6, true, 4,
            KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_ord_asc, true),
    L5(0, 10, 0.1f, 6, true, 4,
            KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_ord_decimal, true),
    L6(0, 999, 1, 5, false, 4,
            KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_ord_desc, true),
    L7(-10, 10, 1, 6, false, 4,
            KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_ord_desc, true),
    L8(-999, 999, 0.1f, 6, false, 4,
            KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_ord_decimal, true),
    ;

    public int min;
    public int max;
    public float interval;
    public int totalNrOfNumbers;
    public boolean asc;
    public int nrOfCorrectUnknownNumbers;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;
    public boolean isLocked;

    KidLearnMathCaterOrdLevel(int min, int max, float interval, int totalNrOfNumbers, boolean asc, int nrOfCorrectUnknownNumbers, KidLearnQuestionDifficultyLevel difficulty, GameLabel title, boolean isLocked) {
        this.isLocked = isLocked;
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.totalNrOfNumbers = totalNrOfNumbers;
        this.asc = asc;
        this.nrOfCorrectUnknownNumbers = nrOfCorrectUnknownNumbers;
        this.difficulty = difficulty;
        this.title = title;
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

    @Override
    public String title() {
        return title.getText();
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }
}
