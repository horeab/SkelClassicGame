package libgdx.implementations.buylow.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

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
import libgdx.implementations.buylow.spec.BuyLowHighScorePreferencesManager;
import libgdx.implementations.buylow.spec.BuyLowLevelFinishedPopup;
import libgdx.implementations.buylow.spec.BuyLowResource;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

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
        createAllRes();
        createInvTable();
    }

    private void createInvTable() {
        Table table = new Table();
        Table head = new Table();
        float horizMargin = MainDimen.horizontal_general_margin.getDimen() * 2;
        float screenWidthValue = ScreenDimensionsManager.getScreenWidthValue(100);
        float headerContainerWidth = screenWidthValue / 2.05f;
        MyWrappedLabel budgetLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(headerContainerWidth).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText("Budget: " + formatNrToCurrencyWithDollar(budget)).build());
        MyWrappedLabel daysLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(headerContainerWidth).setFontConfig(new FontConfig(FontColor.RED.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText("Days: " + (countdownAmountMillis.getValue() / 1000) + "").build());
        MyWrappedLabel invSpace = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(screenWidthValue).setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText("Inventory space: " + getTotalInv() + "/" + MAX_INVENTORY).build());
        MyWrappedLabel invValue = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(screenWidthValue).setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText("Inventory value: " + formatNrToCurrencyWithDollar(getInvValue()) + "").build());
        invSpace.setAlignment(Align.left);
        invValue.setAlignment(Align.left);
        daysLabel.setAlignment(Align.right);
        head.add(budgetLabel).align(Align.left).width(headerContainerWidth);
        head.add(daysLabel).padRight(horizMargin).align(Align.right).width(headerContainerWidth);
        table.add(head).width(screenWidthValue).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        table.add(invSpace).padLeft(horizMargin).align(Align.left).width(screenWidthValue).row();
        table.add(invValue).padLeft(horizMargin).align(Align.left).width(screenWidthValue).row();
        allTable.add(table).width(screenWidthValue)
                .height(ScreenDimensionsManager.getScreenHeight() - itemHeight() * resAmount.size());
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
        return ScreenDimensionsManager.getScreenHeightValue(20);
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
        MyWrappedLabel resName = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(res.name()).build());
        MyWrappedLabel resAmountLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(this.resAmount.get(res) + "").build());
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        head.add(resName).width(itemWidth / 2.1f).padRight(dimen * 2);
        head.add(resAmountLabel).width(itemWidth / 2.1f).padLeft(dimen * 2);
        Table info = new Table();
        float iconSideDimen = infoHeight / 1.3f;
        info.add(GraphicUtils.getImage(MainResource.heart_full)).width(iconSideDimen).height(iconSideDimen);
        Table priceAmount = new Table();
        MyWrappedLabel price = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.5f))).setText(formatNrToCurrencyWithDollar(currentPrice) + "").build());
        MyWrappedLabel amountAfford = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(getAmountYouAffordAndHaveSpaceFor(currentPrice) + "").build());
        priceAmount.add(price).row();
        priceAmount.add(amountAfford);
        info.add(priceAmount).height(infoHeight).width(itemWidth - infoHeight);
        Table item = new Table();
        item.add(head).height(itemHeight - infoHeight).width(itemWidth).row();
        item.add(info).height(infoHeight).width(itemWidth);
        Table buySell = new Table();
        SkelClassicButtonSkin skin = SkelClassicButtonSkin.valueOf("BUYLOW_SELLBUY" + index + "_BTN");
        MyButton buyBtn = new ButtonBuilder().setButtonSkin(skin).setFixedButtonSize(SkelClassicButtonSize.BUYLOW_SELLBUY_BTN).setText("Buy").build();
        buyBtn.setDisabled(getAmountYouAffordAndHaveSpaceFor(currentPrice) == 0);
        buyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int amountYouAfford = getAmountYouAfford(currentPrice);
                budget = budget - amountYouAfford * currentPrice;
                resAmount.put(res, resAmount.get(res) + amountYouAfford);
                refreshAllTable();
            }
        });
        MyButton sellBtn = new ButtonBuilder().setButtonSkin(skin).setFixedButtonSize(SkelClassicButtonSize.BUYLOW_SELLBUY_BTN).setText("Sell").build();
        sellBtn.setDisabled(this.resAmount.get(res) == 0);
        sellBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                budget = budget + resAmount.get(res) * currentPrice;
                resAmount.put(res, 0);
                refreshAllTable();
            }
        });
        buySell.add(buyBtn).height(buyBtn.getHeight()).width(buyBtn.getWidth()).row();
        buySell.add(sellBtn).height(sellBtn.getHeight()).width(sellBtn.getWidth());
        table.add(item).width(itemWidth);
        table.add(buySell).width(ScreenDimensionsManager.getScreenWidth() - itemWidth).height(itemHeight);
        table.setBackground(GraphicUtils.getNinePatch(getResBackgr(res, currentPrice)));
        return table;
    }

    private MainResource getResBackgr(BuyLowResource res, int currentPrice) {
        MainResource backr = MainResource.btn_lowcolor_up;
        if (currentPrice > res.getPrice()) {
            backr = MainResource.btn_menu_down;
        } else if (currentPrice < res.getPrice()) {
            backr = MainResource.btn_menu_up;
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
        return budget / resourcePrice;
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
