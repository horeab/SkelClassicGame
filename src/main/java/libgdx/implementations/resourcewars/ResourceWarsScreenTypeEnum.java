package libgdx.implementations.resourcewars;

import libgdx.implementations.resourcewars.screens.ResourceWarsGameScreen;
import libgdx.implementations.resourcewars.screens.ResourceWarsMainMenuScreen;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum ResourceWarsScreenTypeEnum implements ScreenType {

    MAINMENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ResourceWarsMainMenuScreen();
        }
    },
    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ResourceWarsGameScreen((CurrentGame) params[0]);
        }
    },

}