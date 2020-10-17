package libgdx.implementations.kidlearn.screens;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsConfig;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsWordConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciPreDefConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciVerticalGameCreator;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.screen.AbstractScreen;

public class KidLearnSciGameScreen<T extends Enum & KidLearnMathCaterLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnSciConfig config;
    KidLearnGameContext<T> gameContext;


    public KidLearnSciGameScreen(KidLearnGameContext<T> gameContext) {
        this.gameContext = gameContext;
        config = createConfig();
    }

    private KidLearnSciConfig createConfig() {
        return new KidLearnSciConfig(Arrays.asList(
                createConfigItem("Head", MainResource.sound_on, Pair.of(1, 0)),
                createConfigItem("Left arm", MainResource.remove, Pair.of(1, 1)),
                createConfigItem("Body", MainResource.refresh_down, Pair.of(2, 1)),
                createConfigItem("Right arm", MainResource.heart_full, Pair.of(3, 1)),
                createConfigItem("Legs", MainResource.heart_full, Pair.of(1, 2))));
    }

    private KidLearnSciPreDefConfig createConfigItem(String text, Res res, Pair<Integer, Integer> posInMatrix) {
        return new KidLearnSciPreDefConfig(text, res, res, posInMatrix);
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        new KidLearnSciVerticalGameCreator(gameContext, config).create();
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
