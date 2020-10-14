package libgdx.implementations.kidlearn.screens;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterMenuCreator;
import libgdx.screen.AbstractScreen;

public class KidLearnLevelScreen<T extends Enum & KidLearnMathCaterLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    Class<T> levelType;
    private KidLearnMathCaterMenuCreator mathMenuCreator;

    public KidLearnLevelScreen(Class<T> levelType) {
        mathMenuCreator = new KidLearnMathCaterMenuCreator(this);
        this.levelType = levelType;
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        mathMenuCreator.createLevelMenu(levelType, "Order");
    }

    @Override
    public void onBackKeyPress() {
        mathMenuCreator.createChooseLevelMenu();
    }

}
