package libgdx.implementations.resourcewars.spec;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.implementations.resourcewars.spec.logic.LocationMovementManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public class LocationPopup extends MyPopup<AbstractScreen, AbstractScreenManager> {


    private final CurrentGame currentGame;

    public LocationPopup(CurrentGame currentGame, AbstractScreen abstractScreen) {
        super(abstractScreen);
        this.currentGame = currentGame;
    }

    @Override
    protected void addButtons() {
        for (Location location : Location.values()) {
            MyButton myButton = new ButtonBuilder().setText(location.toString()).build();
            myButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    new LocationMovementManager(currentGame).passDayAndMoveLocation(location);
                    hide();
                }
            });
            addButton(myButton);
        }
    }

    @Override
    protected String getLabelText() {
        return "Move to another country";
    }
}
