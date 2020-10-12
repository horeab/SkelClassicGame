package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.numberordering.KidLearnNumberOrderingCreator;
import libgdx.screen.AbstractScreen;

public class KidLearnGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private Table allTable;
    private MyButton hoverBackButton;

    public KidLearnGameScreen() {
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();

        new KidLearnNumberOrderingCreator(getAbstractScreen()).create();
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
