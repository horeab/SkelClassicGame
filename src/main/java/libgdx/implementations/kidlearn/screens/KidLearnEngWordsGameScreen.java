package libgdx.implementations.kidlearn.screens;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsConfig;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.screen.AbstractScreen;

public class KidLearnEngWordsGameScreen<T extends Enum & KidLearnMathCaterLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnEngWordsConfig config;
    KidLearnGameContext<T> gameContext;


    public KidLearnEngWordsGameScreen(KidLearnGameContext<T> gameContext) {
        this.gameContext = gameContext;
        config = createConfig();
    }

    private KidLearnEngWordsConfig createConfig() {
        return new KidLearnEngWordsConfig();
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        new KidLearnEngWordsGameCreator(gameContext, config).create();
    }

    @Override
    public void onBackKeyPress() {
//        screenManager.showLevelScreen(config.gameType);
    }

}
