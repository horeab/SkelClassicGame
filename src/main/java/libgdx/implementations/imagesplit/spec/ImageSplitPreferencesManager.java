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

    public void putMaxSeconds(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum, int seconds) {
        preferencesService.putInteger(getSecondsKey(gameType, campaignLevelEnum), seconds);
    }

    public void putMaxMoves(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum, int moves) {
        preferencesService.putInteger(getMovesKey(gameType, campaignLevelEnum), moves);
    }

    private String getSecondsKey(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return getLevelKey(gameType, campaignLevelEnum) + "_SECONDS";
    }

    private String getMovesKey(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return getLevelKey(gameType, campaignLevelEnum) + "_MOVES";
    }

    private String getLevelKey(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        return gameType.name() + ":" + campaignLevelEnum.getName();
    }

}
