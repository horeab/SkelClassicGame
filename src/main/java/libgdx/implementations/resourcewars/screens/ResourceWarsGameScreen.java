package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.logic.ResourceTransactionsManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.resources.FontManager;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResourceWarsGameScreen extends AbstractScreen<ResourceWarsScreenManager> {


    private ContainerManager containerManager;
    private CurrentGame currentGame;
    public static float INVMARKETWIDTH = ScreenDimensionsManager.getScreenWidthValue(39);
    public static float HEADERWIDTH = ScreenDimensionsManager.getScreenWidth() ;

    public ResourceWarsGameScreen() {
        this.currentGame = new CurrentGame();
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
        float invMarketHeight = ScreenDimensionsManager.getScreenHeightValue(75);
        gameTable.add(containerManager.createScrollTable(containerManager.createInventory(), "Inventory", invMarketHeight)).width(INVMARKETWIDTH);
        float numberPickerWidth = ScreenDimensionsManager.getScreenWidth() - INVMARKETWIDTH * 2;
        gameTable.add(containerManager.createNumberPickerColumn(numberPickerWidth)).height(invMarketHeight).width(numberPickerWidth);
        gameTable.add(containerManager.createScrollTable(containerManager.createMarket(), "Market", invMarketHeight)).width(INVMARKETWIDTH);
        table.add(gameTable).row();
        table.add(containerManager.createFooter(ScreenDimensionsManager.getScreenWidth()));
        addActor(table);
    }



    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
