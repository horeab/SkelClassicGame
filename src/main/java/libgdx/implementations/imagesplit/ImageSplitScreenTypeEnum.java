package libgdx.implementations.imagesplit;

import libgdx.implementations.imagesplit.screens.ImageSplitGameScreen;
import libgdx.implementations.imagesplit.screens.ImageSplitMainMenuScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum ImageSplitScreenTypeEnum implements ScreenType {

    MAIN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ImageSplitMainMenuScreen();
        }
    },
    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ImageSplitGameScreen();
        }
    },

}