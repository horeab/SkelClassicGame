package libgdx.campaign;

import libgdx.implementations.skelgame.SkelGameButtonSkin;
import libgdx.resources.Res;
import libgdx.resources.Resource;
import libgdx.resources.SpecificResource;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.EnumUtils;


public class CampaignLevelEnumService {

    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private CampaignLevel campaignLevel;

    public CampaignLevelEnumService(CampaignLevel campaignLevel) {
        this.campaignLevel = campaignLevel;
    }

    public static CampaignLevel getCampaignLevelForDiffAndCat(QuestionDifficulty difficultyLevel, QuestionCategory category) {
        return (CampaignLevel) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum(), "LEVEL_" + difficultyLevel.getIndex() + "_" + category.getIndex());
    }

    public static CampaignLevel getNextLevel(CampaignLevel currentCampaignLevelEnum) {
        CampaignLevel[] values = (CampaignLevel[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum());
        for (CampaignLevel campaignLevelEnum : values) {
            if (currentCampaignLevelEnum != null && campaignLevelEnum.getIndex() == currentCampaignLevelEnum.getIndex() + 1) {
                return campaignLevelEnum;
            }
        }
        return null;
    }

    public String getLabelText() {
        return getCategory() != null ? new SpecificPropertiesUtils().getQuestionCategoryLabel(getCategory()) : null;
    }

    public Resource getBackgroundTexture() {
        return EnumUtils.getEnumValue(Resource.class, "campaign_level_" + getDifficulty() + "_background");
    }

    public SkelGameButtonSkin getButtonSkin() {
        return EnumUtils.getEnumValue(SkelGameButtonSkin.class, "CAMPAIGN_LEVEL_" + getCategory());
    }

    public QuestionConfig getQuestionConfig(int nrOfQuestions) {
        QuestionDifficulty difficulty = (QuestionDifficulty) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum(), "_" + getDifficulty());
        QuestionConfig questionConfig;
        if (getCategory() != null) {
            QuestionCategory category = (QuestionCategory) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum(), "cat" + getCategory());
            questionConfig = new QuestionConfig(difficulty, category);
        } else {
            questionConfig = new QuestionConfig(difficulty);
        }
        questionConfig.setA(nrOfQuestions);
        return questionConfig;
    }

    public GameTypeStage getGameTypeStage() {
        Integer category = getCategory();
        if (category != null && category > 4) {
            category = category - 5 * getDifficulty();
        }
        return EnumUtils.getEnumValue(GameTypeStage.class, "CAMPAIGN_LEVEL_" + getDifficulty() + (category != null ? "_" + category : ""));
    }

    private int getDifficulty() {
        return Integer.valueOf(getSplit(campaignLevel.getName())[1]);
    }

    public Integer getCategory() {
        return getCategory(campaignLevel.getName());
    }


    public Res getIcon() {
        return (SpecificResource) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getSpecificResourceTypeEnum(), "campaign_level_" + getDifficulty() + "_" + getCategory());
    }

    private static String[] getSplit(String name) {
        return name.split("_");
    }

    public static Integer getCategory(String name) {
        String[] split = getSplit(name);
        return split.length == 3 ? Integer.valueOf(split[2]) : null;
    }

}
