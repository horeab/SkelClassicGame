package libgdx.implementations.iqtest;

import libgdx.campaign.*;
import libgdx.resources.IncrementingRes;

import java.util.ArrayList;
import java.util.List;

public class IqTestDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<IncrementingRes> list = new ArrayList<>();
        int totalQuestions = 38;
        list.add(new ImageQuestionIncrementRes(0, totalQuestions, ImageQuestionIncrementRes.PNG));
        for (int i = 0; i < 8; i++) {
            list.add(new ImageAnswerIncrementRes(0, totalQuestions, i, ImageQuestionIncrementRes.PNG));
        }
        return list;
    }

    @Override
    protected String allQuestionText() {
        return "";
    }

    @Override
    public String getExtraContentProductId() {
        return "extraContentIqtest";
    }

    @Override
    public Class<IqTestSpecificResource> getSpecificResourceTypeEnum() {
        return IqTestSpecificResource.class;
    }

    @Override
    public Class<LettersCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return LettersCampaignLevelEnum.class;
    }

    @Override
    public Class<LettersQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return LettersQuestionCategoryEnum.class;
    }

    @Override
    public Class<LettersQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return LettersQuestionDifficultyLevel.class;
    }

    public StarsService getStarsService() {
        return new StarsService();
    }
}
