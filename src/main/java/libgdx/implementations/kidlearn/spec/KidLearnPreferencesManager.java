package libgdx.implementations.kidlearn.spec;

import libgdx.preferences.PreferencesService;

public class KidLearnPreferencesManager {

    private static String SHARED_PREFS_NAME = "ImageSplitPreferencesManager";


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public KidLearnPreferencesManager() {
//        preferencesService.clear();
    }

    public void putLevelScore(Enum level, int score) {
        if (getLevelScore(level) < score) {
            preferencesService.putInteger(getLevelKey(level), score);
        }
    }

    public int getLevelScore(Enum level) {
        return preferencesService.getPreferences().getInteger(getLevelKey(level), 0);
    }

    private String getLevelKey(Enum level) {
        return level.getClass().getSimpleName() + "_" + level.name();
    }

}
