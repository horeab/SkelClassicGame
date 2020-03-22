package libgdx.implementations.resourcewars.spec.creator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.ResourceWarsSpecificResource;
import libgdx.implementations.resourcewars.screens.ResourceWarsGameScreen;
import libgdx.implementations.resourcewars.spec.LocationPopup;
import libgdx.implementations.resourcewars.spec.logic.GamePreferencesManager;
import libgdx.implementations.resourcewars.spec.logic.HighScorePreferencesManager;
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
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerManager {

    public static final int LOCATION_UNLOCK_REPUTATION = 15;
    public static final int FINAL_BUDGET_TO_REACH_REPUTATION = 25;
    public static final int TOTAL_DAYS = 60;
    public static final int FINAL_BUDGET_TO_REACH = 1000000;
    private static float SELECTEDRESOURCEMONEYCHANGEFONTSIZE = FontConfig.FONT_SIZE * 1.3f;
    private AbstractResource selectedResource;
    private CurrentGame currentGame;
    private int amount;
    private MyWrappedLabel amountLabel;
    private MyWrappedLabel selectedResourceMoneyLabel;
    private static String BUYSELLBTN_NAME = "BUYSELLBTN_NAME";
    private ResourceTransactionsManager resourceTransactionsManager;
    private HighScorePreferencesManager highScorePreferencesManager = new HighScorePreferencesManager();
    private LocationMovementManager locationMovementManager;
    private static float INV_MARKET_ITEM_HEIGHT = ScreenDimensionsManager.getScreenWidthValue(15);
    private Integer increaseAmountButtonPressedSeconds = null;
    private Integer decreaseAmountButtonPressedSeconds = null;


    public ContainerManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
        this.resourceTransactionsManager = new ResourceTransactionsManager(currentGame);
        this.locationMovementManager = new LocationMovementManager(currentGame);
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
        MyButton changeCountryBtn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_LOCATION)
                .setFixedButtonSize(SkelClassicButtonSize.RESOURCEWARS_LOCATION_BTN)
                .setWrappedText(currentGame.getMarket().getCurrentLocation().getDisplayName(),
                        SkelClassicButtonSize.RESOURCEWARS_LOCATION_BTN.getWidth()).build();
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
        MyButton passDayBtn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_LOCATION_NEXTDAY)
                .setFixedButtonSize(SkelClassicButtonSize.RESOURCEWARS_LOCATION_BTN)
                .setWrappedText(SkelGameLabel.l_next_day.getText(), SkelClassicButtonSize.RESOURCEWARS_LOCATION_BTN.getWidth()).build();
        passDayBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passDayButStayInSameLocation();
                resetControls();
            }
        });
        table.add(changeCountryBtn).width(changeCountryBtn.getWidth()).height(changeCountryBtn.getHeight())
                .padRight(MainDimen.horizontal_general_margin.getDimen() * 7);
        table.add(passDayBtn).width(passDayBtn.getWidth()).height(passDayBtn.getHeight());
        return table;
    }

    private void resetControls() {
        setSelectedResource(null);
        createHeader(getBudgetColor());
        createInventory();
        createMarket();
        createNumberPickerColumn();
        setAmount(0);
        createFooter();
    }

    public void passDayButStayInSameLocation() {
        Location currentLocation = currentGame.getMarket().getCurrentLocation();
        locationMovementManager.increaseDaysPassed();
        currentGame.getMarket().setCurrentLocation(currentLocation, currentGame.getMyInventory().getAvailableResourcesByType());
    }

    public Table createHeader(FontColor budgetColor) {
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
        tableRow1.add(createHeaderInfoLabelPair(SkelGameLabel.l_remaining_days.getText() + ": ", (TOTAL_DAYS - currentGame.getDaysPassed()) + "", FontColor.BLACK)).width(infoWidth);
        tableRow1.add(createHeaderInfoLabelPair(SkelGameLabel.l_inventory.getText() + ": ", (Inventory.STARTING_MAX_CONTAINER - currentGame.getMyInventory().getContainerSpaceLeft()) + " / " + Inventory.STARTING_MAX_CONTAINER, getSpaceLeftColor())).width(infoWidth);
        table.add(tableRow1).width(tableWidth).row();

        Table tableRow2 = new Table();
        tableRow2.add(createHeaderInfoLabelPair(SkelGameLabel.l_budget.getText() + ": ", formatNrToCurrencyWithDollar(currentGame.getMyInventory().getBudget()), budgetColor)).width(infoWidth);
        tableRow2.add(createHeaderInfoLabelPair(SkelGameLabel.l_reputation.getText() + ": ", currentGame.getPlayerInfo().getReputation() + "%", getReputationColor())).width(infoWidth);
        table.add(tableRow2).width(tableWidth).row();

        Table tableRow3 = new Table();
        tableRow3.add(getNextObjectiveTable()).width(tableWidth);
        table.add(tableRow3).pad(MainDimen.vertical_general_margin.getDimen()).width(tableWidth);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return table;
    }

    private Table getNextObjectiveTable() {
        boolean areAllLocationsUnlocked = locationMovementManager.areAllLocationsUnlocked();
        Table table = new Table();
        float width = ResourceWarsGameScreen.HEADERWIDTH;
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getNextObjectiveText(areAllLocationsUnlocked))
                .setWrappedLineLabel(width / 2)
                .build()).fitToContainer()).width(width / 2);
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.ORANGE, FontConfig.FONT_SIZE * 1.2f))
                .setWrappedLineLabel(width / 2)
                .setText(getNextObjectiveReputation(areAllLocationsUnlocked))
                .build()).fitToContainer()).width(width / 2);
        return table;
    }

    private String getNextObjectiveReputation(boolean areAllLocationsUnlocked) {
        if (!areAllLocationsUnlocked) {
            return "+" + LOCATION_UNLOCK_REPUTATION + "% " + SkelGameLabel.l_reputation.getText();
        } else {
            return "+" + FINAL_BUDGET_TO_REACH_REPUTATION + "% " + SkelGameLabel.l_reputation.getText();
        }
    }

    private String getNextObjectiveText(boolean areAllLocationsUnlocked) {
        Location location = locationMovementManager.nextLocationToUnlock();
        if (location != null) {
            return SkelGameLabel.l_unlock.getText(Location.valueOf("LOC" + location.getIndex()).getDisplayName());
        } else {
            return SkelGameLabel.l_reachbudget.getText(formatNrToCurrencyWithDollar(FINAL_BUDGET_TO_REACH));
        }
    }

    public FontColor getBudgetColor() {
        FontColor color = getEnoughBudgetColor();
        if (currentGame.getMyInventory().getBudget() == 0) {
            color = getNotEnoughBudgetColor();
        }
        return color;
    }

    private FontColor getEnoughBudgetColor() {
        return FontColor.BLACK;
    }

    private FontColor getNotEnoughBudgetColor() {
        return FontColor.RED;
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
                        .setFontConfig(new FontConfig(text2Color.getColor(), FontConfig.FONT_SIZE * 1.1f)).setText(text2).build());
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
        Table inventoryTable = initInvMarketTable(INVENTORYTABLE_NAME);
        Inventory inventory = currentGame.getMyInventory();
        Market market = currentGame.getMarket();
        int remainingSlotsToAdd = Inventory.MAX_ITEMS_IN_INVENTORY;
        List<ResourceInventory> availableResources = inventory.getAvailableResources().stream()
                .sorted(Comparator.comparingInt(ResourceInventory::getPrice))
                .collect(Collectors.toList());
        for (ResourceInventory resourceInventory : availableResources) {
            Table itemTable = new Table();
            Integer actualSellPrice = getActualSellPrice(resourceInventory, market);
            final boolean isEnabled = isInventoryItemEnabled(resourceInventory);
            MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setWidth(tableWidth / 2)
                    .setFontColor(isEnabled ? getDisplayNameColor(resourceInventory.getResourceType()) : FontColor.GRAY)
                    .setText(resourceInventory.getResourceType().getDisplayName()).build())
                    .fitToContainer();
            MyWrappedLabel actualSellPriceLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontScale(FontManager.getSmallFontDim())
                    .setFontColor(isEnabled ? FontColor.GREEN : FontColor.GRAY)
                    .setText(isEnabled ? formatNrToCurrencyWithDollar(actualSellPrice) : "").build());
            MyWrappedLabel pastBuyPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontColor(FontColor.GRAY)
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.7f))
                    .setText(formatNrToCurrencyWithDollar(resourceInventory.getPrice())).build());
            MyWrappedLabel invAmountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontColor(isEnabled ? FontColor.GREEN : FontColor.GRAY)
                    .setText(resourceInventory.getAmount() + "").build());
            itemTable.add(pastBuyPrice).width(tableWidth / 2);
            itemTable.add(displayName).width(tableWidth / 2);
            itemTable.row();
            itemTable.add(invAmountLabel).width(tableWidth / 2);
            itemTable.add(actualSellPriceLabel).width(tableWidth / 2);
            itemTable.setTouchable(isEnabled ? Touchable.enabled : Touchable.disabled);
            itemTable.setName(resourceInventoryName(resourceInventory));
            itemTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isEnabled) {
                        if (selectedResource != null && selectedResource instanceof ResourceMarket) {
                            setSelectedResource(null);
                        }
                        if (selectedResource != null && resourceInventory.equalsResourceType(selectedResource) && selectedResource instanceof ResourceInventory) {
                            resetInvMarketItemBackground();
                            setSelectedResource(null);
                        } else {
                            resetInvMarketItemBackground();
                            itemTable.setBackground(GraphicUtils.getNinePatch(ResourceWarsSpecificResource.invmarket_itemselected));
                            setSelectedResource(resourceInventory);
                        }
                        if (selectedResource != null) {
                            setAmount(resourceInventory.getAmount());
                        } else {
                            setAmount(0);
                        }
                    }
                }
            });
            addInvMarketItemToTable(tableWidth, inventoryTable, itemTable, isEnabled);
            remainingSlotsToAdd--;
        }
        for (int i = 0; i < remainingSlotsToAdd; i++) {
            Table itemTable = new Table();
            itemTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(SkelGameLabel.l_empty.getText()).build()));
            itemTable.setBackground(GraphicUtils.getNinePatch(ResourceWarsSpecificResource.invmarket_itemnotselected));
            addInvMarketItemToTable(tableWidth, inventoryTable, itemTable, false);
        }
        return inventoryTable;
    }

    private boolean isInventoryItemEnabled(ResourceInventory resourceInventory) {
        Integer actualSellPrice = getActualSellPrice(resourceInventory, currentGame.getMarket());
        return !currentGame.getMyInventory().getAvailableResources().isEmpty() && actualSellPrice != null;
    }

    private Table initInvMarketTable(String tableName) {
        Table table = getRoot().findActor(tableName);
        if (table == null) {
            table = new Table();
        }
        table.setName(tableName);
        table.clearChildren();
        return table;
    }

    private Group getRoot() {
        return Game.getInstance().getAbstractScreen().getRoot();
    }

    private Integer getActualSellPrice(ResourceInventory resourceInventory, Market market) {
        for (ResourceMarket marketElem : market.getAvailableResources()) {
            if (resourceInventory.equalsResourceType(marketElem)) {
                return marketElem.getPrice();
            }
        }
        return null;
    }

    public Table createMarket() {
        float tableWidth = ResourceWarsGameScreen.INVMARKETWIDTH;
        Market market = currentGame.getMarket();
        String MARKETTABLE_NAME = "MARKETTABLE_NAME";
        Table table = initInvMarketTable(MARKETTABLE_NAME);
        List<ResourceMarket> availableResources = market.getAvailableResources().stream()
                .sorted(Comparator.comparingInt(ResourceMarket::getPrice))
                .collect(Collectors.toList());
        for (ResourceMarket resourceMarket : availableResources) {
            int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(resourceMarket);
            Table itemTable = new Table();

            boolean isEnabled = isMarketItemEnabled(resourceMarket);
            float nameWidth = tableWidth / 1.7f;
            FontColor displayNameColor = isEnabled ? getDisplayNameColor(resourceMarket.getResourceType()) : FontColor.GRAY;
            MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontColor(displayNameColor)
                    .setWidth(nameWidth)
                    .setText(resourceMarket.getResourceType().getDisplayName()).build()).fitToContainer();
            MyWrappedLabel marketPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontColor(isEnabled ? FontColor.GREEN : FontColor.GRAY)
                    .setText(formatNrToCurrencyWithDollar(resourceMarket.getPrice())).build());
            itemTable.add(displayName).width(nameWidth);
            itemTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText(formatNrToCurrencyWithDollar(resourceMarket.getResourceType().getStandardPrice()))
                    .setFontConfig(new FontConfig(FontColor.GRAY.getColor(), FontConfig.FONT_SIZE * 0.7f)).build())
                    .fitToContainer())
                    .width(tableWidth - nameWidth);
            itemTable.row();
            itemTable.add(marketPrice).width(nameWidth);
            float resMarketPriceDimen = MainDimen.horizontal_general_margin.getDimen() * 4;
            itemTable.add(GraphicUtils.getImage(getResourceForMarketPrice(resourceMarket, !isEnabled && !inventoryItemIsInMarket(resourceMarket)), resMarketPriceDimen)).width(resMarketPriceDimen).height(resMarketPriceDimen);
            itemTable.setTouchable(isEnabled ? Touchable.enabled : Touchable.disabled);
            itemTable.setName(resourceMarketName(resourceMarket));
            itemTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isMarketItemEnabled(resourceMarket)) {
                        resetInvMarketItemBackground();
                        if (selectedResource != null && selectedResource instanceof ResourceInventory) {
                            setSelectedResource(null);
                        }
                        if (selectedResource != null && resourceMarket.equalsResourceType(selectedResource) && selectedResource instanceof ResourceMarket) {
                            setSelectedResource(null);
                        } else {
                            setSelectedResource(resourceMarket);
                            itemTable.setBackground(GraphicUtils.getNinePatch(ResourceWarsSpecificResource.invmarket_itemselected));
                            setAmount(amountYouAffordAndHaveSpaceFor);
                        }
                    }
                }
            });
            addInvMarketItemToTable(tableWidth, table, itemTable, isEnabled);
        }
        return table;
    }

    private boolean isMarketItemEnabled(ResourceMarket resourceMarket) {
        int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(resourceMarket);
        return amountYouAffordAndHaveSpaceFor > 0 && !isMaxInventoryItems();
    }

    private void addInvMarketItemToTable(float tableWidth, Table table, Table itemTable, boolean isEnabled) {
        itemTable.setBackground(GraphicUtils.getNinePatch(
                isEnabled ? getNotSelectedBackground() : getDisabledItemBackground()));
        table.add(itemTable).width(tableWidth)
                .height(INV_MARKET_ITEM_HEIGHT)
                .padTop(MainDimen.vertical_general_margin.getDimen() / 2)
                .padBottom(MainDimen.vertical_general_margin.getDimen() / 2).row();
    }

    private MainResource getDisabledItemBackground() {
        return MainResource.transparent_background;
    }

    private boolean isMaxInventoryItems() {
        return currentGame.getMyInventory().getAvailableResources().size() >= Inventory.MAX_ITEMS_IN_INVENTORY;
    }

    private Res getResourceForMarketPrice(ResourceMarket resourceMarket, boolean disabled) {
        Res res = getDisabledItemBackground();
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

    public void setAmount(int amount) {
        if (selectedResource != null) {
            if (amount < 0) {
                this.amount = 0;
            } else {
                this.amount = amount;
            }
            String prefix = "+";
            FontColor selectedResourceMoneyLabelFontColor = FontColor.GREEN;
            int moneyChange = 0;
            if (selectedResource instanceof ResourceMarket) {
                int amountYouAffordAndHaveSpaceFor = getAmountYouAffordAndHaveSpaceFor(ContainerManager.this.getSelectedResource());
                if (amount > amountYouAffordAndHaveSpaceFor) {
                    this.amount = amountYouAffordAndHaveSpaceFor;
                    createHeader(getNotEnoughBudgetColor());
                } else {
                    createHeader(getBudgetColor());
                }
                moneyChange = this.amount * selectedResource.getPrice();
                selectedResourceMoneyLabelFontColor = FontColor.RED;
                prefix = "-";
            } else if (selectedResource instanceof ResourceInventory) {
                if (amount > ((ResourceInventory) selectedResource).getAmount()) {
                    this.amount = ((ResourceInventory) selectedResource).getAmount();
                }
                Integer actualSellPrice = getActualSellPrice((ResourceInventory) selectedResource, currentGame.getMarket());
                if (actualSellPrice != null) {
                    moneyChange = this.amount * actualSellPrice;
                } else {
                    moneyChange = 0;
                }
            }
            amountLabel.setText(this.amount + "");
            prefix = this.amount == 0 ? "" : prefix;
            selectedResourceMoneyLabel.setFontConfig(new FontConfig(selectedResourceMoneyLabelFontColor.getColor(),
                    SELECTEDRESOURCEMONEYCHANGEFONTSIZE));
            selectedResourceMoneyLabel.setText(prefix + formatNrToCurrencyWithDollar(moneyChange));
        } else {
            selectedResourceMoneyLabel.setText("");
        }
    }

    private MyButton createBuySellBtn() {
        ButtonSkin buttonSkin = SkelClassicButtonSkin.RESOURCEWARS_AMOUNT_BUY;
        if (getSelectedResource() != null && getSelectedResource() instanceof ResourceInventory) {
            buttonSkin = SkelClassicButtonSkin.RESOURCEWARS_AMOUNT_SELL;
        }
        MyButton buySellBtn = new ButtonBuilder().setDisabled(getSelectedResource() == null)
                .setButtonSkin(buttonSkin).setButtonName(BUYSELLBTN_NAME).build();
        buySellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getSelectedResource() != null) {
                    if (getSelectedResource() instanceof ResourceMarket) {
                        if (amount > 0) {
                            resourceTransactionsManager.buyResource(getSelectedResource(), amount);
                        }
                    } else if (getSelectedResource() instanceof ResourceInventory) {
                        resourceTransactionsManager.sellResource(getSelectedResource(), amount);
                    }
                    setSelectedResource(null);
                    createInventory();
                    createHeader(getBudgetColor());
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

    public Table createNumberPickerColumn() {
        float numberPickerWidth = ResourceWarsGameScreen.NUMBERPICKERWIDTH;
        String tableName = "NUMBERPICKERCOLUMN";
        Table table = getRoot().findActor(tableName);
        if (table == null) {
            table = new Table();
        }
        table.setName(tableName);
        table.clearChildren();
        MyButton delBtn = new ButtonBuilder().setText("").setDisabled(getSelectedResource() == null)
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_AMOUNT_DELETE)
                .build();
        delBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setAmount(0);
            }
        });
        MyButton increaseBtn = new ButtonBuilder().setText("")
                .setDisabled(getSelectedResource() == null)
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_AMOUNT_PLUS).build();
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
        amountLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2))
                .setText(amount + "").setWidth(numberPickerWidth).build());
        MyButton decreaseBtn = new ButtonBuilder().setText("").setDisabled(getSelectedResource() == null)
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_AMOUNT_MINUS).build();
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
        MyButton buySellBtn = createBuySellBtn();
        selectedResourceMoneyLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(SELECTEDRESOURCEMONEYCHANGEFONTSIZE)).setText("").setSingleLineLabel().build());
        table.add(selectedResourceMoneyLabel).width(numberPickerWidth).row();
        table.add().growY().row();
        float dimen = MainDimen.vertical_general_margin.getDimen();
        table.add(buySellBtn).width(numberPickerWidth).padBottom(dimen).row();
        table.add(increaseBtn).width(numberPickerWidth).padBottom(dimen).row();
        table.add(amountLabel).padBottom(dimen).row();
        table.add(decreaseBtn).width(numberPickerWidth).padBottom(dimen).row();
        table.add(delBtn).width(numberPickerWidth).padBottom(dimen).row();
        table.add().growY().row();
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

    private Res getNotSelectedBackground() {
        return ResourceWarsSpecificResource.invmarket_itemnotselected;
    }

    public void resetInvMarketItemBackground() {
        for (ResourceMarket resourceMarket : currentGame.getMarket().getAvailableResources()) {
            Actor actor = getRoot().findActor(resourceMarketName(resourceMarket));
            if (actor != null) {
                Table table = (Table) actor;
                Res background = table.getTouchable() == Touchable.disabled ? getDisabledItemBackground() : getNotSelectedBackground();
                table.setBackground(GraphicUtils.getNinePatch(background));
            }
        }
        for (ResourceInventory resourceInventory : currentGame.getMyInventory().getAvailableResources()) {
            Actor actor = getRoot().findActor(resourceInventoryName(resourceInventory));
            if (actor != null) {
                Table table = (Table) actor;
                Res background = table.getTouchable() == Touchable.disabled ? getDisabledItemBackground() : getNotSelectedBackground();
                table.setBackground(GraphicUtils.getNinePatch(background));
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
            setAmount(0);
        }
        this.selectedResource = selectedResource;
        createNumberPickerColumn();
        if (selectedResource == null) {
            setAmount(0);
        }
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

    public static void gameOver(CurrentGame currentGame) {
        int currentRep = currentGame.getPlayerInfo().getReputation();
        int currentDaysPassed = currentGame.getDaysPassed();
        final HighScorePreferencesManager highScorePreferencesManager = new HighScorePreferencesManager();
        int maxRep = highScorePreferencesManager.getMaxReputation();
        int maxDaysPassed = highScorePreferencesManager.getMaxDays();
        MutableBoolean newHighScore = new MutableBoolean(false);
        if (currentDaysPassed > ContainerManager.TOTAL_DAYS
                ||
                currentRep >= 100) {
            new GamePreferencesManager().reset();
            AbstractScreen abstractScreen = Game.getInstance().getAbstractScreen();
            ((Table) abstractScreen.getRoot().findActor(ResourceWarsGameScreen.ALLTABLE_NAME)).clearChildren();
            if (currentRep > maxRep || (currentRep == maxRep && currentDaysPassed < maxDaysPassed)) {
                highScorePreferencesManager.putMaxDays(currentDaysPassed);
                highScorePreferencesManager.putMaxReputation(currentRep);
                newHighScore.setTrue();
            }
            new MyPopup<AbstractScreen, ResourceWarsScreenManager>(abstractScreen) {
                @Override
                protected String getLabelText() {
                    String text = "";
                    if (newHighScore.booleanValue()) {
                        text = MainGameLabel.l_congratulations.getText();
                        text = text + "\n";
                        text = text + MainGameLabel.l_highscore_record.getText();
                        text = text + "\n";
                        text = text + "\n";
                    }
                    text = text + SkelGameLabel.l_highscorebudget.getText(
                            currentRep, currentDaysPassed
                    );
                    return text;
                }

                @Override
                protected void addButtons() {

                }

                @Override
                public void hide() {
                    super.hide();
                    screenManager.showMainScreen();
                }
            }.addToPopupManager();
        }
    }
}
