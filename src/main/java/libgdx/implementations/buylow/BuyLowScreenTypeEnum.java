package libgdx.implementations.buylow;

import libgdx.implementations.buylow.screens.BuyLowGameScreen;
import libgdx.implementations.buylow.screens.BuyLowMainMenuScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum BuyLowScreenTypeEnum implements ScreenType {

    MAIN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BuyLowMainMenuScreen();
        }
    },
    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BuyLowGameScreen();
        }
    },

}