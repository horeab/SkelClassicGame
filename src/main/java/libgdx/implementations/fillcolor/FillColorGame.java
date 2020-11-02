package libgdx.implementations.fillcolor;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class FillColorGame extends CampaignGame<AppInfoService,
        FillColorMainDependencyManager,
        FillColorDependencyManager,
        AbstractScreen,
        FillColorScreenManager,
        GameIdEnum
        > {

    public FillColorGame(AppInfoService appInfoService) {
        super(appInfoService, new FillColorMainDependencyManager());
    }

    public FillColorDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static FillColorGame getInstance() {
        return (FillColorGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        FillColorScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
