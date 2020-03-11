package libgdx.implementations.resourcewars;

import libgdx.campaign.QuestionDifficulty;

public enum ResourceWarsQuestionDifficultyLevel implements QuestionDifficulty {

    ;

    public int getIndex() {
        return ordinal();
    }

}
