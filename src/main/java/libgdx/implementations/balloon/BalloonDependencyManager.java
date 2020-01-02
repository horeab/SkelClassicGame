package libgdx.implementations.balloon;

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

public class BalloonDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
                Scanner scanner = new Scanner(questionConfigFileHandler.getFileText(difficultyLevel, category));
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine());
                }
                scanner.close();
            }
        }
        return text.toString();
    }

    @Override
    public Class<BalloonSpecificResource> getSpecificResourceTypeEnum() {
        return BalloonSpecificResource.class;
    }

    @Override
    public Class<BalloonCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return BalloonCampaignLevelEnum.class;
    }

    @Override
    public Class<BalloonQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return BalloonQuestionCategoryEnum.class;
    }

    @Override
    public Class<BalloonQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return BalloonQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

}
