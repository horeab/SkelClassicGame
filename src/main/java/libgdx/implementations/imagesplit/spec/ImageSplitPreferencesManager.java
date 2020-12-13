package libgdx.implementations.imagesplit.spec;

import libgdx.implementations.imagesplit.ImageSplitCampaignLevelEnum;
import libgdx.preferences.PreferencesService;

public class ImageSplitPreferencesManager {

    private static String SHARED_PREFS_NAME = "ImageSplitPreferencesManager";


    private PreferencesService preferencesService = new PreferencesService(SHARED_PREFS_NAME);

    public ImageSplitPreferencesManager() {
//        preferencesService.clear();
    }

    public int getMaxSeconds(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return preferencesService.getPreferences().getInteger(getSecondsKey(gameType, campaignLevelEnum));
    }

    public int getMaxMoves(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return preferencesService.getPreferences().getInteger(getMovesKey(gameType, campaignLevelEnum));
    }

    public void putMaxMoves(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum, int moves) {
        preferencesService.putInteger(getMovesKey(gameType, campaignLevelEnum), moves);
    }

    public int getShowAdPopupValue() {
        return preferencesService.getPreferences().getInteger(getAdPopupValueKey(), 0);
    }

    public void incrementWithAdPopupValue(int val) {
        preferencesService.putInteger(getAdPopupValueKey(), getShowAdPopupValue() + val);
    }

    public void putMaxSeconds(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum, int seconds) {
        preferencesService.putInteger(getSecondsKey(gameType, campaignLevelEnum), seconds);
    }

    public void putTutorialPlayed(ImageSplitGameType gameType) {
        preferencesService.putBoolean(getTutorialKey(gameType), true);
    }

    public boolean isTutorialPlayed(ImageSplitGameType gameType) {
        return preferencesService.getPreferences().getBoolean(getTutorialKey(gameType), false);
    }

    private String getTutorialKey(ImageSplitGameType gameType) {
        return gameType.name() + "_TUTORIAL";
    }

    private String getSecondsKey(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return getLevelKey(gameType, campaignLevelEnum) + "_SECONDS";
    }

    private String getMovesKey(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return getLevelKey(gameType, campaignLevelEnum) + "_MOVES";
    }

    private String getAdPopupValueKey() {
        return "WON_GAMES";
    }


    private String getLevelKey(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return gameType.name() + ":" + campaignLevelEnum.getName();
    }

}
