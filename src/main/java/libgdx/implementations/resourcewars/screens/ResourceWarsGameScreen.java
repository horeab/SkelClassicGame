package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.logic.GamePreferencesManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class ResourceWarsGameScreen extends AbstractScreen<ResourceWarsScreenManager> {


    private boolean incrDecrIsBeingMade = false;
    private static final float INV_MARKET_HEIGHT = ScreenDimensionsManager.getScreenHeightValue(68);
    public static float INVMARKETWIDTH = ScreenDimensionsManager.getScreenWidthValue(43);
    public static float NUMBERPICKERWIDTH = ScreenDimensionsManager.getScreenWidth() - INVMARKETWIDTH * 2;
    private ContainerManager containerManager;
    private CurrentGame currentGame;
    public static float HEADERWIDTH = ScreenDimensionsManager.getScreenWidth();
    public static String ALLTABLE_NAME = "ALLTABLE_NAME";

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
        table.setName(ALLTABLE_NAME);
        table.setFillParent(true);

        table.add(containerManager.createHeader(containerManager.getBudgetColor())).width(HEADERWIDTH).row();
        Table gameTable = new Table();
        gameTable.add(getInvMarket(SkelGameLabel.l_inventory.getText(),
                containerManager.createScrollTable(containerManager.createInventory(), INV_MARKET_HEIGHT))).height(INV_MARKET_HEIGHT).width(INVMARKETWIDTH);
        gameTable.add(containerManager.createNumberPickerColumn())
                .padLeft(MainDimen.horizontal_general_margin.getDimen())
                .padRight(MainDimen.horizontal_general_margin.getDimen())
                .height(INV_MARKET_HEIGHT).width(NUMBERPICKERWIDTH);
        gameTable.add(getInvMarket(SkelGameLabel.l_market.getText(),
                containerManager.createScrollTable(containerManager.createMarket(), INV_MARKET_HEIGHT))).height(INV_MARKET_HEIGHT).width(INVMARKETWIDTH);
        table.add(gameTable).row();
        table.add(containerManager.createFooter());
        addActor(table);
    }

    private Table getInvMarket(String headerText, Table scrollTable) {
        MyWrappedLabel headerTextLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontScale(FontManager.getSmallFontDim()).setText(headerText).setSingleLineLabel().build());
        Table table = new Table();
        table.add(headerTextLabel).row();
        table.add(scrollTable);
        return table;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (containerManager.getIncreaseAmountButtonPressedSeconds() != null && !incrDecrIsBeingMade) {
            final Integer increaseAmountButtonPressedSeconds = containerManager.getIncreaseAmountButtonPressedSeconds();
            processIncrDecr(new Runnable() {
                @Override
                public void run() {
                    containerManager.setAmount(containerManager.getAmount()
                            + getAmountToIncrDecr(increaseAmountButtonPressedSeconds));
                    containerManager.setIncreaseAmountButtonPressedSeconds(
                            containerManager.getIncreaseAmountButtonPressedSeconds() != null
                                    ? increaseAmountButtonPressedSeconds + 1 : null);
                    incrDecrIsBeingMade = false;
                }
            });
        }
        if (containerManager.getDecreaseAmountButtonPressedSeconds() != null && !incrDecrIsBeingMade) {
            final Integer decreaseAmountButtonPressedSeconds = containerManager.getDecreaseAmountButtonPressedSeconds();
            processIncrDecr(new Runnable() {
                @Override
                public void run() {
                    containerManager.setAmount(containerManager.getAmount()
                            - getAmountToIncrDecr(decreaseAmountButtonPressedSeconds));
                    containerManager.setDecreaseAmountButtonPressedSeconds(containerManager.getDecreaseAmountButtonPressedSeconds() != null
                            ? decreaseAmountButtonPressedSeconds + 1 : null);
                    incrDecrIsBeingMade = false;
                }
            });
        }
    }

    private void processIncrDecr(Runnable runnable) {
        addAction(Actions.sequence(
                Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        incrDecrIsBeingMade = true;
                    }
                }),
                Actions.delay(0.1f),
                Utils.createRunnableAction(runnable)));
    }

    private int getAmountToIncrDecr(Integer amount) {
        if (amount != null) {
            int res = Math.round(amount / 1.4f);
            return res < 1 ? 1 : res;
        } else {
            return 0;
        }
    }

    @Override
    public void onBackKeyPress() {
        new GamePreferencesManager().saveGame(currentGame);
        screenManager.showMainScreen();
    }
}
