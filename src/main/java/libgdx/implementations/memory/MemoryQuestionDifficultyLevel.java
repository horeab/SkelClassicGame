package libgdx.implementations.memory;

import libgdx.campaign.QuestionDifficulty;

public enum MemoryQuestionDifficultyLevel implements QuestionDifficulty {

    ;

    public int getIndex() {
        return ordinal();
    }

}
