package libgdx.implementations.resourcewars.spec.logic;

import libgdx.preferences.PreferencesService;

public class HighScorePreferencesManager {

    private static String MAX_REPUTATION = "MAX_REPUTATION";
    private static String MAX_DAYS = "MAX_DAYS";
    private static String SHARED_PREFS_NAME = "HighScorePreferencesManager";
    public static Integer MAX_DAYS_DEF = 999;


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public HighScorePreferencesManager() {
//        preferencesService.clear();
    }

    public int getMaxReputation() {
        return preferencesService.getPreferences().getInteger(MAX_REPUTATION, 0);
    }

    public int getMaxDays() {
        return preferencesService.getPreferences().getInteger(MAX_DAYS, MAX_DAYS_DEF);
    }

    public void putMaxReputation(int maxReputation) {
        preferencesService.putInteger(MAX_REPUTATION, maxReputation);
    }

    public void putMaxDays(int maxDays) {
        preferencesService.putInteger(MAX_DAYS, maxDays);
    }
}
