package libgdx.implementations.applepie;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.SkelClassicGameIdEnum;
import libgdx.screen.AbstractScreen;

public class ApplePieGame extends CampaignGame<AppInfoService,
        ApplePieMainDependencyManager,
        ApplePieDependencyManager,
        AbstractScreen,
        ApplePieScreenManager,
        SkelClassicGameIdEnum
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
