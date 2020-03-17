package libgdx.implementations.resourcewars.spec.logic;

import com.google.gson.Gson;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.preferences.PreferencesService;

public class InGameStoreManager {

    private static String LOCATION_UNLOCK = "LOCATION_UNLOCK";
    private static String MAX_HEALTH_CONTAINER_BOUGHT = "MAX_HEALTH_CONTAINER_BOUGHT";
    private static String SAVED_GAME = "SAVED_GAME";
    private static String SHARED_PREFS_NAME = "InGameStoreManager";


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public InGameStoreManager() {
//        preferencesService.clear();
    }

    public void reset(){
        preferencesService.clear();
    }

    public boolean isMaxHealthContainerBought() {
        return preferencesService.getPreferences().getBoolean(MAX_HEALTH_CONTAINER_BOUGHT, false);
    }

    public void maxHealthContainerBought() {
        preferencesService.putBoolean(MAX_HEALTH_CONTAINER_BOUGHT, true);
    }

    public boolean isLocationUnlocked(Location location) {
        if (location == Location.LOC1) {
            return true;
        }
        return preferencesService.getPreferences().getBoolean(LOCATION_UNLOCK + location.toString(), false);
    }

    public void saveGame(CurrentGame currentGame) {
        Gson gson = new Gson();
        String json = gson.toJson(currentGame);
        preferencesService.putString(SAVED_GAME, json);
    }

    public CurrentGame getSavedGame() {
        Gson gson = new Gson();
        String json = preferencesService.getPreferences().getString(SAVED_GAME, "");
        if (json.isEmpty()) {
            return null;
        }
        CurrentGame obj = gson.fromJson(json, CurrentGame.class);
        return obj;
    }

    public void unlockLocation(Location location) {
        preferencesService.putBoolean(LOCATION_UNLOCK + location.toString(), true);
    }

}
