package libgdx.implementations.fillcolor;

import libgdx.implementations.fillcolor.screens.FillColorGameScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum FillColorScreenTypeEnum implements ScreenType {

    MAIN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new FillColorGameScreen(null) {
            };
        }
    },

}