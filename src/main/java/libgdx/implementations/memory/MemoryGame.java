package libgdx.implementations.memory;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class MemoryGame extends CampaignGame<AppInfoService,
        MemoryMainDependencyManager,
        MemoryDependencyManager,
        AbstractScreen,
        MemoryScreenManager,
        GameIdEnum
        > {

    public MemoryGame(AppInfoService appInfoService) {
        super(appInfoService, new MemoryMainDependencyManager());
    }

    public MemoryDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static MemoryGame getInstance() {
        return (MemoryGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        MemoryScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
