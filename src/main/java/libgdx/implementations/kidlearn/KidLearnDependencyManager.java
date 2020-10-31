package libgdx.implementations.kidlearn;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterSeqLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateLevel;
import libgdx.resources.IncrementingRes;
import libgdx.utils.model.RGBColor;

public class KidLearnDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    public String getAllFontChars() {
        String allChars = super.getAllFontChars();
        List<Enum<? extends KidLearnLevel>> allLevels = new ArrayList<>();
        allLevels.addAll(Arrays.asList(KidLearnEngHangmanLevel.values()));
        allLevels.addAll(Arrays.asList(KidLearnEngVerbLevel.values()));
        allLevels.addAll(Arrays.asList(KidLearnEngWordsLevel.values()));
        allLevels.addAll(Arrays.asList(KidLearnSciBodyLevel.values()));
        allLevels.addAll(Arrays.asList(KidLearnSciFeedLevel.values()));
        allLevels.addAll(Arrays.asList(KidLearnSciRecyLevel.values()));
        allLevels.addAll(Arrays.asList(KidLearnSciStateLevel.values()));

        for (Enum<? extends KidLearnLevel> val : allLevels) {
            allChars = allChars + KidLearnUtils.getWords(val);
        }
        Set<String> resultSet = new HashSet<>();
        for (int i = 0; i < allChars.length(); i++) {
            String var = Character.toString(allChars.charAt(i));
            resultSet.add(var.toLowerCase());
            resultSet.add(var.toUpperCase());
        }
        return StringUtils.join(resultSet, "");
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
