package libgdx.implementations.math;

import libgdx.campaign.QuestionDifficulty;

public enum MathQuestionDifficultyLevel implements QuestionDifficulty {

    ;

    public int getIndex() {
        return ordinal();
    }

}
