package libgdx.implementations.resourcewars.spec.creator;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.resourcewars.screens.ResourceWarsGameScreen;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.Market;
import libgdx.implementations.resourcewars.spec.model.resource.AbstractResource;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceMarket;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.model.FontConfig;

public class ContainerManager {

    private AbstractResource selectedResource;
    private CurrentGame currentGame;

    public ContainerManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
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

    public Table inventoryItem(Inventory inventory, Market market, float tableWidth) {
        Table table = new Table();
        if (inventory.getAvailableResources().isEmpty()) {
            Table itemTable = new Table();
            itemTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("Empty").build()));
            itemTable.setWidth(tableWidth);
            table.add(itemTable).row();
        } else {
            for (ResourceInventory resourceInventory : inventory.getAvailableResources()) {
                Table itemTable = new Table();
                MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(resourceInventory.getResourceType().getDisplayName()).build());
                MyWrappedLabel actualSellPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(
                        getActualSellPrice(resourceInventory, market)).build());
                MyWrappedLabel pastBuyPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(formatNrToCurrencyWithDollar(resourceInventory.getPrice())).build());
                MyWrappedLabel amount = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(resourceInventory.getAmount() + "").build());
                itemTable.add(displayName).width(tableWidth / 2);
                itemTable.add(actualSellPrice).width(tableWidth / 2);
                itemTable.row();
                itemTable.add(pastBuyPrice).width(tableWidth / 2);
                itemTable.add(amount).width(tableWidth / 2);
                itemTable.setWidth(tableWidth);
                itemTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                table.add(itemTable).pad(MainDimen.vertical_general_margin.getDimen()).row();
            }
        }
        return table;
    }

    private String getActualSellPrice(ResourceInventory resourceInventory, Market market) {
        for (ResourceMarket marketElem : market.getAvailableResources()) {
            if (resourceInventory.getResourceType().equals(marketElem.getResourceType())) {
                return formatNrToCurrencyWithDollar(marketElem.getPrice());
            }
        }
        return null;
    }

    public Table marketItem(Market market, float tableWidth) {
        Table table = new Table();
        for (ResourceMarket resourceMarket : market.getAvailableResources()) {
            Table itemTable = new Table();
            MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(resourceMarket.getResourceType().getDisplayName()).build());
            MyWrappedLabel marketPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(formatNrToCurrencyWithDollar(resourceMarket.getPrice())).build());
            itemTable.add(displayName).width(tableWidth / 2);
            itemTable.row();
            itemTable.add(marketPrice).width(tableWidth / 2);
            itemTable.setBackground(GraphicUtils.getNinePatch(MainResource.transparent_background));
            itemTable.setTouchable(Touchable.enabled);
            itemTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedResource = resourceMarket;
                    MyButton sellBuyButton = Game.getInstance().getAbstractScreen().getRoot().findActor(ResourceWarsGameScreen.BUYSELLBTN_NAME);
                    sellBuyButton.setText("Buy");
                    itemTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                }
            });
            table.add(itemTable).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        }
        return table;
    }

    public AbstractResource getSelectedResource() {
        return selectedResource;
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
