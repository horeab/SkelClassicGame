package libgdx.implementations.iqtest;


import libgdx.constants.GameIdEnum;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.login.GuestUserLoginService;
import libgdx.screen.AbstractScreen;

public class IqTestGame extends Game<AppInfoService,
        IqTestMainDependencyManager,
        IqTestDependencyManager,
        AbstractScreen,
        IqTestScreenManager,
        GameIdEnum
        > {

    public IqTestGame(AppInfoService appInfoService) {
        super(appInfoService, new IqTestMainDependencyManager());
    }

    @Override
    public void create() {
        super.create();
        loginService = new GuestUserLoginService();
    }

    public IqTestDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static IqTestGame getInstance() {
        return (IqTestGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        IqTestScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
