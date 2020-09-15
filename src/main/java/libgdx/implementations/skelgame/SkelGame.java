package libgdx.implementations.skelgame;


import libgdx.campaign.CampaignGame;
import libgdx.constants.GameIdEnum;
import libgdx.game.Game;
import libgdx.game.ScreenManager;
import libgdx.game.external.AppInfoService;
import libgdx.screen.AbstractScreen;

public class SkelGame extends CampaignGame<AppInfoService,
        SkelGameMainDependencyManager,
        SkelGameDependencyManager,
        AbstractScreen,
        ScreenManager,
        GameIdEnum
        > {

    public SkelGame(AppInfoService appInfoService) {
        super(appInfoService, new SkelGameMainDependencyManager());
    }

    public SkelGameDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static SkelGame getInstance() {
        return (SkelGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        ScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
