package libgdx.implementations.resourcewars;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.resources.IncrementingRes;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.List;

public class ResourceWarsDependencyManager extends CampaignGameDependencyManager {

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
    public Class<ResourceWarsSpecificResource> getSpecificResourceTypeEnum() {
        return ResourceWarsSpecificResource.class;
    }

    @Override
    public Class<ResourceWarsCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return ResourceWarsCampaignLevelEnum.class;
    }

    @Override
    public Class<ResourceWarsQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return ResourceWarsQuestionCategoryEnum.class;
    }

    @Override
    public Class<ResourceWarsQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return ResourceWarsQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 142, 220, 176);
    }

}
