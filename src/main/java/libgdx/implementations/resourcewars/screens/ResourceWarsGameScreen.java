package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfig;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.creator.ContainerCreator;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class ResourceWarsGameScreen extends AbstractScreen<ResourceWarsScreenManager> {


    private ContainerCreator containerCreator = new ContainerCreator();
    private int amount = 0;
    private MyWrappedLabel amountLabel;

    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
    }

    private void addAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        Table gameTable = new Table();
        float invMarketWidth = ScreenDimensionsManager.getScreenWidthValue(40);
        gameTable.add(createScrollTable(containerCreator.inventoryItem(invMarketWidth))).width(invMarketWidth);
        gameTable.add(createNumberPicker()).width(ScreenDimensionsManager.getScreenWidthValue(20));
        gameTable.add(createScrollTable(containerCreator.marketItem(invMarketWidth))).width(invMarketWidth);
        table.add(gameTable).fill();
        addActor(table);
    }

    private Table createNumberPicker() {
        Table table = new Table();
        MyButton increaseBtn = new ButtonBuilder().setText("+").build();
        increaseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                amount++;
                amountLabel.setText(amount + "");
            }
        });
        table.add(increaseBtn).row();
        amountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2))
                .setText(amount + "").build());
        table.add(amountLabel).row();
        MyButton decreaseBtn = new ButtonBuilder().setText("-").build();
        decreaseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (amount > 0) {
                    amount--;
                    amountLabel.setText(amount + "");
                }
            }
        });
        table.add(decreaseBtn);
        return table;
    }

    private Table createScrollTable(Table tableToAdd) {
        Table itemTable = new Table();
        itemTable.add(tableToAdd);
        ScrollPane scrollPane = new ScrollPane(itemTable);
        scrollPane.setScrollingDisabled(true, false);
        Table allTable = new Table();
        allTable.add(scrollPane).expand();
        return allTable;
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
