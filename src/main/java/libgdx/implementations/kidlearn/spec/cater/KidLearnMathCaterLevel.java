package libgdx.implementations.kidlearn.spec.cater;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;

public interface KidLearnMathCaterLevel {

    boolean asc();

    int nrOfCorrectUnknownNumbers();

    KidLearnQuestionDifficultyLevel difficulty();
}
