package libgdx.implementations.kidlearn.screens;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterConfig;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterGameCreator;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterService;
import libgdx.screen.AbstractScreen;

public class KidLearnMathGameScreen<T extends Enum & KidLearnMathCaterLevel & KidLearnLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnMathCaterConfig config;
    KidLearnGameContext<T> gameContext;
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();


    public KidLearnMathGameScreen(KidLearnGameContext<T> gameContext) {
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
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(KidLearnSpecificResource.scroll_background_math);
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        new KidLearnMathCaterGameCreator(gameContext, config).create();
        kidLearnDifficultyService.setBackgroundDiff(new KidLearnPreferencesManager().getDifficultyLevel(KidLearnMathLevelScreen.difficultyLevelClass()), getBackgroundStage());
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(config.gameType);
    }

}
