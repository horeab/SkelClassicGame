package libgdx.implementations.buylow;

import libgdx.screen.AbstractScreenManager;

public class BuyLowScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showGameScreen();
    }

    public void showGameScreen() {
        showScreen(BuyLowScreenTypeEnum.GAME_SCREEN);
    }
}
