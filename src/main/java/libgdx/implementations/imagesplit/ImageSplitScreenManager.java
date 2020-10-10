package libgdx.implementations.imagesplit;

import libgdx.implementations.imagesplit.spec.ImageSplitGameType;
import libgdx.screen.AbstractScreenManager;

public class ImageSplitScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(ImageSplitScreenTypeEnum.MAIN_SCREEN);
//        showGameScreen(ImageSplitGameType.SLIDE, ImageSplitCampaignLevelEnum.LEVEL_0_0);
    }

    public void showGameScreen(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        switch (gameType) {
            case SWAP:
                showScreen(ImageSplitScreenTypeEnum.SWAP_GAME_SCREEN, campaignLevelEnum);
                break;
            case PUSH:
                showScreen(ImageSplitScreenTypeEnum.PUSH_GAME_SCREEN, campaignLevelEnum);
                break;
            case SLIDE:
                showScreen(ImageSplitScreenTypeEnum.SLIDE_GAME_SCREEN, campaignLevelEnum);
                break;
        }
    }
}