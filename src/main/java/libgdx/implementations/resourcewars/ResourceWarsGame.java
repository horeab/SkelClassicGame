package libgdx.implementations.resourcewars;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class ResourceWarsGame extends CampaignGame<AppInfoService,
        ResourceWarsMainDependencyManager,
        ResourceWarsDependencyManager,
        AbstractScreen,
        ResourceWarsScreenManager,
        GameIdEnum
        > {

    public ResourceWarsGame(AppInfoService appInfoService) {
        super(appInfoService, new ResourceWarsMainDependencyManager());
    }

    public ResourceWarsDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static ResourceWarsGame getInstance() {
        return (ResourceWarsGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        ResourceWarsScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
