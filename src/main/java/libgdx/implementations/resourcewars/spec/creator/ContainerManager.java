package libgdx.implementations.resourcewars.spec.creator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
import libgdx.implementations.resourcewars.ResourceWarsSpecificResource;
import libgdx.implementations.resourcewars.screens.ResourceWarsGameScreen;
import libgdx.implementations.resourcewars.spec.LocationPopup;
import libgdx.implementations.resourcewars.spec.logic.LocationMovementManager;
import libgdx.implementations.resourcewars.spec.logic.ResourceTransactionsManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.Market;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.implementations.resourcewars.spec.model.resource.AbstractResource;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceMarket;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerManager {

    public static final int LOCATION_UNLOCK_REPUTATION = 15;
    public static final int FINAL_BUDGET_TO_REACH_REPUTATION = 25;
    private static final int TOTAL_DAYS = 60;
    public static final int FINAL_BUDGET_TO_REACH = 1000000;
    private AbstractResource selectedResource;
    private CurrentGame currentGame;
    private int amount;
    private MyWrappedLabel amountLabel;
    private static String BUYSELLBTN_NAME = "BUYSELLBTN_NAME";
    private ResourceTransactionsManager resourceTransactionsManager;
    private static float INV_MARKET_ITEM_HEIGHT = ScreenDimensionsManager.getScreenWidthValue(15);
    private Integer increaseAmountButtonPressedSeconds = null;
    private Integer decreaseAmountButtonPressedSeconds = null;


    public ContainerManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
        this.resourceTransactionsManager = new ResourceTransactionsManager(currentGame);
    }

    public Table createFooter() {
        float btnWidth = ScreenDimensionsManager.getScreenWidth() / 3;
        String FOOTERTABLE_NAME = "FOOTERTABLE_NAME";
        Table table = getRoot().findActor(FOOTERTABLE_NAME);
        if (table == null) {
            table = new Table();
        }
        table.setName(FOOTERTABLE_NAME);
        table.clearChildren();
        MyButton changeCountryBtn = new ButtonBuilder().setWrappedText(currentGame.getMarket().getCurrentLocation().toString(), btnWidth).build();
        changeCountryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new LocationPopup(currentGame, Game.getInstance().getAbstractScreen()) {
                    @Override
                    public void hide() {
                        super.hide(Utils.createRunnableAction(new Runnable() {
                            @Override
                            public void run() {
                                resetControls();
                            }
                        }));
                    }
                }.addToPopupManager();
            }
        });
        MyButton passDayBtn = new ButtonBuilder().setWrappedText("Next day", btnWidth).build();
        passDayBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passDayButStayInSameLocation();
                resetControls();
            }
        });
        table.add(changeCountryBtn).width(btnWidth);
        table.add(passDayBtn).width(btnWidth);
        return table;
    }

    private void resetControls() {
        createHeader();
        createInventory();
        createMarket();
        updateAmount(0);
        createFooter();
    }

    private void increaseDaysPassed() {
        currentGame.setDaysPassed(currentGame.getDaysPassed() + 1);
    }

    public void passDayButStayInSameLocation() {
        Location currentLocation = currentGame.getMarket().getCurrentLocation();
        increaseDaysPassed();
        currentGame.getMarket().setCurrentLocation(currentLocation, currentGame.getMyInventory().getAvailableResourcesByType());
    }

    public void setAmount(int amount) {
        if (amount < 0) {
            this.amount = 0;
        } else {
            this.amount = amount;
        }
    }

    public MyWrappedLabel getAmountLabel() {
        return amountLabel;
    }

    public Table createHeader() {
        float tableWidth = ResourceWarsGameScreen.HEADERWIDTH;
        String HEADERTABLE_NAME = "HEADERTABLE_NAME";
        Table table = getRoot().findActor(HEADERTABLE_NAME);
        if (table == null) {
            table = new Table();
        }
        table.setName(HEADERTABLE_NAME);
        table.clearChildren();
        float infoWidth = tableWidth / 2;

        table.setWidth(tableWidth);
        Table tableRow1 = new Table();
        tableRow1.add(createHeaderInfoLabelPair("Remaining Days: ", (TOTAL_DAYS - currentGame.getDaysPassed()) + "", FontColor.BLACK)).width(infoWidth);
        tableRow1.add(createHeaderInfoLabelPair("Budget: ", formatNrToCurrencyWithDollar(currentGame.getMyInventory().getBudget()), getBudgetColor())).width(infoWidth);
        table.add(tableRow1).width(tableWidth).row();

        Table tableRow2 = new Table();
        tableRow2.add(createHeaderInfoLabelPair("Inventory: ", (Inventory.STARTING_MAX_CONTAINER - currentGame.getMyInventory().getContainerSpaceLeft()) + "/" + Inventory.STARTING_MAX_CONTAINER, getSpaceLeftColor())).width(infoWidth);
        tableRow2.add(createHeaderInfoLabelPair("Reputation: ", currentGame.getPlayerInfo().getReputation() + "%", getReputationColor())).width(infoWidth);
        table.add(tableRow2).width(tableWidth).row();

        Table tableRow3 = new Table();
        tableRow3.add(getNextObjectiveTable()).width(tableWidth).colspan(2);
        table.add(tableRow3).pad(MainDimen.vertical_general_margin.getDimen()).width(tableWidth);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return table;
    }

    private Table getNextObjectiveTable() {
        boolean areAllLocationsUnlocked = new LocationMovementManager(currentGame).areAllLocationsUnlocked();
        Table table = new Table();
        float width = ResourceWarsGameScreen.HEADERWIDTH / 1.5f;
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getNextObjectiveText(areAllLocationsUnlocked))
                .setWrappedLineLabel(width)
                .build())).width(width);
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.ORANGE, FontConfig.FONT_SIZE * 1.4f))
                .setText(getNextObjectiveReputation(areAllLocationsUnlocked))
                .build())).width(ResourceWarsGameScreen.HEADERWIDTH - width);
        return table;
    }

    private String getNextObjectiveReputation(boolean areAllLocationsUnlocked) {
        if (!areAllLocationsUnlocked) {
            return "+" + LOCATION_UNLOCK_REPUTATION + "%";
        } else {
            return "+" + FINAL_BUDGET_TO_REACH_REPUTATION + "%";
        }
    }

    private String getNextObjectiveText(boolean areAllLocationsUnlocked) {
        int nextLocationIndex = currentGame.getMarket().getCurrentLocation().getIndex() + 1;
        if (!areAllLocationsUnlocked) {
            return "Unlock " + Location.valueOf("LOC" + nextLocationIndex).toString();
        } else {
            return "Reach a budget of " + formatNrToCurrencyWithDollar(FINAL_BUDGET_TO_REACH);
        }
    }

    private FontColor getBudgetColor() {
        FontColor color = FontColor.BLACK;
        if (currentGame.getMyInventory().getBudget() == 0) {
            color = FontColor.RED;
        }
        return color;
    }

    private FontColor getReputationColor() {
        FontColor color = FontColor.BLACK;
        if (currentGame.getPlayerInfo().getReputation() >= 75) {
            color = FontColor.GREEN;
        }
        return color;
    }

    private FontColor getSpaceLeftColor() {
        int spaceLeft = Inventory.STARTING_MAX_CONTAINER - currentGame.getMyInventory().getContainerSpaceLeft();
        float onePercentOfValueContainerMax = (float) Inventory.STARTING_MAX_CONTAINER / 100f;
        FontColor color = FontColor.BLACK;
        if (spaceLeft >= onePercentOfValueContainerMax * 80) {
            color = FontColor.RED;
        } else if (spaceLeft >= onePercentOfValueContainerMax * 60) {
            color = FontColor.ORANGE;
        }
        return color;
    }

    private Table createHeaderInfoLabelPair(String text1, String text2, FontColor text2Color) {
        MyWrappedLabel l1 = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder()
                        .setSingleLineLabel()
                        .setFontConfig(new FontConfig(FontConfig.FONT_SIZE / 1.3f))
                        .setText(text1).build());
        MyWrappedLabel l2 = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setSingleLineLabel()
                        .setFontConfig(new FontConfig(text2Color.getColor(), FontConfig.FONT_SIZE / 1.1f)).setText(text2).build());
        Table table = new Table();
        l1.getLabels().get(0).setAlignment(Align.left);
        l2.getLabels().get(0).setAlignment(Align.left);
        table.add(l1);
        table.add(l2);
        return table;
    }

    public Table createInventory() {
        float tableWidth = ResourceWarsGameScreen.INVMARKETWIDTH;
        String INVENTORYTABLE_NAME = "INVENTORYTABLE_NAME";
        Table inventoryTable = initInvMarketTable(INVENTORYTABLE_NAME, isMaxInventoryItems() ? GraphicUtils.getNinePatch(MainResource.transparent_background) : GraphicUtils.getNinePatch(MainResource.popup_background));
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
                        .setFontColor(StringUtils.isNotBlank(actualSellPrice) ? getDisplayNameColor(resourceInventory.getResourceType()) : FontColor.GRAY)
                        .setText(resourceInventory.getResourceType().toString()).build());
                MyWrappedLabel actualSellPriceLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                        .setFontScale(FontManager.getSmallFontDim())
                        .setFontColor(StringUtils.isNotBlank(actualSellPrice) ? FontColor.GREEN : FontColor.GRAY)
                        .setText(actualSellPrice != null ? actualSellPrice : "").build());
                MyWrappedLabel pastBuyPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                        .setFontColor(FontColor.GRAY)
                        .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.7f))
                        .setText(formatNrToCurrencyWithDollar(resourceInventory.getPrice())).build());
                MyWrappedLabel invAmountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                        .setFontColor(StringUtils.isNotBlank(actualSellPrice) ? FontColor.GREEN : FontColor.GRAY)
                        .setText(resourceInventory.getAmount() + "").build());
                itemTable.add(displayName).width(tableWidth / 2);
                itemTable.add(pastBuyPrice).width(tableWidth / 2);
                itemTable.row();
                itemTable.add(actualSellPriceLabel).width(tableWidth / 2);
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
                addInvMarketItemToTable(tableWidth, inventoryTable, itemTable);
            }
        }
        return inventoryTable;
    }

    private Table initInvMarketTable(String tableName, NinePatchDrawable background) {
        Table table = getRoot().findActor(tableName);
        if (table == null) {
            table = new Table();
        }
        table.setBackground(background);
        table.setName(tableName);
        table.clearChildren();
        return table;
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
        String MARKETTABLE_NAME = "MARKETTABLE_NAME";
        Table table = initInvMarketTable(MARKETTABLE_NAME, GraphicUtils.getNinePatch(MainResource.popup_background));
        List<ResourceMarket> availableResources = market.getAvailableResources().stream()
                .sorted(Comparator.comparingInt(ResourceMarket::getPrice))
                .collect(Collectors.toList());
        for (ResourceMarket resourceMarket : availableResources) {
            int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(resourceMarket);
            Table itemTable = new Table();

            boolean isEnabled = amountYouAffordAndHaveSpaceFor > 0 && !isMaxInventoryItems();
            float nameWidth = tableWidth / 1.7f;
            FontColor displayNameColor = isEnabled ? getDisplayNameColor(resourceMarket.getResourceType()) : FontColor.GRAY;
            MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontColor(displayNameColor)
                    .setWrappedLineLabel(nameWidth)
                    .setText(resourceMarket.getResourceType().toString()).build());
            MyWrappedLabel marketPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontColor(isEnabled ? FontColor.GREEN : FontColor.GRAY)
                    .setText(formatNrToCurrencyWithDollar(resourceMarket.getPrice())).build());
            itemTable.add(displayName).width(nameWidth);
            itemTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText(formatNrToCurrencyWithDollar(resourceMarket.getResourceType().getStandardPrice()))
                    .setFontConfig(new FontConfig(FontColor.GRAY.getColor(), FontConfig.FONT_SIZE * 0.7f)).build()))
                    .width(tableWidth - nameWidth);
            itemTable.row();
            itemTable.add(marketPrice).width(nameWidth);
            float resMarketPriceDimen = MainDimen.horizontal_general_margin.getDimen() * 4;
            itemTable.add(GraphicUtils.getImage(getResourceForMarketPrice(resourceMarket, !isEnabled && !inventoryItemIsInMarket(resourceMarket)), resMarketPriceDimen)).width(resMarketPriceDimen).height(resMarketPriceDimen);
            itemTable.setBackground(getNotSelectedBackground());
            itemTable.setTouchable(Touchable.enabled);
            itemTable.setName(resourceMarketName(resourceMarket));
            itemTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(resourceMarket);
                    if (amountYouAffordAndHaveSpaceFor > 0 && !isMaxInventoryItems()) {
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
            addInvMarketItemToTable(tableWidth, table, itemTable);
        }
        return table;
    }

    private void addInvMarketItemToTable(float tableWidth, Table table, Table itemTable) {
        table.add(itemTable).width(tableWidth)
                .height(INV_MARKET_ITEM_HEIGHT)
                .padTop(MainDimen.vertical_general_margin.getDimen() / 2)
                .padBottom(MainDimen.vertical_general_margin.getDimen() / 2).row();
    }

    private boolean isMaxInventoryItems() {
        return currentGame.getMyInventory().getAvailableResources().size() >= Inventory.MAX_ITEMS_IN_INVENTORY;
    }

    private Res getResourceForMarketPrice(ResourceMarket resourceMarket, boolean disabled) {
        Res res = MainResource.transparent_background;
        if (resourceMarket.getPrice() < (resourceMarket.getResourceType().getStandardPrice() / 1.6f)) {
            res = disabled ? ResourceWarsSpecificResource.disableddowndownarrow : ResourceWarsSpecificResource.downdownarrow;
        } else if (resourceMarket.getPrice() < resourceMarket.getResourceType().getStandardPrice()) {
            res = disabled ? ResourceWarsSpecificResource.disableddownarrow : ResourceWarsSpecificResource.downarrow;
        } else if (resourceMarket.getPrice() > (resourceMarket.getResourceType().getStandardPrice() * 1.6f)) {
            res = disabled ? ResourceWarsSpecificResource.disabledupuparrow : ResourceWarsSpecificResource.upuparrow;
        } else if (resourceMarket.getPrice() > resourceMarket.getResourceType().getStandardPrice()) {
            res = disabled ? ResourceWarsSpecificResource.disableduparrow : ResourceWarsSpecificResource.uparrow;
        }
        return res;
    }

    private boolean inventoryItemIsInMarket(ResourceMarket resourceMarket) {
        for (ResourceInventory resourceInventory : currentGame.getMyInventory().getAvailableResources()) {
            if (resourceInventory.getResourceType() == resourceMarket.getResourceType()) {
                return true;
            }
        }
        return false;
    }

    private FontColor getDisplayNameColor(ResourceType resourceType) {
        FontColor displayNameColor = FontColor.BLACK;
        if (currentGame.getMarket().getCurrentLocation().getExpensiveResources().contains(resourceType)) {
            displayNameColor = FontColor.RED;
        } else if (currentGame.getMarket().getCurrentLocation().getCheapResources().contains(resourceType)) {
            displayNameColor = FontColor.BLUE;
        }
        return displayNameColor;
    }

    private void updateAmount(int amount) {
        this.amount = amount;
        amountLabel.setText(this.amount + "");
    }

    public Table createNumberPickerColumn2(float numberPickerWidth) {
        Table table = new Table();
        List<Integer> modifValues = Arrays.asList(500, 10, 1);
        table.add(createModifyAmountBtn(0, 2, true)).width(numberPickerWidth).row();
        for (Integer val : modifValues) {
            table.add(createModifyAmountBtn(val, -1, true)).row();
        }
        amountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2))
                .setText(amount + "").setWidth(numberPickerWidth).build());
        table.add(amountLabel).row();
        MyButton buySellBtn = createBuySellBtn();
        table.add(buySellBtn).width(numberPickerWidth).row();
        Collections.reverse(modifValues);
        for (Integer val : modifValues) {
            table.add(createModifyAmountBtn(val, -1, false)).row();
        }
        table.add(createModifyAmountBtn(0, 2, false)).row();
        return table;
    }

    private MyButton createBuySellBtn() {
        MyButton buySellBtn = new ButtonBuilder().setButtonName(BUYSELLBTN_NAME)
                .setSingleLineText("Buy", FontManager.getNormalBigFontDim()).build();
        buySellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getSelectedResource() != null) {
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


    public Table createNumberPickerColumn(float numberPickerWidth) {
        Table table = new Table();
        MyButton increaseBtn = new ButtonBuilder().setText("+").build();
        increaseBtn.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                increaseAmountButtonPressedSeconds = null;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                increaseAmountButtonPressedSeconds = 0;
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        table.add(increaseBtn).width(numberPickerWidth).row();
        amountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2))
                .setText(amount + "").setWidth(numberPickerWidth).build());
        table.add(amountLabel).row();
        MyButton decreaseBtn = new ButtonBuilder().setText("-").build();
        decreaseBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                decreaseAmountButtonPressedSeconds = null;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                decreaseAmountButtonPressedSeconds = 0;
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        table.add(decreaseBtn).width(numberPickerWidth).row();
        MyButton buySellBtn = createBuySellBtn();
        table.add(buySellBtn).width(numberPickerWidth).row();
        return table;
    }

    public Integer getIncreaseAmountButtonPressedSeconds() {
        return increaseAmountButtonPressedSeconds;
    }

    public void setIncreaseAmountButtonPressedSeconds(Integer increaseAmountButtonPressedSeconds) {
        this.increaseAmountButtonPressedSeconds = increaseAmountButtonPressedSeconds;
    }

    public Integer getDecreaseAmountButtonPressedSeconds() {
        return decreaseAmountButtonPressedSeconds;
    }

    public void setDecreaseAmountButtonPressedSeconds(Integer decreaseAmountButtonPressedSeconds) {
        this.decreaseAmountButtonPressedSeconds = decreaseAmountButtonPressedSeconds;
    }

    public int getAmount() {
        return amount;
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

    public Table createScrollTable(Table tableToAdd, float invMarketHeight) {
        Table itemTable = new Table();
        itemTable.add(tableToAdd).height(invMarketHeight / 1.1f);
        ScrollPane scrollPane = new ScrollPane(itemTable);
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, true);
        Table allTable = new Table();
        allTable.add(scrollPane);
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
