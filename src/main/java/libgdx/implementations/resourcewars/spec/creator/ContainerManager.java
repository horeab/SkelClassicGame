package libgdx.implementations.resourcewars.spec.creator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.resourcewars.screens.ResourceWarsGameScreen;
import libgdx.implementations.resourcewars.spec.logic.ResourceTransactionsManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.Market;
import libgdx.implementations.resourcewars.spec.model.resource.AbstractResource;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceMarket;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContainerManager {

    private AbstractResource selectedResource;
    private CurrentGame currentGame;
    private int amount;
    private MyWrappedLabel amountLabel;
    public static String BUYSELLBTN_NAME = "BUYSELLBTN_NAME";
    private static String INVENTORYTABLE_NAME = "INVENTORYTABLE_NAME";
    private static String MARKETTABLE_NAME = "MARKETTABLE_NAME";
    private ResourceTransactionsManager resourceTransactionsManager;
    private static float INV_MARKET_ITEM_HEIGHT = ScreenDimensionsManager.getScreenWidthValue(15);

    public ContainerManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
        this.resourceTransactionsManager = new ResourceTransactionsManager(currentGame);
    }

    public Table createFooter(float tableWidth) {
        Table table = new Table();
        float btnWidth = tableWidth / 3;
        MyButton changeCountryBtn = new ButtonBuilder().setWrappedText("Unites States", btnWidth).build();
        MyButton passDayBtn = new ButtonBuilder().setWrappedText("Pass day", btnWidth).build();
        MyButton storeBtn = new ButtonBuilder().setWrappedText("Store", btnWidth).build();
        table.add(changeCountryBtn).width(btnWidth);
        table.add(passDayBtn).width(btnWidth);
        table.add(storeBtn).width(btnWidth);
        return table;
    }

    public Table createHeader(float tableWidth) {
        Table table = new Table();
        float infoWidth = tableWidth / 2;

        table.setWidth(tableWidth);
        Table tableRow1 = new Table();
        tableRow1.add(createHeaderInfoLabelPair("Budget: ", formatNrToCurrencyWithDollar(currentGame.getMyInventory().getBudget()))).width(infoWidth);
        tableRow1.add(createHeaderInfoLabelPair("Health: ", "95/100")).width(infoWidth);
        table.add(tableRow1).width(tableWidth).row();

        Table tableRow2 = new Table();
        tableRow2.add(createHeaderInfoLabelPair("Space filled: ", "22/100")).width(infoWidth);
        tableRow2.add(createHeaderInfoLabelPair("Reputation: ", "16%")).width(infoWidth);
        table.add(tableRow2).width(tableWidth).row();

        Table tableRow3 = new Table();
        tableRow3.add(createHeaderInfoLabelPair("Days passed: ", "31")).width(infoWidth);
        tableRow3.add(createHeaderInfoLabelPair("Threat: ", "11%")).width(infoWidth);
        table.add(tableRow3).width(tableWidth);

        return table;
    }

    private Table createHeaderInfoLabelPair(String text1, String text2) {
        MyWrappedLabel l1 = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setSingleLineLabel().setFontConfig(new FontConfig(FontConfig.FONT_SIZE / 1.3f))
                        .setText(text1).build());
        MyWrappedLabel l2 = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setSingleLineLabel()
                        .setFontConfig(new FontConfig(FontConfig.FONT_SIZE / 1.1f)).setText(text2).build());
        Table table = new Table();
        l1.getLabels().get(0).setAlignment(Align.left);
        l2.getLabels().get(0).setAlignment(Align.left);
        table.add(l1);
        table.add(l2);
        return table;
    }

    public Table createInventory() {
        float tableWidth = ResourceWarsGameScreen.INVMARKETWIDTH;
        Table inventoryTable = getRoot().findActor(INVENTORYTABLE_NAME);
        if (inventoryTable == null) {
            inventoryTable = new Table();
        }
        inventoryTable.setName(INVENTORYTABLE_NAME);
        inventoryTable.clearChildren();
        Inventory inventory = currentGame.getMyInventory();
        Market market = currentGame.getMarket();
        if (inventory.getAvailableResources().isEmpty()) {
            Table itemTable = new Table();
            itemTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("Empty").build()));
            itemTable.setWidth(tableWidth);
            inventoryTable.add(itemTable).row();
        } else {
            for (ResourceInventory resourceInventory : inventory.getAvailableResources()) {
                Table itemTable = new Table();
                MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                        .setText(resourceInventory.getResourceType().toString()).build());
                MyWrappedLabel actualSellPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(
                        getActualSellPrice(resourceInventory, market)).build());
                MyWrappedLabel pastBuyPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(formatNrToCurrencyWithDollar(resourceInventory.getPrice())).build());
                MyWrappedLabel invAmountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(resourceInventory.getAmount() + "").build());
                itemTable.add(displayName).width(tableWidth / 2);
                itemTable.add(actualSellPrice).width(tableWidth / 2);
                itemTable.row();
                itemTable.add(pastBuyPrice).width(tableWidth / 2);
                itemTable.add(invAmountLabel).width(tableWidth / 2);
                itemTable.setBackground(getNotSelectedBackground());
                itemTable.setTouchable(Touchable.enabled);
                itemTable.setName(resourceInventoryName(resourceInventory));
                itemTable.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!currentGame.getMyInventory().getAvailableResources().isEmpty()) {
                            if (selectedResource != null && selectedResource instanceof ResourceMarket) {
                                setSelectedResource(null);
                            }
                            if (selectedResource != null && resourceInventory.equalsResourceType(selectedResource)) {
                                resetInvMarketItemBackground();
                                setSelectedResource(null);
                            } else {
                                resetInvMarketItemBackground();
                                itemTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                                setSelectedResource(resourceInventory);
                                MyButton sellBuyButton = getRoot().findActor(BUYSELLBTN_NAME);
                                sellBuyButton.getCenterRowLabels().get(0).setText("Sell");
                                if (!ResourceTransactionsManager.isResourceAvailableForSelling(selectedResource.getResourceType(),
                                        currentGame.getMarket().getAvailableResources())) {
                                }
                            }
                            resetAmount();
                        }
                    }
                });
                inventoryTable.add(itemTable).width(tableWidth).height(INV_MARKET_ITEM_HEIGHT).pad(MainDimen.vertical_general_margin.getDimen()).row();
            }
        }
        return inventoryTable;
    }

    private Group getRoot() {
        return Game.getInstance().getAbstractScreen().getRoot();
    }

    private String getActualSellPrice(ResourceInventory resourceInventory, Market market) {
        for (ResourceMarket marketElem : market.getAvailableResources()) {
            if (resourceInventory.equalsResourceType(marketElem)) {
                return formatNrToCurrencyWithDollar(marketElem.getPrice());
            }
        }
        return null;
    }

    public Table createMarket() {
        float tableWidth = ResourceWarsGameScreen.INVMARKETWIDTH;
        Market market = currentGame.getMarket();
        Table table = getRoot().findActor(MARKETTABLE_NAME);
        if (table == null) {
            table = new Table();
        }
        table.setName(MARKETTABLE_NAME);
        table.clearChildren();
        for (ResourceMarket resourceMarket : market.getAvailableResources()) {
            Table itemTable = new Table();
            MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText(resourceMarket.getResourceType().toString()).build());
            MyWrappedLabel marketPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(formatNrToCurrencyWithDollar(resourceMarket.getPrice())).build());
            itemTable.add(displayName).width(tableWidth / 2);
            itemTable.row();
            itemTable.add(marketPrice).width(tableWidth / 2);
            itemTable.setBackground(getNotSelectedBackground());
            itemTable.setTouchable(Touchable.enabled);
            itemTable.setName(resourceMarketName(resourceMarket));
            itemTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    resetInvMarketItemBackground();
                    if (selectedResource != null && selectedResource instanceof ResourceInventory) {
                        setSelectedResource(null);
                    }
                    if (selectedResource != null && resourceMarket.equalsResourceType(selectedResource)) {
                        setSelectedResource(null);
                    } else {
                        setSelectedResource(resourceMarket);
                        itemTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                    }
                    MyButton sellBuyButton = getRoot().findActor(BUYSELLBTN_NAME);
                    sellBuyButton.getCenterRowLabels().get(0).setText("Buy");
                    resetAmount();
                }
            });
            table.add(itemTable).width(tableWidth).height(INV_MARKET_ITEM_HEIGHT).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        }
        return table;
    }

    private void resetAmount() {
        amount = 0;
        amountLabel.setText(amount + "");
    }

    public Table createNumberPickerColumn(float numberPickerWidth) {
        Table table = new Table();
        List<Integer> modifValues = Arrays.asList(1, 10, 500);
        for (Integer val : modifValues) {
            table.add(createModifyAmountBtn(val, -1, true)).row();
        }
        table.add(createModifyAmountBtn(0, 2, true)).width(numberPickerWidth).row();
        amountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2))
                .setText(amount + "").setWidth(numberPickerWidth).build());
        table.add(amountLabel).row();
        MyButton buySellBtn = createBuySellBtn();
        table.add(buySellBtn).width(numberPickerWidth).row();
        table.add(createModifyAmountBtn(0, 2, false)).row();
        Collections.reverse(modifValues);
        for (Integer val : modifValues) {
            table.add(createModifyAmountBtn(val, -1, false)).row();
        }
        return table;
    }

    private MyButton createBuySellBtn() {
        MyButton buySellBtn = new ButtonBuilder().setButtonName(BUYSELLBTN_NAME)
                .setSingleLineText("Buy", FontManager.getNormalBigFontDim()).build();
        buySellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (buySellBtn.getCenterRowLabels().get(0).getText().toString().equals("Buy")) {
                    if (amount > 0) {
                        resourceTransactionsManager.buyResource(getSelectedResource(), amount);
                    }
                } else if (buySellBtn.getCenterRowLabels().get(0).getText().toString().equals("Sell")) {
                    resourceTransactionsManager.sellResource(getSelectedResource(), amount);
                }
                setSelectedResource(null);
                resetAmount();
                createInventory();
            }
        });
        return buySellBtn;
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
                if (getSelectedResource() != null) {
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
                        resetAmount();
                    }

                    int amountYouAffordAndHaveSpaceFor = ResourceTransactionsManager.getResourceAmountYouAfford(
                            getSelectedResource().getPrice(), currentGame.getMyInventory().getBudget());
                    amountYouAffordAndHaveSpaceFor = getAmountYouAfforAndHaveSpaceFor(amountYouAffordAndHaveSpaceFor);
                    if (amount > amountYouAffordAndHaveSpaceFor) {
                        amount = amountYouAffordAndHaveSpaceFor;
                    }
                    amountLabel.setText(amount + "");
                }
            }
        });
        return btn;
    }

    private String getModifyAmountBtnText(int modifAmount, int mult, boolean positive) {
        return mult == -1 ? ((positive ? "+" : "-") + modifAmount) : ((positive ? "*" : "/") + mult);
    }

    private NinePatchDrawable getNotSelectedBackground() {
        return GraphicUtils.getNinePatch(MainResource.transparent_background);
    }

    public void resetInvMarketItemBackground() {
        for (ResourceMarket resourceMarket : currentGame.getMarket().getAvailableResources()) {
            Actor actor = getRoot().findActor(resourceMarketName(resourceMarket));
            if (actor != null) {
                ((Table) actor).setBackground(getNotSelectedBackground());
            }
        }
        for (ResourceInventory resourceInventory : currentGame.getMyInventory().getAvailableResources()) {
            Actor actor = getRoot().findActor(resourceInventoryName(resourceInventory));
            if (actor != null) {
                ((Table) actor).setBackground(getNotSelectedBackground());
            }
        }
    }

    private String resourceInventoryName(ResourceInventory resourceInventory) {
        return "ResourceInventory" + resourceInventory.getResourceType().toString() + resourceInventory.getPrice();
    }

    private String resourceMarketName(ResourceMarket resourceMarket) {
        return "ResourceMarket" + resourceMarket.getResourceType().toString() + resourceMarket.getPrice();
    }

    public AbstractResource getSelectedResource() {
        return selectedResource;
    }

    public void setSelectedResource(AbstractResource selectedResource) {
        if (selectedResource == null) {
            resetInvMarketItemBackground();
        }
        this.selectedResource = selectedResource;
    }

    public Table createScrollTable(Table tableToAdd, String headerText, float invMarketHeight) {
        Table itemTable = new Table();
        itemTable.add(tableToAdd).height(invMarketHeight / 1.1f);
        ScrollPane scrollPane = new ScrollPane(itemTable);
        scrollPane.setScrollingDisabled(true, false);
        Table allTable = new Table();
        allTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(headerText).setSingleLineLabel().build())).row();
        allTable.add(scrollPane).expand();
        return allTable;
    }

    public static String formatNrToCurrencyWithDollar(int nr) {
        return formatNrToCurrency(nr) + " $";
    }

    public static String formatNrToCurrency(int nr) {
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
}
