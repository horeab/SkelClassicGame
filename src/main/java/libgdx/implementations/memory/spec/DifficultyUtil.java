package libgdx.implementations.memory.spec;

public class DifficultyUtil {
	public static final int SWARM_APP_ID = 8434;

	public static final String SWARM_APP_KEY = "b76dde9e6e15f4d63f9976470612d19b";

	public static final int SWARM_AMATEUR_LEADERBOARD_ID = 12504;
	public static final int SWARM_PROFESSIONAL_LEADERBOARD_ID = 12506;
	public static final int SWARM_WORLD_CLASS_LEADERBOARD_ID = 12508;

	private int selectedDifficulty;

//	private Resources res;

	public DifficultyUtil() {
		this.selectedDifficulty = 1;
//		this.res = context.getResources();
	}
//
//	public String getSelectedDifficultyName() {
//		return getDifficultyNameForDifficultyIndex(selectedDifficulty, context);
//	}
//
//	public static Map<String, Integer> getDifficultyNameAndScores(Activity context) {
//		Map<String, Integer> diffAndScore = new LinkedHashMap<String, Integer>();
//
//		int nrOfDifficulties = getNrOfDifficulties(context);
//		for (int i = 0; i < nrOfDifficulties; i++) {
//			diffAndScore.put(getDifficultyNameForDifficultyIndex(i, context), getHighScoreForDifficultyIndex(i, context));
//		}
//
//		return diffAndScore;
//	}
//
//	public int getScoreForToIncrement() {
//		return getDifficultyIncrementScoreFromResource(selectedDifficulty, "for");
//	}
//
//	public int getScoreAgainstToIncrement() {
//		return getDifficultyIncrementScoreFromResource(selectedDifficulty, "against");
//	}
//
//	public int getChancesNr(int levelNr) {
//		return getChancesForDifficultyFromResource(selectedDifficulty) + levelNr * 3;
//	}
//
//	public int getSelectedDifficultyHighScore(Activity context) {
//		return getHighScoreForDifficultyIndex(selectedDifficulty, context);
//	}
//
//	public int getDifficultySWARMLeaderBoardId(int diff) {
//		int leaderBoardId = 0;
//		if (diff == 2) {
//			leaderBoardId = SWARM_WORLD_CLASS_LEADERBOARD_ID;
//		} else if (diff == 1) {
//			leaderBoardId = SWARM_PROFESSIONAL_LEADERBOARD_ID;
//		} else {
//			leaderBoardId = SWARM_AMATEUR_LEADERBOARD_ID;
//		}
//		return leaderBoardId;
//	}
//
//	public int getSelectedDifficultySWARMLeaderBoardId() {
//		return getDifficultySWARMLeaderBoardId(selectedDifficulty);
//	}
//
//	private static int getHighScoreForDifficultyIndex(int diffIndex, Activity context) {
//		StoreManagement storeManagement = new StoreManagement(context);
//		return storeManagement.getHighScoreForDifficulty(diffIndex);
//	}
//
//	public static int getNrOfDifficulties(Activity context) {
//		int resId = -1;
//		int nrOfDiff = 0;
//		while (resId != 0) {
//			resId = getDifficultyNameResourceId(nrOfDiff, context);
//			nrOfDiff++;
//		}
//		return nrOfDiff - 1;
//	}
//
//	public static Map<Integer, String> getCodeAndDifficultyName(Activity context) {
//		Map<Integer, String> diffAndScore = new HashMap<Integer, String>();
//		int nrOfDifficulties = getNrOfDifficulties(context);
//		for (int i = 0; i < nrOfDifficulties; i++) {
//			diffAndScore.put(i, getDifficultyNameForDifficultyIndex(i, context));
//		}
//		return diffAndScore;
//	}
//
//	public static int getDifficultyIndexForDifficultyName(String diffName, Activity context) {
//		Map<Integer, String> diffAndScore = getCodeAndDifficultyName(context);
//		for (Entry<Integer, String> entry : diffAndScore.entrySet()) {
//			if (entry.getValue().equals(diffName)) {
//				return entry.getKey();
//			}
//		}
//		return -1;
//	}
//
//	public static String getDifficultyNameForDifficultyIndex(int difficultyIndex, Activity context) {
//		int nameId = getDifficultyNameResourceId(difficultyIndex, context);
//		return context.getResources().getString(nameId);
//	}
//
//	private static int getDifficultyNameResourceId(int difficultyIndex, Activity context) {
//		String difficultyNameResName = "difficulty_name_" + difficultyIndex;
//		int nameId = context.getResources().getIdentifier(difficultyNameResName, "string", context.getPackageName());
//		return nameId;
//	}
//
//	private int getDifficultyIncrementScoreFromResource(int difficultyIndex, String forAgainst) {
//		String difficultyScoreResName = "difficulty_increment_" + forAgainst + "_score_" + difficultyIndex;
//		int scoreId = res.getIdentifier(difficultyScoreResName, "integer", context.getPackageName());
//		return res.getInteger(scoreId);
//	}
//
//	private int getChancesForDifficultyFromResource(int difficultyIndex) {
//		String resName = "difficulty_chances_" + difficultyIndex;
//		int scoreId = res.getIdentifier(resName, "integer", context.getPackageName());
//		return res.getInteger(scoreId);
//	}
}
