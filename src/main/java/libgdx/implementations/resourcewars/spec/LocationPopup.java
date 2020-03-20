package libgdx.implementations.resourcewars.spec;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.resourcewars.ResourceWarsSpecificResource;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.logic.GamePreferencesManager;
import libgdx.implementations.resourcewars.spec.logic.LocationMovementManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;
import libgdx.utils.model.FontConfig;

import java.util.List;

public class LocationPopup extends MyPopup<AbstractScreen, AbstractScreenManager> {


    private final CurrentGame currentGame;
    private LocationMovementManager locationMovementManager;
    private GamePreferencesManager gamePreferencesManager;

    public LocationPopup(CurrentGame currentGame, AbstractScreen abstractScreen) {
        super(abstractScreen);
        this.currentGame = currentGame;
        locationMovementManager = new LocationMovementManager(currentGame);
        gamePreferencesManager = new GamePreferencesManager();
    }

    @Override
    protected void addButtons() {
        for (Location location : Location.values()) {
            ButtonSkin buttonSkin = SkelClassicButtonSkin.RESOURCEWARS_LOCATION;
            boolean isCurrentLocation = location == currentGame.getMarket().getCurrentLocation();
            boolean isDisabled = isCurrentLocation;
            String text = location.getDisplayName();
            String info = "";
            if (!gamePreferencesManager.isLocationUnlocked(location)) {
                buttonSkin = SkelClassicButtonSkin.RESOURCEWARS_LOCATION_LOCKED;
                info = SkelGameLabel.l_unlock.getText(ContainerManager.formatNrToCurrencyWithDollar(location.getUnlockPrice()));
                isDisabled = location.getUnlockPrice() > currentGame.getMyInventory().getBudget();
            } else if (!isCurrentLocation) {
                buttonSkin = SkelClassicButtonSkin.RESOURCEWARS_LOCATION_TRAVELTO;
                info = SkelGameLabel.l_travel.getText(ContainerManager.formatNrToCurrencyWithDollar(location.getTravelPrice(currentGame.getDaysPassed())));
                isDisabled = location.getTravelPrice(currentGame.getDaysPassed()) > currentGame.getMyInventory().getBudget();
            }
            SkelClassicButtonSize resourcewarsLocationBtn = SkelClassicButtonSize.RESOURCEWARS_LOCATION_BTN;
            MyButton myButton = new ButtonBuilder()
                    .setFixedButtonSize(resourcewarsLocationBtn)
                    .setButtonSkin(buttonSkin)
                    .setDisabled(isDisabled)
                    .setWrappedText(text, resourcewarsLocationBtn.getWidth()).build();
            MyWrappedLabel locInfo = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(
                    FontConfig.FONT_SIZE * 0.7f))
                    .setText(info).build());
            Table locationTable = new Table();
            myButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    locationMovementManager.passDayAndMoveLocation(location);
                    hide();
                }
            });
            float locationWidth = getPrefWidth() / 2.5f;
            float cheapExpResourcesWidth = (getPrefWidth() - locationWidth) / 2;
            Table locInfoTable = new Table();
            locInfoTable.add(myButton).width(myButton.getWidth()).height(myButton.getHeight()).row();
            locInfoTable.add(locInfo).width(locationWidth);
            locationTable.add(locInfoTable).width(locationWidth);
            locationTable
                    .add(createCheapExpensiveResourceTable(true, location.getCheapResources())).width(cheapExpResourcesWidth);
            locationTable
                    .add(createCheapExpensiveResourceTable(false, location.getExpensiveResources())).width(cheapExpResourcesWidth);
            getContentTable().add(locationTable).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        }
    }

    private Table createCheapExpensiveResourceTable(boolean cheap, List<ResourceType> list) {
        Table table = new Table();
        for (ResourceType resourceType : list) {
            Image downArrow = GraphicUtils.getImage(ResourceWarsSpecificResource.downarrow);
            Image upArrow = GraphicUtils.getImage(ResourceWarsSpecificResource.uparrow);
            float unitSize = MainDimen.horizontal_general_margin.getDimen() * 3;
            float textWidth = unitSize * 3;
            MyWrappedLabel resLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.7f))
                    .setWrappedLineLabel(textWidth)
                    .setText(resourceType.getDisplayName()).build());
            resLabel.fitToContainer();
            table.add(resLabel).width(textWidth);
            table.add(cheap ? downArrow : upArrow).width(unitSize).height(unitSize);
            table.row();
        }
        return table;
    }

    @Override
    protected String getLabelText() {
        return "";
    }
}
