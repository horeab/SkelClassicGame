package libgdx.implementations.imagesplit;

import libgdx.screen.AbstractScreenManager;

public class ImageSplitScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(ImageSplitScreenTypeEnum.MAIN_SCREEN);
        showGameScreen();
    }

    public void showGameScreen() {
        showScreen(ImageSplitScreenTypeEnum.GAME_SCREEN);
    }
}
