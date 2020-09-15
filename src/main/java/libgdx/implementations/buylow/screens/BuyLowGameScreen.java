package libgdx.implementations.buylow.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.LinkedHashMap;
import java.util.Map;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.buylow.BuyLowScreenManager;
import libgdx.implementations.buylow.spec.BuyLowResource;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class BuyLowGameScreen extends AbstractScreen<BuyLowScreenManager> {

    private static final int START_BUDGET = 100;
    private static final int MAX_DAYS = 200;
    private static final int MAX_INVENTORY = 100;

    private int inventory = 0;
    private int budget = START_BUDGET;
    private int days = MAX_DAYS;
    private Map<BuyLowResource, Integer> resAmount = new LinkedHashMap<>();


    private Table allTable;
    private MyButton hoverBackButton;

    public BuyLowGameScreen() {
        init();
    }

    private void init() {
        for (BuyLowResource res : BuyLowResource.values()) {
            resAmount.put(res, 0);
        }

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
        createAllRes();
    }

    private void createAllRes() {
        for (Map.Entry<BuyLowResource, Integer> e : resAmount.entrySet()) {
            allTable.add(createResTable(e.getKey(), e.getValue(), e.getKey().getPrice()))
                    .width(ScreenDimensionsManager.getScreenWidthValue(100))
                    .height(itemHeight()).row();
        }
    }

    private float itemHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(20);
    }

    private float itemWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(65);
    }

    private Table createResTable(BuyLowResource res, int amount, int currentPrice) {
        float itemHeight = itemHeight();
        float itemWidth = itemWidth();
        float infoHeight = itemHeight / 1.5f;
        Table table = new Table();
        Table head = new Table();
        MyWrappedLabel resName = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(res.name()).build());
        MyWrappedLabel resAmount = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(amount + "").build());
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        head.add(resName).width(itemWidth / 2).padRight(dimen * 2);
        head.add(resAmount).width(itemWidth / 2).padLeft(dimen * 2);
        Table info = new Table();
        info.add(GraphicUtils.getImage(MainResource.heart_full)).width(infoHeight).height(infoHeight);
        Table priceAmount = new Table();
        MyWrappedLabel price = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(currentPrice + "").build());
        MyWrappedLabel amountAfford = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE))).setText(123 + "").build());
        priceAmount.add(price).row();
        priceAmount.add(amountAfford);
        info.add(priceAmount).height(infoHeight).width(itemWidth - infoHeight);
        Table item = new Table();
        item.add(head).height(itemHeight - infoHeight).width(itemWidth).row();
        item.add(info).height(infoHeight).width(itemWidth);
        Table buySell = new Table();
        MyButton buyBtn = new ButtonBuilder().setFixedButtonSize(SkelClassicButtonSize.BUYLOW_SELLBUY_BTN).setText("Buy").build();
        MyButton sellBtn = new ButtonBuilder().setFixedButtonSize(SkelClassicButtonSize.BUYLOW_SELLBUY_BTN).setText("Sell").build();
        buySell.add(buyBtn).height(buyBtn.getHeight()).width(buyBtn.getWidth()).row();
        buySell.add(sellBtn).height(sellBtn.getHeight()).width(sellBtn.getWidth());
        table.add(item).width(itemWidth);
        table.add(buySell).width(ScreenDimensionsManager.getScreenWidth() - itemWidth).height(itemHeight);
        buySell.setBackground(GraphicUtils.getNinePatch(MainResource.btn_lowcolor_down));
        return table;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
