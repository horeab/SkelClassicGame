package libgdx.implementations.kidlearn.screens;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterMenuCreator;
import libgdx.screen.AbstractScreen;

public class KidLearnGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;

    private KidLearnMathCaterMenuCreator mathMenuCreator;

    public KidLearnGameScreen() {
        mathMenuCreator = new KidLearnMathCaterMenuCreator(this);
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        mathMenuCreator.create();
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
