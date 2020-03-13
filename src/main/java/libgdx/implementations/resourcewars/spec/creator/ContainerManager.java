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
import libgdx.implementations.resourcewars.spec.logic.HealthManager;
import libgdx.implementations.resourcewars.spec.logic.InGameStoreManager;
import libgdx.implementations.resourcewars.spec.logic.ResourceTransactionsManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.Market;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.implementations.resourcewars.spec.model.resource.AbstractResource;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceMarket;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerManager {

    private AbstractResource selectedResource;
    private CurrentGame currentGame;
    private int amount;
    private MyWrappedLabel amountLabel;
    public static String BUYSELLBTN_NAME = "BUYSELLBTN_NAME";
    private static String INVENTORYTABLE_NAME = "INVENTORYTABLE_NAME";
    private static String MARKETTABLE_NAME = "MARKETTABLE_NAME";
    private static String HEADERTABLE_NAME = "HEADERTABLE_NAME";
    private ResourceTransactionsManager resourceTransactionsManager;
    private HealthManager healthManager;
    private InGameStoreManager storeManager;
    private static float INV_MARKET_ITEM_HEIGHT = ScreenDimensionsManager.getScreenWidthValue(15);

    public ContainerManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
        this.resourceTransactionsManager = new ResourceTransactionsManager(currentGame);
        healthManager = new HealthManager(currentGame);
        storeManager = new InGameStoreManager();
    }

    public Table createFooter(float tableWidth) {
        Table table = new Table();
        float btnWidth = tableWidth / 3;
        MyButton changeCountryBtn = new ButtonBuilder().setWrappedText("Unites States", btnWidth).build();
        MyButton passDayBtn = new ButtonBuilder().setWrappedText("Next day", btnWidth).build();
        passDayBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passDayButStayInSameLocation();
                resetControls();
            }
        });
        MyButton storeBtn = new ButtonBuilder().setWrappedText("Store", btnWidth).build();
        table.add(changeCountryBtn).width(btnWidth);
        table.add(passDayBtn).width(btnWidth);
        table.add(storeBtn).width(btnWidth);
        return table;
    }

    private void resetControls() {
        createHeader();
        createInventory();
        createMarket();
        updateAmount(0);
    }

    public void passDayAndMoveLocation(Location newLocation) {
        if (storeManager.isLocationUnlocked(newLocation)) {
            spendMoneyOnTravel(newLocation);
        } else {
            unlockLocation(newLocation);
        }
        processHealthAndThreat(newLocation);
        increaseDaysPassed();
        currentGame.getMarket().setCurrentLocation(newLocation, currentGame.getMyInventory().getAvailableResourcesByType());
    }

    private void spendMoneyOnTravel(Location newLocation) {
        currentGame.getMyInventory().setBudget(currentGame.getMyInventory().getBudget() - newLocation.getTravelPrice(currentGame.getDaysPassed()));
    }

    private void increaseThreat(Location location) {
        currentGame.getPlayerInfo().setThreat(currentGame.getPlayerInfo().getThreat() + location.getThreatIncrease());
    }

    private void unlockLocation(Location location) {
        storeManager.unlockLocation(location);
        currentGame.getMyInventory().setBudget(currentGame.getMyInventory().getBudget() - location.getUnlockPrice());
    }

    private void increaseDaysPassed() {
        currentGame.setDaysPassed(currentGame.getDaysPassed() + 1);
    }

    public void passDayButStayInSameLocation() {
        Location currentLocation = currentGame.getMarket().getCurrentLocation();
        increaseDaysPassed();
        healthManager.processHealth();
        currentGame.getMarket().setCurrentLocation(currentLocation, currentGame.getMyInventory().getAvailableResourcesByType());
    }

    public void processHealthAndThreat(Location currentLocation) {
        healthManager.processHealth();
        increaseThreat(currentLocation);
    }


    public Table createHeader() {
        float tableWidth = ResourceWarsGameScreen.HEADERWIDTH;
        Table table = getRoot().findActor(HEADERTABLE_NAME);
        if (table == null) {
            table = new Table();
        }
        table.setName(HEADERTABLE_NAME);
        table.clearChildren();
        float infoWidth = tableWidth / 2;

        table.setWidth(tableWidth);
        Table tableRow1 = new Table();
        tableRow1.add(createHeaderInfoLabelPair("Budget: ", formatNrToCurrencyWithDollar(currentGame.getMyInventory().getBudget()))).width(infoWidth);
        tableRow1.add(createHeaderInfoLabelPair("Health: ", currentGame.getPlayerInfo().getHealth() + "/100")).width(infoWidth);
        table.add(tableRow1).width(tableWidth).row();

        Table tableRow2 = new Table();
        int totalInventory = 100;
        tableRow2.add(createHeaderInfoLabelPair("Inventory: ", (totalInventory - currentGame.getMyInventory().getContainerSpaceLeft()) + "/" + totalInventory)).width(infoWidth);
        tableRow2.add(createHeaderInfoLabelPair("Reputation: ", currentGame.getPlayerInfo().getReputation() + "%")).width(infoWidth);
        table.add(tableRow2).width(tableWidth).row();

        Table tableRow3 = new Table();
        tableRow3.add(createHeaderInfoLabelPair("Days: ", currentGame.getDaysPassed() + "")).width(infoWidth);
        tableRow3.add(createHeaderInfoLabelPair("Threat: ", currentGame.getPlayerInfo().getReputation() + "%")).width(infoWidth);
        table.add(tableRow3).width(tableWidth);

        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return table;
    }

    private Table createHeaderInfoLabelPair(String text1, String text2) {
        MyWrappedLabel l1 = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder()
                        .setSingleLineLabel()
                        .setFontConfig(new FontConfig(FontConfig.FONT_SIZE / 1.3f))
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
            List<ResourceInventory> availableResources = inventory.getAvailableResources().stream()
                    .sorted(Comparator.comparingInt(ResourceInventory::getPrice))
                    .collect(Collectors.toList());
            for (ResourceInventory resourceInventory : availableResources) {
                Table itemTable = new Table();
                String actualSellPrice = getActualSellPrice(resourceInventory, market);
                MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                        .setFontColor(StringUtils.isNotBlank(actualSellPrice) ? FontColor.GREEN : FontColor.RED)
                        .setText(resourceInventory.getResourceType().toString()).build());
                MyWrappedLabel actualSellPriceLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                        .setText(actualSellPrice != null ? actualSellPrice : "").build());
                MyWrappedLabel pastBuyPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                        .setFontColor(FontColor.GRAY)
                        .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.8f))
                        .setText(formatNrToCurrencyWithDollar(resourceInventory.getPrice())).build());
                MyWrappedLabel invAmountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(resourceInventory.getAmount() + "").build());
                itemTable.add(displayName).width(tableWidth / 2);
                itemTable.add(actualSellPriceLabel).width(tableWidth / 2);
                itemTable.row();
                itemTable.add(pastBuyPrice).width(tableWidth / 2);
                itemTable.add(invAmountLabel).width(tableWidth / 2);
                itemTable.setBackground(getNotSelectedBackground());
                itemTable.setTouchable(Touchable.enabled);
                itemTable.setName(resourceInventoryName(resourceInventory));
                itemTable.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!currentGame.getMyInventory().getAvailableResources().isEmpty() && StringUtils.isNotBlank(actualSellPrice)) {
                            if (selectedResource != null && selectedResource instanceof ResourceMarket) {
                                setSelectedResource(null);
                            }
                            if (selectedResource != null && resourceInventory.equalsResourceType(selectedResource) && selectedResource instanceof ResourceInventory) {
                                resetInvMarketItemBackground();
                                setSelectedResource(null);
                            } else {
                                resetInvMarketItemBackground();
                                itemTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                                setSelectedResource(resourceInventory);
                                MyButton sellBuyButton = getRoot().findActor(BUYSELLBTN_NAME);
                                sellBuyButton.getCenterRowLabels().get(0).setText("Sell");
                            }
                            if (selectedResource != null) {
                                updateAmount(resourceInventory.getAmount());
                            } else {
                                updateAmount(0);
                            }
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
        List<ResourceMarket> availableResources = market.getAvailableResources().stream()
                .sorted(Comparator.comparingInt(ResourceMarket::getPrice))
                .collect(Collectors.toList());
        for (ResourceMarket resourceMarket : availableResources) {
            int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(resourceMarket);
            Table itemTable = new Table();
            MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontColor(amountYouAffordAndHaveSpaceFor > 0 ? FontColor.GREEN : FontColor.RED)
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
                    int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(resourceMarket);
                    if (amountYouAffordAndHaveSpaceFor > 0) {
                        resetInvMarketItemBackground();
                        if (selectedResource != null && selectedResource instanceof ResourceInventory) {
                            setSelectedResource(null);
                        }
                        if (selectedResource != null && resourceMarket.equalsResourceType(selectedResource) && selectedResource instanceof ResourceMarket) {
                            setSelectedResource(null);
                        } else {
                            setSelectedResource(resourceMarket);
                            itemTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                            updateAmount(amountYouAffordAndHaveSpaceFor);
                        }
                        MyButton sellBuyButton = getRoot().findActor(BUYSELLBTN_NAME);
                        sellBuyButton.getCenterRowLabels().get(0).setText("Buy");
                    }
                }
            });
            table.add(itemTable).width(tableWidth).height(INV_MARKET_ITEM_HEIGHT).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        }
        return table;
    }

    private void updateAmount(int amount) {
        this.amount = amount;
        amountLabel.setText(this.amount + "");
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
                createInventory();
                createHeader();
                createMarket();
            }
        });
        return buySellBtn;
    }

    private int getAmountYouAffordAndHaveSpaceFor(int amountYouAfford) {
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
                        updateAmount(0);
                    }

                    if (selectedResource instanceof ResourceMarket) {
                        int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(ContainerManager.this.getSelectedResource());
                        if (amount > amountYouAffordAndHaveSpaceFor) {
                            amount = amountYouAffordAndHaveSpaceFor;
                        }
                    } else if (selectedResource instanceof ResourceInventory) {
                        if (amount > ((ResourceInventory) selectedResource).getAmount()) {
                            amount = ((ResourceInventory) selectedResource).getAmount();
                        }
                    }
                    amountLabel.setText(amount + "");
                }
            }
        });
        return btn;
    }

    private int getAmountYouAffordAndHaveSpaceFor(AbstractResource resource) {
        if (resource != null) {
            int amountYouAffordAndHaveSpaceFor = ResourceTransactionsManager.getResourceAmountYouAfford(
                    resource.getPrice(), currentGame.getMyInventory().getBudget());
            amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(amountYouAffordAndHaveSpaceFor);
            return amountYouAffordAndHaveSpaceFor;
        }
        return 0;
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
            updateAmount(0);
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
