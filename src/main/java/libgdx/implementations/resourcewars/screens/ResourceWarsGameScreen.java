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
    private int amount = 0;
    private MyWrappedLabel amountLabel;
    private CurrentGame currentGame;
    public static String BUYSELLBTN_NAME = "BUYSELLBTN_NAME";

    public ResourceWarsGameScreen() {
        this.currentGame = new CurrentGame();
        this.containerManager =  new ContainerManager(currentGame);
    }

    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
    }

    private void addAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        float headerWidth = ScreenDimensionsManager.getScreenWidth() / 1.1f;
        table.add(containerManager.createHeader(headerWidth)).width(headerWidth).row();
        Table gameTable = new Table();
        float invMarketWidth = ScreenDimensionsManager.getScreenWidthValue(38);
        float invMarketHeight = ScreenDimensionsManager.getScreenHeightValue(75);
        gameTable.add(containerManager.createScrollTable(containerManager.inventoryItem(currentGame.getMyInventory(), currentGame.getMarket(), invMarketWidth), "Inventory", invMarketHeight)).width(invMarketWidth);
        float numberPickerWidth = ScreenDimensionsManager.getScreenWidth() - invMarketWidth * 2;
        gameTable.add(createNumberPickerColumn(numberPickerWidth)).height(invMarketHeight).width(numberPickerWidth);
        gameTable.add(containerManager.createScrollTable(containerManager.marketItem(currentGame.getMarket(), invMarketWidth), "Market", invMarketHeight)).width(invMarketWidth);
        table.add(gameTable).row();
        table.add(containerManager.createFooter(ScreenDimensionsManager.getScreenWidth()));
        addActor(table);
    }

    private Table createNumberPickerColumn(float numberPickerWidth) {
        Table table = new Table();
        List<Integer> modifValues = Arrays.asList(1, 10, 500);
        for (Integer val : modifValues) {
            table.add(createModifyAmountBtn(val, -1, true)).row();
        }
        table.add(createModifyAmountBtn(0, 2, true)).width(numberPickerWidth).row();
        amountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2))
                .setText(amount + "").build());
        table.add(amountLabel).row();
        MyButton buySellBtn = new ButtonBuilder().setButtonName(BUYSELLBTN_NAME).setSingleLineText("Buy", FontManager.getNormalBigFontDim()).build();
        table.add(buySellBtn).row();
        table.add(createModifyAmountBtn(0, 2, false)).row();
        Collections.reverse(modifValues);
        for (Integer val : modifValues) {
            table.add(createModifyAmountBtn(val, -1, false)).row();
        }
        return table;
    }

    private int getAmountYouAfforAndHaveSpaceFor(int amountYouAfford) {
        int amount = amountYouAfford;

        if (amountYouAfford > currentGame.getMyInventory().getContainerSpaceLeft()) {
            amount = currentGame.getMyInventory().getContainerSpaceLeft();
        }

        return amount;
    }

    private MyButton createModifyAmountBtn(int modifAmount, int mult, boolean positive) {
        String text = getModifyAmountBtnText(modifAmount, mult, positive);
        MyButton btn = new ButtonBuilder()
                .setSingleLineText(text, FontManager.getSmallFontDim())
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mult == -1) {
                    if (positive) {
                        amount = amount + modifAmount;
                    } else {
                        amount = amount - modifAmount;
                    }
                } else {
                    if (positive) {
                        amount = amount * mult;
                    } else {
                        amount = amount / mult;
                    }
                }
                if (amount < 0) {
                    amount = 0;
                }

                int amountYouAffordAndHaveSpaceFor = ResourceTransactionsManager.getResourceAmountYouAfford(
                        containerManager.getSelectedResource().getPrice(), currentGame.getMyInventory().getBudget());
                amountYouAffordAndHaveSpaceFor = getAmountYouAfforAndHaveSpaceFor(amountYouAffordAndHaveSpaceFor);
                if (amount > amountYouAffordAndHaveSpaceFor) {
                    amount = amountYouAffordAndHaveSpaceFor;
                }
                amountLabel.setText(amount + "");
            }
        });
        return btn;
    }

    private String getModifyAmountBtnText(int modifAmount, int mult, boolean positive) {
        return mult == -1 ? ((positive ? "+" : "-") + modifAmount) : ((positive ? "*" : "/") + mult);
    }


    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
