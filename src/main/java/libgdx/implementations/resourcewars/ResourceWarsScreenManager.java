package libgdx.implementations.resourcewars;

import libgdx.screen.AbstractScreenManager;

public class ResourceWarsScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(ResourceWarsScreenTypeEnum.GAME_SCREEN);
//        showGameScreen(GameLevel._3);
    }

}
