package libgdx.implementations.iqtest.spec;

import com.google.gson.Gson;
import libgdx.preferences.PreferencesService;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
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
            Map rawMap = new Gson().fromJson(res, Map.class);
            Map<Integer, Integer> map = new LinkedHashMap<>();
            for (Object o : rawMap.entrySet()) {
                map.put((int) Math.round(Double.valueOf(((Map.Entry) o).getKey().toString())),
                        (int) Math.round(Double.valueOf(((Map.Entry) o).getValue().toString())));
            }
            return map;
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
