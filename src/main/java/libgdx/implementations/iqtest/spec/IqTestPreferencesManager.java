package libgdx.implementations.iqtest.spec;

import libgdx.preferences.PreferencesService;

public class IqTestPreferencesManager {

    private static String SHARED_PREFS_NAME = "IqTestPreferencesManager";


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public IqTestPreferencesManager() {
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

    private String getScoreKey(Enum level) {
        return "Score" + getLevelKey(level);
    }

    private String getLevelKey(Enum level) {
        return level.getClass().getSimpleName() + "_" + level.name();
    }


}
