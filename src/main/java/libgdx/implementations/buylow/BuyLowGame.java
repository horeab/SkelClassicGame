package libgdx.implementations.buylow;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class BuyLowGame extends CampaignGame<AppInfoService,
        BuyLowMainDependencyManager,
        BuyLowDependencyManager,
        AbstractScreen,
        BuyLowScreenManager,
        GameIdEnum
        > {

    public BuyLowGame(AppInfoService appInfoService) {
        super(appInfoService, new BuyLowMainDependencyManager());
    }

    public BuyLowDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static BuyLowGame getInstance() {
        return (BuyLowGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        BuyLowScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
