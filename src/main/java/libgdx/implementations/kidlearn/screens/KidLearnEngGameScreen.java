package libgdx.implementations.kidlearn.screens;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsConfig;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;

public class KidLearnEngGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnGameContext gameContext;


    public KidLearnEngGameScreen(KidLearnGameContext gameContext) {
        this.gameContext = gameContext;
    }

    private KidLearnEngWordsConfig createWordsConfig() {
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
        if (gameContext.level instanceof KidLearnEngWordsLevel) {
            KidLearnEngWordsLevel inst = (KidLearnEngWordsLevel) gameContext.level;
            List<KidLearnWordImgConfig> configs = new ArrayList<>();
            for (Pair<String, Res> v : inst.vals) {
                configs.add(new KidLearnWordImgConfig(v.getLeft(), v.getRight()));
            }
            Collections.shuffle(configs);
            new KidLearnEngWordsGameCreator(gameContext, new KidLearnEngWordsConfig(configs)).create();
        } else {
            KidLearnEngHangmanLevel inst = (KidLearnEngHangmanLevel) gameContext.level;
            Pair<String, Res> rand = KidLearnUtils.getLevelValsToPlay(gameContext, inst.vals);
            setUpAllTable();
            getAllTable().add(new KidLearnEngHangmanGameCreator(gameContext, new KidLearnWordImgConfig(rand.getLeft(), rand.getRight())).createTable())
                    .padTop(MainDimen.vertical_general_margin.getDimen() * 4);
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
