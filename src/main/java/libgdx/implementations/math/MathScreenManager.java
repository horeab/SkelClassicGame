package libgdx.implementations.math;

import libgdx.screen.AbstractScreenManager;

public class MathScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(MathScreenTypeEnum.CAMPAIGN_SCREEN);
//        showGameScreen(MathCampaignLevelEnum.LEVEL_0_1);
    }

    public void showGameScreen(MathCampaignLevelEnum mathCampaignLevelEnum) {
        showScreen(MathScreenTypeEnum.GAME_SCREEN, mathCampaignLevelEnum);
    }
}
