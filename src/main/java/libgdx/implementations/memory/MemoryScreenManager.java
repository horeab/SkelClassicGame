package libgdx.implementations.memory;

import libgdx.implementations.memory.spec.CurrentGame;
import libgdx.screen.AbstractScreenManager;

public class MemoryScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
//        showScreen(MemoryScreenTypeEnum.CAMPAIGN_SCREEN);
        showGameScreen();
    }

    public void showGameScreen() {
        showScreen(MemoryScreenTypeEnum.GAME_SCREEN);
    }
}
