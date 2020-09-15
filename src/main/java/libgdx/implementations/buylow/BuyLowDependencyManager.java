package libgdx.implementations.buylow;

import java.util.ArrayList;
import java.util.List;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.resources.IncrementingRes;
import libgdx.utils.model.RGBColor;

public class BuyLowDependencyManager extends CampaignGameDependencyManager {

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
    public Class<BuyLowSpecificResource> getSpecificResourceTypeEnum() {
        return BuyLowSpecificResource.class;
    }

    @Override
    public Class<BuyLowCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return BuyLowCampaignLevelEnum.class;
    }

    @Override
    public Class<BuyLowQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return BuyLowQuestionCategoryEnum.class;
    }

    @Override
    public Class<BuyLowQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return BuyLowQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

}
