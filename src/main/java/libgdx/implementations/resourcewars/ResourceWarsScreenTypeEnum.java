package libgdx.implementations.resourcewars;

import libgdx.implementations.resourcewars.screens.ResourceWarsGameScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum ResourceWarsScreenTypeEnum implements ScreenType {

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ResourceWarsGameScreen();
        }
    },

}