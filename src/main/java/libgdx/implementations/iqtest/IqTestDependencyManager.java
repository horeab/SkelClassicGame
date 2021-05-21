package libgdx.implementations.iqtest;

import libgdx.campaign.*;
import libgdx.resources.IncrementingRes;

import java.util.ArrayList;
import java.util.List;

public class IqTestDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<IncrementingRes> list = new ArrayList<>();
        int totalIqQuestionsInclusivePlusOne = 38;
        list.add(new IqTestIqImageQuestionIncrementRes(0, totalIqQuestionsInclusivePlusOne, IqTestIqImageQuestionIncrementRes.PNG));
        for (int i = 0; i < 8; i++) {
            list.add(new IqTestIqImageAnswerIncrementRes(0, totalIqQuestionsInclusivePlusOne, i, IqTestIqImageAnswerIncrementRes.PNG));
        }
        int totalNumberSeqQuestionsInclusivePlusOne = 13;
        list.add(new IqTestNumberSeqImageQuestionIncrementRes(0, totalNumberSeqQuestionsInclusivePlusOne, IqTestNumberSeqImageQuestionIncrementRes.PNG));
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
