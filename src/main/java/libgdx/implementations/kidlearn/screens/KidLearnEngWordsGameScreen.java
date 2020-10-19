package libgdx.implementations.kidlearn.screens;

import java.util.Arrays;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsConfig;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.MainResource;
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
        return new KidLearnEngWordsConfig(Arrays.asList(
                new KidLearnWordImgConfig("Dog", MainResource.sound_on),
                new KidLearnWordImgConfig("Cat", MainResource.sound_off),
                new KidLearnWordImgConfig("Cow", MainResource.remove),
                new KidLearnWordImgConfig("Horse", MainResource.refresh_down),
                new KidLearnWordImgConfig("Lion", MainResource.heart_full)));
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        new KidLearnEngWordsGameCreator(gameContext, config).create();
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
