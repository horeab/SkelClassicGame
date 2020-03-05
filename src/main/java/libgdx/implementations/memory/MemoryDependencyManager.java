package libgdx.implementations.memory;

import libgdx.campaign.*;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemoryDependencyManager extends CampaignGameDependencyManager {

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
    public Class<MemorySpecificResource> getSpecificResourceTypeEnum() {
        return MemorySpecificResource.class;
    }

    @Override
    public Class<MemoryCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return MemoryCampaignLevelEnum.class;
    }

    @Override
    public Class<MemoryQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return MemoryQuestionCategoryEnum.class;
    }

    @Override
    public Class<MemoryQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return MemoryQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 142, 220, 176);
    }

}
