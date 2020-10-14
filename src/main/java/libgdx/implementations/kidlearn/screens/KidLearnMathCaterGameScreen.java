package libgdx.implementations.kidlearn.screens;

import java.util.List;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterConfig;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterGameCreator;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterService;
import libgdx.screen.AbstractScreen;

public class KidLearnMathCaterGameScreen<T extends Enum & KidLearnMathCaterLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnMathCaterConfig config;
    KidLearnGameContext<T> gameContext;


    public KidLearnMathCaterGameScreen(KidLearnGameContext<T> gameContext) {
        this.gameContext = gameContext;
        config = createConfig();
    }

    private KidLearnMathCaterConfig createConfig() {
        KidLearnMathCaterService kidLearnMathCaterService = new KidLearnMathCaterService();
        List<Float> allCorrectNumbers = kidLearnMathCaterService.generateCorrectNumbers(gameContext.level);
        return new KidLearnMathCaterConfig<T>(
                (Class<T>) gameContext.level.getClass(),
                allCorrectNumbers,
                kidLearnMathCaterService.generateWrongNumbers(gameContext.level, allCorrectNumbers),
                gameContext.level.nrOfCorrectUnknownNumbers(),
                gameContext.level.asc());
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        new KidLearnMathCaterGameCreator(gameContext, config).create();
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(config.gameType);
    }

}
