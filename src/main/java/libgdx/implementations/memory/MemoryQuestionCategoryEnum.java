package libgdx.implementations.memory;

import libgdx.campaign.QuestionCategory;

public enum MemoryQuestionCategoryEnum implements QuestionCategory {

    ;

    @Override
    public int getIndex() {
        return ordinal();
    }


}
