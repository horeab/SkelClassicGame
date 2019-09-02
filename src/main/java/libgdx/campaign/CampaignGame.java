package libgdx.campaign;


import libgdx.dbapi.UniqueDbOperationContainer;
import libgdx.game.*;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.game.external.LoginService;
import libgdx.implementations.skelgame.SkelGameDependencyManager;
import libgdx.implementations.skelgame.SkelGameMainDependencyManager;
import libgdx.login.GuestUserLoginService;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public abstract class CampaignGame<
        TAppInfoService extends AppInfoService,
        TMainDependencyManager extends MainDependencyManager,
        TSubGameDependencyManager extends CampaignGameDependencyManager,
        TScreen extends AbstractScreen,
        TScreenManager extends AbstractScreenManager,
        TGameId extends Enum & GameId> extends Game<TAppInfoService, TMainDependencyManager, TSubGameDependencyManager, TScreen, TScreenManager, TGameId> {

    public CampaignGame(FacebookService facebookService,
                        BillingService billingService,
                        TAppInfoService appInfoService,
                        TMainDependencyManager mainDependencyManager) {
        super(facebookService, billingService, appInfoService, mainDependencyManager);
    }

    @Override
    public void create() {
        super.create();
        loginService = createLoginService();
        UniqueDbOperationContainer.getInstance().clear();
    }

    private LoginService createLoginService() {
        return new GuestUserLoginService();
    }

    @Override
    public TSubGameDependencyManager getSubGameDependencyManager() {
        return super.getSubGameDependencyManager();
    }

    public static CampaignGame getInstance() {
        return (CampaignGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        TScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
