package libgdx.implementations.kidlearn;

import java.util.ArrayList;
import java.util.List;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.resources.IncrementingRes;
import libgdx.utils.model.RGBColor;

public class KidLearnDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        return "";
    }

    @Override
    public Class<KidLearnSpecificResource> getSpecificResourceTypeEnum() {
        return KidLearnSpecificResource.class;
    }

    @Override
    public Class<KidLearnCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return KidLearnCampaignLevelEnum.class;
    }

    @Override
    public Class<KidLearnQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return KidLearnQuestionCategoryEnum.class;
    }

    @Override
    public Class<KidLearnQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return KidLearnQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

}
