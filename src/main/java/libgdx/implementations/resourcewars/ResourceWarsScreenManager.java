package libgdx.implementations.resourcewars;

import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.screen.AbstractScreenManager;

public class ResourceWarsScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(ResourceWarsScreenTypeEnum.MAINMENU_SCREEN);
//        showScreen(ResourceWarsScreenTypeEnum.GAME_SCREEN, new CurrentGame());
    }

    public void showGameScreen(CurrentGame currentGame) {
        showScreen(ResourceWarsScreenTypeEnum.GAME_SCREEN, currentGame);
    }

}
