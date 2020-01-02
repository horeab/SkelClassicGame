package libgdx.implementations.balloon;

import libgdx.campaign.QuestionDifficulty;

public enum BalloonQuestionDifficultyLevel implements QuestionDifficulty {

    ;

    public int getIndex() {
        return ordinal();
    }

}
