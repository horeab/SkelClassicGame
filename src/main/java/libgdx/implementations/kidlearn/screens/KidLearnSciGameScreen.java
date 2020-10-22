package libgdx.implementations.kidlearn.screens;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnVerticalGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciPreDefConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSingleLabelConfig;
import libgdx.resources.Res;
import libgdx.screen.AbstractScreen;

public class KidLearnSciGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnGameContext gameContext;


    public KidLearnSciGameScreen(KidLearnGameContext gameContext) {
        this.gameContext = gameContext;
    }


    private KidLearnSingleLabelConfig createHumanBodyConfig() {
        return new KidLearnSingleLabelConfig(Arrays.asList("Head", "Left arm", "Body", "Right arm", "Legs"));
    }

//    private KidLearnSciMultipleItemsConfig createRecycleConfig() {
//        return new KidLearnSciMultipleItemsConfig(Arrays.asList(
//                new KidLearnWordImgConfig("Dog", MainResource.sound_on),
//                new KidLearnWordImgConfig("Cat", MainResource.sound_off),
//                new KidLearnWordImgConfig("Cow", MainResource.remove),
//                new KidLearnWordImgConfig("Horse", MainResource.refresh_down),
//                new KidLearnWordImgConfig("Lion", MainResource.heart_full)),
//                Arrays.asList(MainResource.sound_off, MainResource.sound_on, MainResource.refresh_down));
//    }

    private KidLearnSciPreDefConfig createConfigItem(String text, Res res, Pair<Integer, Integer> posInMatrix) {
        return new KidLearnSciPreDefConfig(text, res, res, posInMatrix);
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        if (true) {
            new KidLearnVerticalGameCreator(gameContext, createHumanBodyConfig()).create();
        } else if (true) {
//            new KidLearnMultipleItemsGameCreator(gameContext, createRecycleConfig()).create();
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
