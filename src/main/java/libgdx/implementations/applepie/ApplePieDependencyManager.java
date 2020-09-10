package libgdx.implementations.applepie;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import libgdx.campaign.CampaignGame;
import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;
import libgdx.utils.model.RGBColor;

public class ApplePieDependencyManager extends CampaignGameDependencyManager {

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
    public Class<ApplePieSpecificResource> getSpecificResourceTypeEnum() {
        return ApplePieSpecificResource.class;
    }

    @Override
    public Class<ApplePieCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return ApplePieCampaignLevelEnum.class;
    }

    @Override
    public Class<ApplePieQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return ApplePieQuestionCategoryEnum.class;
    }

    @Override
    public Class<ApplePieQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return ApplePieQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

}
