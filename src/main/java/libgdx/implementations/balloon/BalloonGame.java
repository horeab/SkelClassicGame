package libgdx.implementations.balloon;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class BalloonGame extends CampaignGame<AppInfoService,
        BalloonMainDependencyManager,
        BalloonDependencyManager,
        AbstractScreen,
        BalloonScreenManager,
        GameIdEnum
        > {

    public BalloonGame(AppInfoService appInfoService) {
        super(appInfoService, new BalloonMainDependencyManager());
    }

    public BalloonDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static BalloonGame getInstance() {
        return (BalloonGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        BalloonScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
