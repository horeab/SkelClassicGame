package libgdx.implementations.balloon;

import libgdx.screen.AbstractScreenManager;

public class BalloonScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(BalloonScreenTypeEnum.CAMPAIGN_SCREEN);
//        showScreen(BalloonScreenTypeEnum.MAINMENU_SCREEN);
    }

    public void showGameScreen() {
        showScreen(BalloonScreenTypeEnum.MAINMENU_SCREEN);
    }

    public void showCampaignScreen() {
        showScreen(BalloonScreenTypeEnum.CAMPAIGN_SCREEN);
    }
}
