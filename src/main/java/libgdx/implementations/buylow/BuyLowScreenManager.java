package libgdx.implementations.buylow;

import libgdx.screen.AbstractScreenManager;

public class BuyLowScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(BuyLowScreenTypeEnum.MAIN_SCREEN);
    }

    public void showGameScreen() {
        showScreen(BuyLowScreenTypeEnum.GAME_SCREEN);
    }
}
