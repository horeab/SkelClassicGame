package libgdx.implementations.balloon;

import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.screen.AbstractScreenManager;

public class BalloonScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
//        showLevelFinishedScreen(new LevelInfo(false, BalloonCampaignLevelEnum.LEVEL_0_0), 5, 3);
//        showCampaignScreen(0);
//        showGameScreen(new LevelInfo(false, BalloonCampaignLevelEnum.LEVEL_1_4));
        showScreen(BalloonScreenTypeEnum.MAINMENU_SCREEN);
    }

    public void showGameScreen(LevelInfo levelInfo) {
        showScreen(BalloonScreenTypeEnum.GAME_SCREEN, levelInfo);
    }

    public void showCampaignScreen(int stageNr) {
        showScreen(BalloonScreenTypeEnum.CAMPAIGN_SCREEN, stageNr);
    }

    public void showLevelFinishedScreen(LevelInfo levelInfo, int player1Score, int player2Score) {
        showScreen(BalloonScreenTypeEnum.LEVELFINISHED_SCREEN, levelInfo, player1Score, player2Score);
    }
}
