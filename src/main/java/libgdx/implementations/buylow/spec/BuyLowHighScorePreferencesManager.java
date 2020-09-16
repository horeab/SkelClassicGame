package libgdx.implementations.buylow.spec;

import libgdx.preferences.PreferencesService;

public class BuyLowHighScorePreferencesManager {

    private static String MAX_SCORE = "MAX_SCORE";
    private static String SHARED_PREFS_NAME = "BuyLowHighScorePreferencesManager";


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public BuyLowHighScorePreferencesManager() {
        preferencesService.clear();
    }

    public int getMaxScore() {
        return preferencesService.getPreferences().getInteger(MAX_SCORE, 0);
    }

    public void putMaxScore(int maxScore) {
        preferencesService.putInteger(MAX_SCORE, maxScore);
    }

}
