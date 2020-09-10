package libgdx.implementations.applepie;

import libgdx.implementations.applepie.screens.ApplePieGameScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum ApplePieScreenTypeEnum implements ScreenType {

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ApplePieGameScreen();
        }
    },

}