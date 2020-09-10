package libgdx.implementations.applepie;

import libgdx.screen.AbstractScreenManager;

public class ApplePieScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showGameScreen();
    }

    public void showGameScreen() {
        showScreen(ApplePieScreenTypeEnum.GAME_SCREEN);
    }
}
