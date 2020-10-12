package libgdx.implementations.kidlearn;

import libgdx.implementations.kidlearn.screens.KidLearnGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMainMenuScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum KidLearnScreenTypeEnum implements ScreenType {

    MAIN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new KidLearnMainMenuScreen();
        }
    },
    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new KidLearnGameScreen();
        }
    },

}