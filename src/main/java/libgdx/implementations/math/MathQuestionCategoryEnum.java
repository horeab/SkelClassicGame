package libgdx.implementations.math;

import libgdx.campaign.QuestionCategory;

public enum MathQuestionCategoryEnum implements QuestionCategory {

    ;

    @Override
    public int getIndex() {
        return ordinal();
    }


}
