package libgdx.implementations.kidlearn.spec;

import org.apache.commons.lang3.StringUtils;

import libgdx.implementations.imagesplit.spec.ImageSplitGameType;
import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.preferences.PreferencesService;

public class KidLearnPreferencesManager {

    private static String SHARED_PREFS_NAME = "ImageSplitPreferencesManager";


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public KidLearnPreferencesManager() {
//        preferencesService.clear();
    }

    public void putLevelScore(Enum level, int score) {
        if (getLevelScore(level) < score) {
            preferencesService.putInteger(getScoreKey(level), score);
        }
    }

    public int getLevelScore(Enum level) {
        return preferencesService.getPreferences().getInteger(getScoreKey(level), 0);
    }

    public KidLearnQuestionDifficultyLevel getDifficultyLevel(Class<? extends Enum> levelType) {
        String res = preferencesService.getPreferences().getString(getDifficultyKey(levelType));
        return StringUtils.isBlank(res) ? KidLearnQuestionDifficultyLevel._0 : KidLearnQuestionDifficultyLevel.valueOf(res);
    }

    public void putDifficultyLevel(Class<? extends Enum> levelType, KidLearnQuestionDifficultyLevel difficultyLevel) {
        preferencesService.putString(getDifficultyKey(levelType), difficultyLevel.name());
    }

    private String getScoreKey(Enum level) {
        return "Score" + getLevelKey(level);
    }

    private String getDifficultyKey(Class<? extends Enum> levelType) {
        return "Difficulty" + levelType.getSimpleName();
    }

    private String getLevelKey(Enum level) {
        return level.getClass().getSimpleName() + "_" + level.name();
    }

    public void putTutorialPlayed(String level) {
        preferencesService.putBoolean(getTutorialKey(level), true);
    }

    public boolean isTutorialPlayed(String level) {
        return preferencesService.getPreferences().getBoolean(getTutorialKey(level), false);
    }

    private String getTutorialKey(String level) {
        return level + "_TUTORIAL";
    }

}
