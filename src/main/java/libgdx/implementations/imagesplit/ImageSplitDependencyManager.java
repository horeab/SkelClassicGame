package libgdx.implementations.imagesplit;

import java.util.ArrayList;
import java.util.List;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.resources.IncrementingRes;
import libgdx.utils.model.RGBColor;

public class ImageSplitDependencyManager extends CampaignGameDependencyManager {

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
    public Class<ImageSplitSpecificResource> getSpecificResourceTypeEnum() {
        return ImageSplitSpecificResource.class;
    }

    @Override
    public Class<ImageSplitCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return ImageSplitCampaignLevelEnum.class;
    }

    @Override
    public Class<ImageSplitQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return ImageSplitQuestionCategoryEnum.class;
    }

    @Override
    public Class<ImageSplitQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return ImageSplitQuestionDifficultyLevel.class;
    }

    @Override
    public String getExtraContentProductId() {
        return "extracontent.imagesplit";
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

}
