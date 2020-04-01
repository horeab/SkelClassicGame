package libgdx.implementations.memory;

import libgdx.screen.AbstractScreenManager;

public class MemoryScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(MemoryScreenTypeEnum.CAMPAIGN_SCREEN);
        showGameScreen(0);
    }

    public void showGameScreen(int gameLevel) {
        showScreen(MemoryScreenTypeEnum.GAME_SCREEN, gameLevel);
    }
}
