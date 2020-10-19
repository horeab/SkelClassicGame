package libgdx.implementations.kidlearn.screens;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnVerticalGameCreator;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciPreDefConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyGameCreator;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.screen.AbstractScreen;

public class KidLearnSciGameScreen<T extends Enum & KidLearnMathCaterLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnSciRecyConfig config;
    KidLearnGameContext<T> gameContext;


    public KidLearnSciGameScreen(KidLearnGameContext<T> gameContext) {
        this.gameContext = gameContext;
        config = createConfig();
    }

    private KidLearnSciRecyConfig createConfig() {
        return new KidLearnSciRecyConfig(Arrays.asList(
                new KidLearnWordImgConfig("Dog", MainResource.sound_on),
                new KidLearnWordImgConfig("Cat", MainResource.sound_off),
                new KidLearnWordImgConfig("Cow", MainResource.remove),
                new KidLearnWordImgConfig("Horse", MainResource.refresh_down),
                new KidLearnWordImgConfig("Lion", MainResource.heart_full)),
                Arrays.asList(MainResource.sound_off, MainResource.sound_on, MainResource.refresh_down));
    }

    private KidLearnSciPreDefConfig createConfigItem(String text, Res res, Pair<Integer, Integer> posInMatrix) {
        return new KidLearnSciPreDefConfig(text, res, res, posInMatrix);
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        new KidLearnSciRecyGameCreator(gameContext, config).create();
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
