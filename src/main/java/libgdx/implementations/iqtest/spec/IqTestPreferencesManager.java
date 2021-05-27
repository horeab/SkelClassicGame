package libgdx.implementations.iqtest.spec;

import com.google.gson.Gson;
import libgdx.preferences.PreferencesService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class IqTestPreferencesManager {

    private static String SHARED_PREFS_NAME = "IqTestPreferencesManager";


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public IqTestPreferencesManager() {
//        preferencesService.clear();
    }

    public void putCurrentQAState(IqTestGameType iqTestGameType, Map<Integer, Integer> questionWithAnswer) {
        preferencesService.putString(getCurrentStateKey(iqTestGameType), new Gson().toJson(questionWithAnswer));
    }

    public Map<Integer, Integer> getCurrentQAState(IqTestGameType iqTestGameType) {
        String res = preferencesService.getPreferences().getString(getCurrentStateKey(iqTestGameType), "");
        if (StringUtils.isNotBlank(res)) {
            return new Gson().fromJson(res, Map.class);
        }
        return null;
    }

    public void putLevelScore(Enum level, int score) {
        if (getLevelScore(level) < score) {
            preferencesService.putInteger(getScoreKey(level), score);
        }
    }

    public int getLevelScore(Enum level) {
        return preferencesService.getPreferences().getInteger(getScoreKey(level), 0);
    }

    private String getCurrentStateKey(IqTestGameType iqTestGameType) {
        return "CurrentState" + iqTestGameType;
    }

    private String getScoreKey(Enum level) {
        return "Score" + getLevelKey(level);
    }

    private String getLevelKey(Enum level) {
        return level.getClass().getSimpleName() + "_" + level.name();
    }


}
