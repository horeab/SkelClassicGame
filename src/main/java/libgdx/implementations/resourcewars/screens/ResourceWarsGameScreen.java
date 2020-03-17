package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.logic.InGameStoreManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public class ResourceWarsGameScreen extends AbstractScreen<ResourceWarsScreenManager> {


    private ContainerManager containerManager;
    private CurrentGame currentGame;
    public static float INVMARKETWIDTH = ScreenDimensionsManager.getScreenWidthValue(43);
    public static float HEADERWIDTH = ScreenDimensionsManager.getScreenWidth();

    public ResourceWarsGameScreen(CurrentGame currentGame) {
        this.currentGame = currentGame;
        this.containerManager = new ContainerManager(currentGame);
    }

    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
    }

    private void addAllTable() {
        Table table = new Table();
        table.setFillParent(true);

        table.add(containerManager.createHeader()).width(HEADERWIDTH).row();
        Table gameTable = new Table();
        float invMarketHeight = ScreenDimensionsManager.getScreenHeightValue(68);
        gameTable.add(containerManager.createScrollTable(containerManager.createInventory(), "Inventory", invMarketHeight)).width(INVMARKETWIDTH);
        float numberPickerWidth = ScreenDimensionsManager.getScreenWidth() - INVMARKETWIDTH * 2;
        gameTable.add(containerManager.createNumberPickerColumn(numberPickerWidth)).height(invMarketHeight).width(numberPickerWidth);
        gameTable.add(containerManager.createScrollTable(containerManager.createMarket(), "Market", invMarketHeight)).width(INVMARKETWIDTH);
        table.add(gameTable).row();
        table.add(containerManager.createFooter());
        addActor(table);
    }


    @Override
    public void onBackKeyPress() {
        new InGameStoreManager().saveGame(currentGame);
        screenManager.showMainScreen();
    }
}
