package libgdx.implementations.kidlearn.spec.cater;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnMathCaterSeqLevel implements KidLearnMathCaterLevel, KidLearnLevel {

    L0(0, 10, 1f, null, true, 3,
            false, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_seq_add),
    L1(0, 20, 2f, null, true, 3,
            false, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_seq_add),
    L2(0, 100, 2f, null, false, 3,
            false, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_seq_sub),
    L3(0, 100, null, 5f, true, 3,
            false, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_seq_add),
    L4(0, 100, null, 9f, true, 3,
            false, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_seq_add),
    L5(0, 100, null, 5f, false, 3,
            false, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_sub),
    L6(0, 100, null, 9f, true, 3,
            true, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_add),
    L7(0, 100, null, 5f, false, 3,
            false, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_sub),
    L8(0, 100, null, 5f, false, 3,
            false, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_sub),
    L9(0, 100, null, 5f, false, 3,
            false, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_sub),
    ;

    public int min;
    public int max;
    public Float interval;
    public Float upTo;
    public boolean asc;
    public int nrOfCorrectUnknownNumbers;
    public boolean evenNr;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnMathCaterSeqLevel(int min, int max, Float interval, Float upTo, boolean asc, int nrOfCorrectUnknownNumbers, boolean evenNr, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.upTo = upTo;
        this.asc = asc;
        this.nrOfCorrectUnknownNumbers = nrOfCorrectUnknownNumbers;
        this.evenNr = evenNr;
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
        return title.getText(KidLearnMathCaterGameCreator.getNrFromFloat(interval));
    }
}
