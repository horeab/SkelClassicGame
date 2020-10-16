package libgdx.implementations.kidlearn;

import libgdx.campaign.QuestionDifficulty;

public enum KidLearnQuestionDifficultyLevel implements QuestionDifficulty {

    _0,
    _1,
    _2,;

    public int getIndex() {
        return ordinal();
    }

}
