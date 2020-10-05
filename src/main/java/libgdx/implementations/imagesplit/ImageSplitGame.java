package libgdx.implementations.imagesplit;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class ImageSplitGame extends CampaignGame<AppInfoService,
        ImageSplitMainDependencyManager,
        ImageSplitDependencyManager,
        AbstractScreen,
        ImageSplitScreenManager,
        GameIdEnum
        > {

    public ImageSplitGame(AppInfoService appInfoService) {
        super(appInfoService, new ImageSplitMainDependencyManager());
    }

    public ImageSplitDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static ImageSplitGame getInstance() {
        return (ImageSplitGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        ImageSplitScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
