package libgdx.implementations.resourcewars.spec;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfig;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.resourcewars.ResourceWarsSpecificResource;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.logic.InGameStoreManager;
import libgdx.implementations.resourcewars.spec.logic.LocationMovementManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

import java.util.List;

public class LocationPopup extends MyPopup<AbstractScreen, AbstractScreenManager> {


    private final CurrentGame currentGame;
    private LocationMovementManager locationMovementManager;
    private InGameStoreManager inGameStoreManager;

    public LocationPopup(CurrentGame currentGame, AbstractScreen abstractScreen) {
        super(abstractScreen);
        this.currentGame = currentGame;
        locationMovementManager = new LocationMovementManager(currentGame);
        inGameStoreManager = new InGameStoreManager();
    }

    @Override
    protected void addButtons() {
        for (Location location : Location.values()) {
            MainButtonSkin buttonSkin = MainButtonSkin.DEFAULT;
            boolean isCurrentLocation = location == currentGame.getMarket().getCurrentLocation();
            boolean isDisabled = isCurrentLocation;
            String text = location.toString();
            String info = "";
            if(isCurrentLocation){
                buttonSkin = MainButtonSkin.TRANSPARENT;
            }
            if (!inGameStoreManager.isLocationUnlocked(location)) {
                info = "Unlock: " + ContainerManager.formatNrToCurrencyWithDollar(location.getUnlockPrice());
                isDisabled = location.getUnlockPrice() > currentGame.getMyInventory().getBudget();
            } else if (!isCurrentLocation) {
                info = "Travel: " + ContainerManager.formatNrToCurrencyWithDollar(location.getTravelPrice(currentGame.getDaysPassed()));
                isDisabled = location.getTravelPrice(currentGame.getDaysPassed()) > currentGame.getMyInventory().getBudget();
            }
            SkelClassicButtonSize resourcewarsLocationBtn = SkelClassicButtonSize.RESOURCEWARS_LOCATION_BTN;
            MyButton myButton = new ButtonBuilder()
                    .setFixedButtonSize(resourcewarsLocationBtn)
                    .setButtonSkin(buttonSkin)
                    .setDisabled(isDisabled)
                    .setWrappedText(text, resourcewarsLocationBtn.getWidth()).build();
            MyWrappedLabel locInfo = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(info).build());
            Table locationTable = new Table();
            myButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    locationMovementManager.passDayAndMoveLocation(location);
                    hide();
                }
            });
            float cheapExpResources = getPrefWidth() / 4;
            float textWidth = getPrefWidth() / 2;
            Table locInfoTable = new Table();
            locInfoTable.add(myButton).width(myButton.getWidth()).height(myButton.getHeight()).row();
            locInfoTable.add(locInfo).width(textWidth);
            locationTable.add(locInfoTable).width(textWidth);
            locationTable
                    .add(createCheapExpensiveResourceTable(true, location.getCheapResources())).width(cheapExpResources);
            locationTable
                    .add(createCheapExpensiveResourceTable(false, location.getExpensiveResources())).width(cheapExpResources);
            getContentTable().add(locationTable).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        }
    }

    private Table createCheapExpensiveResourceTable(boolean cheap, List<ResourceType> list) {
        Table table = new Table();
        for (ResourceType resourceType : list) {
            Image downArrow = GraphicUtils.getImage(ResourceWarsSpecificResource.downarrow);
            Image upArrow = GraphicUtils.getImage(ResourceWarsSpecificResource.uparrow);
            float side = MainDimen.horizontal_general_margin.getDimen() * 3;
            float textWidth = side * 2;
            table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.7f))
                    .setWrappedLineLabel(textWidth)
                    .setText(resourceType.toString()).build())).width(textWidth);
            table.add(cheap ? downArrow : upArrow).width(side).height(side);
            table.row();
        }
        return table;
    }

    @Override
    protected String getLabelText() {
        return "Travel to another location";
    }
}
