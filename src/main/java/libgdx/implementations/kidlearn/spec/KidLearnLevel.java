package libgdx.implementations.kidlearn.spec;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;

public interface KidLearnLevel {

    String title();

    KidLearnQuestionDifficultyLevel difficulty();

    boolean isLocked();
}
