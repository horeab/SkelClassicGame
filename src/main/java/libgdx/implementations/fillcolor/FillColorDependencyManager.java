package libgdx.implementations.fillcolor;

import java.util.ArrayList;
import java.util.List;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.resources.IncrementingRes;
import libgdx.utils.model.RGBColor;

public class FillColorDependencyManager extends CampaignGameDependencyManager {

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
    public Class<FillColorSpecificResource> getSpecificResourceTypeEnum() {
        return FillColorSpecificResource.class;
    }

    @Override
    public Class<FillColorCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return FillColorCampaignLevelEnum.class;
    }

    @Override
    public Class<FillColorQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return FillColorQuestionCategoryEnum.class;
    }

    @Override
    public Class<FillColorQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return FillColorQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

}
