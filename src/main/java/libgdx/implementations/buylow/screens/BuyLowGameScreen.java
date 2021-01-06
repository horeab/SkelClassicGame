package libgdx.implementations.buylow.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.buylow.BuyLowScreenManager;
import libgdx.implementations.buylow.BuyLowSpecificResource;
import libgdx.implementations.buylow.spec.BuyLowHighScorePreferencesManager;
import libgdx.implementations.buylow.spec.BuyLowLevelFinishedPopup;
import libgdx.implementations.buylow.spec.BuyLowResource;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class BuyLowGameScreen extends AbstractScreen<BuyLowScreenManager> {

    private static final int START_BUDGET = 1000;
    private static final int MAX_DAYS = 200;
    private static final int MAX_INVENTORY = 100;

    private int budget = START_BUDGET;
    private MutableLong countdownAmountMillis = new MutableLong(MAX_DAYS * 1000);
    private ScheduledExecutorService executorService;
    private boolean countdownFinished = false;
    private Map<BuyLowResource, Integer> resAmount = new LinkedHashMap<>();
    private Map<BuyLowResource, Integer> resCurrentPrice = new LinkedHashMap<>();


    private Table allTable;
    private MyButton hoverBackButton;

    public BuyLowGameScreen() {
        init();
    }

    private void init() {
        for (BuyLowResource res : BuyLowResource.values()) {
            resAmount.put(res, 0);
            resCurrentPrice.put(res, res.getPrice());
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void buildStage() {
        createAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        addActor(allTable);
        if (hoverBackButton != null) {
            hoverBackButton.toFront();
        }
        refreshAllTable();
        addAction(Actions.sequence(Actions.delay(1f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                countdownProcess();
            }
        })));
    }

    private void refreshAllTable() {
        allTable.clear();
        createInvTable();
        allTable.row();
        createAllRes();
    }

    private void createInvTable() {
        Table table = new Table();
        Table head = new Table();
        float screenWidthValue = ScreenDimensionsManager.getScreenWidthValue(100);
        MyWrappedLabel budgetLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(screenWidthValue).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText(SkelGameLabel.l_budget.getText() + ": " + formatNrToCurrencyWithDollar(budget)).build());
        MyWrappedLabel daysLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(screenWidthValue).setFontConfig(new FontConfig(FontColor.RED.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText(SkelGameLabel.l_remaining_days.getText() + ": " + getDaysToDisplay() + "").build());
        budgetLabel.setAlignment(Align.left);
        daysLabel.setAlignment(Align.left);
        head.add(budgetLabel).align(Align.left).row();
        head.add(daysLabel).align(Align.left);
        table.add(head).padTop(MainDimen.vertical_general_margin.getDimen() * 2).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        float invRowHeight = ScreenDimensionsManager.getScreenHeightValue(5);
        table.add(createInvInfoTable(SkelGameLabel.l_inventory.getText() ,
                getTotalInv() + "/" + MAX_INVENTORY,
                formatNrToCurrencyWithDollar(getInvValue()),
                getInvSpaceFontConfig(), new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        getInvValueFontSize(), 3f)))
                .height(invRowHeight).row();
        allTable.add(table).width(screenWidthValue / 1.1f)
                .height(ScreenDimensionsManager.getScreenHeight() - itemHeight() * resAmount.size());
    }

    private long getDaysToDisplay() {
        long days = countdownAmountMillis.getValue() / 1000;
        return days < 0 ? 0 : days;
    }

    private FontConfig getInvSpaceFontConfig() {
        FontColor fontColor = FontColor.WHITE;
        float fontSize = getInvValueFontSize();
        Color borderColor = Color.BLUE;
        if (getTotalInv() >= MAX_INVENTORY) {
            fontColor = FontColor.RED;
            borderColor = FontColor.DARK_RED.getColor();
            fontSize = fontSize * 1.3f;
        }
        return new FontConfig(fontColor.getColor(), borderColor, fontSize, 3f);
    }

    private float getInvValueFontSize() {
        return FontConfig.FONT_SIZE * 1.2f;
    }

    private Table createInvInfoTable(String labelText, String spaceLabelValue, String valueLabelValue, FontConfig spaceFontConfig, FontConfig valueFontConfig) {
        float screenWidthValue = ScreenDimensionsManager.getScreenWidthValue(97);
        Table invTable = new Table();
        MyWrappedLabel invSpace = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(screenWidthValue / 3).setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(labelText).build());
        MyWrappedLabel invSpaceValue = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(screenWidthValue / 3).setFontConfig(spaceFontConfig).setText(spaceLabelValue).build());
        MyWrappedLabel invValueValue = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(screenWidthValue / 3).setFontConfig(valueFontConfig).setText(valueLabelValue).build());
        invTable.add(invSpace).align(Align.left);
        invTable.add(invSpaceValue).align(Align.center);
        invTable.add(invValueValue).align(Align.right);
        invSpace.setAlignment(Align.left);
        invSpaceValue.setAlignment(Align.center);
        invValueValue.setAlignment(Align.right);
        return invTable;
    }

    public static String formatNrToCurrencyWithDollar(int nr) {
        return formatNrToCurrency(nr) + " $";
    }

    private static String formatNrToCurrency(int nr) {
        char[] nrArray = Integer.toString(nr).toCharArray();
        int j = 0;
        String result = Integer.toString(nr);
        if (nrArray.length > 3) {
            result = "";
            for (int i = nrArray.length - 1; i >= 0; i--) {
                result = result + nrArray[i];
                if (j == 2) {
                    result = result + ".";
                    j = 0;
                } else {
                    j++;
                }
            }
            result = new StringBuffer(result).reverse().toString();
        }
        if (result.toCharArray()[0] == '.') {
            result = result.substring(1);
        }
        return result;
    }

    private void createAllRes() {
        int i = 0;
        for (Map.Entry<BuyLowResource, Integer> e : resAmount.entrySet()) {
            allTable.add(createResTable(i, e.getKey(), resCurrentPrice.get(e.getKey())))
                    .width(ScreenDimensionsManager.getScreenWidthValue(100))
                    .height(itemHeight()).row();
            i++;
        }
    }

    private float itemHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(18);
    }

    private float itemWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(65);
    }

    private Table createResTable(int index, final BuyLowResource res, final int currentPrice) {
        float itemHeight = itemHeight();
        float itemWidth = itemWidth();
        float infoHeight = itemHeight / 1.5f;
        Table table = new Table();
        Table head = new Table();
        String resNameEnum = res.name().toLowerCase();
        MyWrappedLabel resName = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 3f)).setText(StringUtils.capitalize(SkelGameLabel.valueOf("l_" + resNameEnum).getText())).build());
        MyWrappedLabel resAmountLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 3f)).setText(this.resAmount.get(res) + "").build());
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        head.add(resName).width(itemWidth / 2.1f).padRight(dimen * 2);
        head.add(resAmountLabel).width(itemWidth / 2.1f).padLeft(dimen * 2);
        Table info = new Table();
        float iconSideDimen = infoHeight / 1.3f;
        info.add(GraphicUtils.getImage(BuyLowSpecificResource.valueOf(resNameEnum))).width(iconSideDimen).height(iconSideDimen);
        Table priceAmount = new Table();
        MyWrappedLabel price = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.LIGHT_GREEN.getColor(), FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.5f), 3f)).setText(formatNrToCurrencyWithDollar(currentPrice) + "").build());
        FontConfig amountAffordGreenConfig = new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(), Math.round(FontConfig.FONT_SIZE), 3f);
        FontConfig amountAffordRedConfig = new FontConfig(RGBColor.WHITE.toColor(), FontColor.RED.getColor(), Math.round(FontConfig.FONT_SIZE), 3f);
        int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(currentPrice);
        MyWrappedLabel amountAfford = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(amountYouAffordAndHaveSpaceFor == 0 ? amountAffordRedConfig : amountAffordGreenConfig).setText(amountYouAffordAndHaveSpaceFor + "").build());
        priceAmount.add(price).row();
        priceAmount.add(amountAfford);
        info.add(priceAmount).height(infoHeight).width(itemWidth - infoHeight);
        Table item = new Table();
        item.add(head).height(itemHeight - infoHeight).width(itemWidth).row();
        item.add(info).height(infoHeight).width(itemWidth);
        Table buySell = new Table();
        SkelClassicButtonSkin skin = SkelClassicButtonSkin.valueOf("BUYLOW_SELLBUY" + index + "_BTN");
        boolean buyBtnDisabled = buyBtnDisabled(currentPrice);
        MyButton buyBtn = new ButtonBuilder().setFontColor(buyBtnDisabled ? FontColor.GRAY : FontColor.BLACK).setButtonSkin(skin).setFixedButtonSize(SkelClassicButtonSize.BUYLOW_SELLBUY_BTN).setText(MainGameLabel.l_buy.getText()).build();
        buyBtn.setDisabled(buyBtnDisabled);
        buyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int amountYouAfford = getAmountYouAfford(currentPrice);
                budget = budget - amountYouAfford * currentPrice;
                resAmount.put(res, resAmount.get(res) + amountYouAfford);
                refreshAllTable();
            }
        });
        boolean sellBtnDisabled = sellBtnDisabled(res);
        MyButton sellBtn = new ButtonBuilder().setFontColor(sellBtnDisabled ? FontColor.GRAY : FontColor.BLACK).setButtonSkin(skin).setFixedButtonSize(SkelClassicButtonSize.BUYLOW_SELLBUY_BTN).setText(SkelGameLabel.l_sell.getText()).build();
        sellBtn.setDisabled(sellBtnDisabled);
        sellBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                budget = budget + resAmount.get(res) * currentPrice;
                resAmount.put(res, 0);
                refreshAllTable();
            }
        });
        buySell.add(buyBtn).height(buyBtn.getHeight()).width(buyBtn.getWidth()).padBottom(MainDimen.vertical_general_margin.getDimen() / 4).row();
        buySell.add(sellBtn).height(sellBtn.getHeight()).width(sellBtn.getWidth());
        table.add(item).width(itemWidth);
        table.add(buySell).width(ScreenDimensionsManager.getScreenWidth() - itemWidth).height(itemHeight);
        table.setBackground(GraphicUtils.getNinePatch(getResBackgr(res, currentPrice)));
        return table;
    }

    private boolean sellBtnDisabled(BuyLowResource res) {
        return this.resAmount.get(res) == 0 || getTotalInv() <= 0;
    }

    private boolean buyBtnDisabled(int currentPrice) {
        return getAmountYouAffordAndHaveSpaceFor(currentPrice) == 0 || getTotalInv() >= MAX_INVENTORY;
    }

    private BuyLowSpecificResource getResBackgr(BuyLowResource res, int currentPrice) {
        BuyLowSpecificResource backr = BuyLowSpecificResource.price_normal;
        if (currentPrice > res.getPrice()) {
            backr = BuyLowSpecificResource.price_up;
        } else if (currentPrice < res.getPrice()) {
            backr = BuyLowSpecificResource.price_down;
        }
        return backr;
    }

    private int getAmountYouAffordAndHaveSpaceFor(int resPrice) {
        int res = getAmountYouAfford(resPrice);
        int containerSpaceLeft = getContainerSpaceLeft();
        if (res > containerSpaceLeft) {
            res = containerSpaceLeft;
        }
        return res;
    }

    private int getContainerSpaceLeft() {
        return MAX_INVENTORY - getTotalInv();
    }

    private int getTotalInv() {
        int inv = 0;
        for (Map.Entry<BuyLowResource, Integer> e : resAmount.entrySet()) {
            inv = inv + e.getValue();
        }
        return inv;
    }

    private int getInvValue() {
        int val = 0;
        for (Map.Entry<BuyLowResource, Integer> e : resAmount.entrySet()) {
            val = val + resCurrentPrice.get(e.getKey()) * e.getValue();
        }
        return val;
    }

    private int getAmountYouAfford(int resourcePrice) {
        int amountYouAfford = budget / resourcePrice;
        int freeSpace = MAX_INVENTORY - getTotalInv();
        if (amountYouAfford > freeSpace) {
            amountYouAfford = freeSpace;
        }
        return amountYouAfford;
    }

    private void changeResPrice() {
        for (Map.Entry<BuyLowResource, Integer> e : resCurrentPrice.entrySet()) {
            int valToChange = e.getKey().getPrice() / 10;
            int rand = new Random().nextInt(3);
            int priceToSet = e.getValue();
            if (rand == 0) {
                priceToSet = priceToSet - valToChange;
            } else if (rand == 1) {
                priceToSet = priceToSet + valToChange;
            }
            if (priceToSet == 0) {
                priceToSet = priceToSet + valToChange;
            }
            e.setValue(priceToSet);
        }
    }

    private void countdownProcess() {
        final int period = 1000;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (countdownAmountMillis.getValue() <= 0) {
                            BuyLowHighScorePreferencesManager preferencesManager = new BuyLowHighScorePreferencesManager();
                            boolean isHighScore = false;
                            if (preferencesManager.getMaxScore() < budget) {
                                preferencesManager.putMaxScore(budget);
                                isHighScore = true;
                            }
                            countdownFinished = true;
                            executorService.shutdown();
                            new BuyLowLevelFinishedPopup(getAbstractScreen(), isHighScore, budget).addToPopupManager();
                        }
                        countdownAmountMillis.subtract(period);
                        changeResPrice();
                        refreshAllTable();
                    }
                });
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
