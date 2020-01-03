package libgdx.implementations.balloon;

import libgdx.screen.AbstractScreenManager;

public class BalloonScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showGameScreen(BalloonCampaignLevelEnum.LEVEL_0_0);
//        showScreen(BalloonScreenTypeEnum.MAINMENU_SCREEN);
    }

    public void showGameScreen(BalloonCampaignLevelEnum balloonCampaignLevelEnum) {
        showScreen(BalloonScreenTypeEnum.GAME_SCREEN, balloonCampaignLevelEnum);
    }

    public void showCampaignScreen() {
        showScreen(BalloonScreenTypeEnum.CAMPAIGN_SCREEN);
    }
}
