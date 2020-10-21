package libgdx.implementations.kidlearn.spec.math;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnMathCaterSeqLevel implements KidLearnMathCaterLevel, KidLearnLevel {

    L0(0, 10, 1f, null, true, 3,
            KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_seq_add_nr),
    L1(0, 20, 2f, null, true, 3,
            KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_seq_add_nr),
    L2(0, 50, null, 5f, true, 3,
            KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_math_seq_add_nr),
    L3(0, 100, null, 5f, true, 3,
            KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_seq_add_nr),
    L4(0, 10, 0.5f, null, true, 3,
            KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_seq_add_nr),
    L5(0, 999, null, 9f, false, 3,
            KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_math_seq_sub_nr),
    L6(-100, 0, 3f, null, true, 3,
            KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_add_nr),
    L7(-999, 0, null, 9f, false, 3,
            KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_sub_nr),
    L8(-999, 999, 0.3f, null, false, 3,
            KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_math_seq_sub_nr),
    ;

    public int min;
    public int max;
    public Float interval;
    public Float upTo;
    public boolean asc;
    public int nrOfCorrectUnknownNumbers;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnMathCaterSeqLevel(int min, int max, Float interval, Float upTo, boolean asc, int nrOfCorrectUnknownNumbers, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.upTo = upTo;
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
        return title.getText(KidLearnMathCaterGameCreator.getNrFromFloat(interval));
    }
}
