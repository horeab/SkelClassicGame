package libgdx.implementations.applepie;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class ApplePieGame extends CampaignGame<AppInfoService,
        ApplePieMainDependencyManager,
        ApplePieDependencyManager,
        AbstractScreen,
        ApplePieScreenManager,
        GameIdEnum
        > {

    public ApplePieGame(AppInfoService appInfoService) {
        super(appInfoService, new ApplePieMainDependencyManager());
    }

    public ApplePieDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static ApplePieGame getInstance() {
        return (ApplePieGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        ApplePieScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
