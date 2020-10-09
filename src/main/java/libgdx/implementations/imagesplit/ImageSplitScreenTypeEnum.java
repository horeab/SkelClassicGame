package libgdx.implementations.imagesplit;

import libgdx.implementations.imagesplit.screens.ImagePushGameScreen;
import libgdx.implementations.imagesplit.screens.ImageSlideGameScreen;
import libgdx.implementations.imagesplit.screens.ImageSplitMainMenuScreen;
import libgdx.implementations.imagesplit.screens.ImageSwapGameScreen;
import libgdx.implementations.math.MathCampaignLevelEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum ImageSplitScreenTypeEnum implements ScreenType {

    MAIN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ImageSplitMainMenuScreen();
        }
    },
    SLIDE_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ImageSlideGameScreen((ImageSplitCampaignLevelEnum) params[0]);
        }
    },
    PUSH_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ImagePushGameScreen((ImageSplitCampaignLevelEnum) params[0]);
        }
    },
    SWAP_GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new ImageSwapGameScreen((ImageSplitCampaignLevelEnum) params[0]);
        }
    },

}