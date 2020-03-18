package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.logic.InGameStoreManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public class ResourceWarsGameScreen extends AbstractScreen<ResourceWarsScreenManager> {


    public static final float INV_MARKET_HEIGHT = ScreenDimensionsManager.getScreenHeightValue(68);
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
        gameTable.add(getInvMarket("Inventory",
                containerManager.createScrollTable(containerManager.createInventory(), INV_MARKET_HEIGHT))).height(INV_MARKET_HEIGHT).width(INVMARKETWIDTH);
        float numberPickerWidth = ScreenDimensionsManager.getScreenWidth() - INVMARKETWIDTH * 2;
        gameTable.add(containerManager.createNumberPickerColumn(numberPickerWidth)).height(INV_MARKET_HEIGHT).height(INV_MARKET_HEIGHT).width(numberPickerWidth);
        gameTable.add(getInvMarket("Market",
                containerManager.createScrollTable(containerManager.createMarket(), INV_MARKET_HEIGHT))).height(INV_MARKET_HEIGHT).width(INVMARKETWIDTH);
        table.add(gameTable).row();
        table.add(containerManager.createFooter());
        addActor(table);
    }

    private Table getInvMarket(String headerText, Table scrollTable) {
        MyWrappedLabel headerTextLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontScale(FontManager.getNormalBigFontDim()).setText(headerText).setSingleLineLabel().build());
        headerTextLabel.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        Table table = new Table();
        table.add(headerTextLabel).row();
        table.add(scrollTable);
        return table;
    }


    @Override
    public void onBackKeyPress() {
        new InGameStoreManager().saveGame(currentGame);
        screenManager.showMainScreen();
    }
}
