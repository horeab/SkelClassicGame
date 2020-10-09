package libgdx.implementations.imagesplit;

import libgdx.implementations.imagesplit.screens.ImagePushGameScreen;
import libgdx.implementations.imagesplit.screens.ImageSlideGameScreen;
import libgdx.implementations.imagesplit.screens.ImageSplitMainMenuScreen;
import libgdx.implementations.imagesplit.screens.ImageSwapGameScreen;
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
//            return new ImageSlideGameScreen();
//            return new ImagePushGameScreen();
            return new ImageSwapGameScreen();
        }
    },

}