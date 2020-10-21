package libgdx.implementations.kidlearn.spec.math;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;

public interface KidLearnMathCaterLevel {

    boolean asc();

    int nrOfCorrectUnknownNumbers();

    KidLearnQuestionDifficultyLevel difficulty();
}
