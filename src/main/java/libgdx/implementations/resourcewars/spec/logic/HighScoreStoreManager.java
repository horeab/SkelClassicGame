package libgdx.implementations.resourcewars.spec.logic;

import libgdx.preferences.PreferencesService;

public class HighScoreStoreManager {

	private static String GAMES_PLAYED = "GAMES_PLAYED";
	private static String MINIMUM_DAYS_TO_FINISH_GAME = "MINIMUM_DAYS_TO_FINISH_GAME";
	private static String SHARED_PREFS_NAME = "HighScoreStoreManager";

	private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

	public void incrementGamesPlayed() {
		preferencesService.putInteger(GAMES_PLAYED, getGamesPlayedInt() + 1);
	}

	public boolean putMinDays(int days) {
		int minDays = getMinDaysInt();
		if (minDays == 0) {
			minDays = Integer.MAX_VALUE;
		}
		if (days < minDays) {
			preferencesService.putInteger(MINIMUM_DAYS_TO_FINISH_GAME, days);
			return true;
		}
		return false;
	}

	private int getGamesPlayedInt() {
		return preferencesService.getPreferences().getInteger(GAMES_PLAYED, 0);
	}

	private int getMinDaysInt() {
		return preferencesService.getPreferences().getInteger(MINIMUM_DAYS_TO_FINISH_GAME, 0);
	}

	public String getGamesPlayed() {
		String gamesPlayed = Integer.toString(getGamesPlayedInt());
		gamesPlayed = processNonValue(gamesPlayed);
		return gamesPlayed;

	}

	public String getMinDays() {
		String minDays = Integer.toString(getMinDaysInt());
		minDays = processNonValue(minDays);
		return minDays;
	}

	private String processNonValue(String val) {
		if (val.equals("0")) {
			val = "-";
		}
		return val;
	}
}
