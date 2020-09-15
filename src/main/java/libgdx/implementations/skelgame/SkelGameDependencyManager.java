package libgdx.implementations.skelgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.LettersCampaignLevelEnum;
import libgdx.campaign.LettersQuestionCategoryEnum;
import libgdx.campaign.LettersQuestionDifficultyLevel;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.campaign.StarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

public class SkelGameDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<IncrementingRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(SkelGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(SkelGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<SkelGameSpecificResource> getSpecificResourceTypeEnum() {
        return SkelGameSpecificResource.class;
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
