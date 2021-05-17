package libgdx.implementations.math;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.SkelClassicGameIdEnum;
import libgdx.screen.AbstractScreen;

public class MathGame extends CampaignGame<AppInfoService,
        MathMainDependencyManager,
        MathDependencyManager,
        AbstractScreen,
        MathScreenManager,
        SkelClassicGameIdEnum
        > {

    public MathGame(AppInfoService appInfoService) {
        super(appInfoService, new MathMainDependencyManager());
    }

    public MathDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static MathGame getInstance() {
        return (MathGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        MathScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
