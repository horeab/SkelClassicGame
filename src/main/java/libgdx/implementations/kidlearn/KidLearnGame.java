package libgdx.implementations.kidlearn;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class KidLearnGame extends CampaignGame<AppInfoService,
        KidLearnMainDependencyManager,
        KidLearnDependencyManager,
        AbstractScreen,
        KidLearnScreenManager,
        GameIdEnum
        > {

    public KidLearnGame(AppInfoService appInfoService) {
        super(appInfoService, new KidLearnMainDependencyManager());
    }

    public KidLearnDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static KidLearnGame getInstance() {
        return (KidLearnGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        KidLearnScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
