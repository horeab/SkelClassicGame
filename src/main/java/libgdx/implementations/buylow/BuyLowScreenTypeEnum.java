package libgdx.implementations.buylow;

import libgdx.implementations.buylow.screens.BuyLowGameScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum BuyLowScreenTypeEnum implements ScreenType {

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BuyLowGameScreen();
        }
    },

}