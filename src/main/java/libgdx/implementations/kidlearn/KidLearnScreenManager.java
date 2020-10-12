package libgdx.implementations.kidlearn;

import libgdx.screen.AbstractScreenManager;

public class KidLearnScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(KidLearnScreenTypeEnum.MAIN_SCREEN);
        showGameScreen();
    }

    public void showGameScreen() {
        showScreen(KidLearnScreenTypeEnum.GAME_SCREEN);
    }
}
